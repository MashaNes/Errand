from rest_framework import viewsets, generics
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework_extensions.mixins import NestedViewSetMixin
from django.shortcuts import get_object_or_404
from rest_framework.authtoken.models import Token
from rest_framework.authtoken.views import ObtainAuthToken
from . import models
from . import serializers
from . import parsers
from . import utils

'''
Endpoints:
+ GET users_info/{id} 				// Returns only basic user data
+ GET users/{id} 					// Returns full user data
+ GET filtered_users/ 				// Returns users based on filters

TODO: Return separately full user info

+ GET requests_info/{id} 			// Returns only basic request data
+ GET requests/{id} 				// Returns full request data (including offers and rating)
- GET filtered_requests/ 			// Returns users based on filters
+ GET achievements/{id}				// Returns list of all achievements
- GET stats/ (A)		 			// Returns statistics based on filters

TODO: Return request from user for rating

+ POST login/                       // Login as some user
- POST logout/                      // Logout user
+ POST user_create/					// Creates new user
+ POST benefit_add/					// Adds new user to benefit list
+ POST address_add/					// Adds new address to addresses
+ POST working_hours_add/ 			// Adds new working hours for user
+ POST user_service_add/			// Adds new user service for user
- POST offer_create/ 				// Creates offer for request
+ POST request_create/ 				// Creates request
- POST rate_user/ 					// Adds new rating for completed request
- POST report_user/					// Reports user
- POST ban_user/ (A)	 			// Bans user
~ POST achievement_create/ (A)	 	// Creates new achievement
~ POST service_type_create/ (A)		// Creates new service type

- UPDATE request_update/ 			// Edits request
- UPDATE users_info_update/			// Updates basic user data
- UPDATE benefit_update/			// Updates benefit to benefit list
- UPDATE address_update/			// Updates address to addresses
- UPDATE working_hours_update/ 		// Updates working hours for user
- UPDATE user_service_update/		// Updates user service for user

+ DELETE request_cancel/ 			// Cancels request (when pending)
+ DELETE benefit_remove/ 			// Removes user from benefit list
+ DELETE address_remove/ 			// Removes address from user
+ DELETE working_hours_remove/ 		// Removes working hours from user
+ DELETE user_service_remove/ 		// Removes user service from user

(DEL)
+ GET userservice/{id}				// Returns full userservice data
'''


''' 
    USER
=============
'''
# POST login/
class TokenObtainView(ObtainAuthToken):
    def post(self, request, *args, **kwargs):
        serializer = self.serializer_class(data=request.data,
                                           context={'request':request})
        serializer.is_valid(raise_exception=True)
        user = serializer.validated_data['user']
        _s = serializers.UserSerializer(user)
        token, _ = Token.objects.get_or_create(user=user)
        custom_response = {
            'token': token.key,
            'user': _s.data
        }
        return Response(custom_response)

# POST user_create/
class UserCreate(generics.ListCreateAPIView):
    authentication_classes = ()
    permission_classes = ()
    serializer_class = serializers.UserSerializer

    def create(self, request):
        user = parsers.parse_user(request.data)
        user.set_password(request.data['password'])
        user.save()
        if request.data['picture']:
            picture = parsers.parse_picture(request.data['picture'], 'users/' + str(user.id))
            user.picture = picture
            user.save()
        Token.objects.create(user=user)
        fulluser = models.FullUser(user=user)
        fulluser.save()
        serializer = serializers.UserSerializer(user)
        return Response(serializer.data)

# GET users_info/{id}
class UserViewSet(NestedViewSetMixin, viewsets.ModelViewSet):
    queryset = models.User.objects.all()

    def list(self, request):
        serializer = serializers.UserSerializer(self.queryset, many=True)
        for _q, _s in zip(self.queryset, serializer.data):
            _s['picture'] = utils.encode_img('users/' + str(_q.id))

        # TODO: Add pagination
        custom_response = {
            'count' : len(serializer.data),
            'next' : None,
            'prev' : None,
            'results' : serializer.data
        }

        return Response(custom_response)

    def retrieve(self, request, pk):
        user = get_object_or_404(self.queryset, pk=pk)
        serializer = serializers.UserSerializer(user)
        serializer.data['user'] = utils.encode_img('users/' + str(user.id))

        return Response(serializer.data)

# GET users/{id}
class FullUserViewSet(NestedViewSetMixin, viewsets.ModelViewSet):
    queryset = models.FullUser.objects.all()

    def list(self, request):
        serializer = serializers.FullUserSerializer(self.queryset, many=True)
        for _q, _s in zip(self.queryset, serializer.data):
            _s['user']['picture'] = utils.encode_img('users/' + str(_q.user.id))

        # TODO: Add pagination
        custom_response = {
            'count' : len(serializer.data),
            'next' : None,
            'prev' : None,
            'results' : serializer.data
        }

        return Response(custom_response)

    def retrieve(self, request, pk):
        fulluser = get_object_or_404(self.queryset, pk=pk)
        serializer = serializers.FullUserSerializer(fulluser)
        serializer.data['user']['picture'] = utils.encode_img('users/' + str(fulluser.user.id))

        return Response(serializer.data)

# GET filtered_users/
class FilterUserViewSet(NestedViewSetMixin, viewsets.ModelViewSet):
    queryset = models.FullUser.objects.all()

    def list(self, request):
        self.queryset = utils.filter_user(self.queryset, request.data)
        serializer = serializers.UserSerializer(self.queryset, many=True)

        for _q, _s in zip(self.queryset, serializer.data):
            _s['picture'] = utils.encode_img('users/' + str(_q.id))

        # TODO: Add pagination
        custom_response = {
            'count' : len(serializer.data),
            'next' : None,
            'prev' : None,
            'results' : serializer.data
        }

        return Response(custom_response)

# POST benefit_add/
class BenefitAdd(generics.ListCreateAPIView):
    serializer_class = serializers.BenefitSerializer

    def create(self, request):
        created_by = request.data['created_by']
        user = models.FullUser.objects.get(id=created_by)

        for b in user.benefitlist.all():
            if b.benefit_user.id == request.data['benefit_user']:
                return Response({
                    'success' : False,
                    'description' : 'User is in the list.'})

        benefit = parsers.parse_benefit(request.data)
        benefit.save()
        user.benefitlist.add(benefit)
        user.save()
        serializer = serializers.BenefitSerializer(benefit)
        serializer.data['benefit_user']['picture'] = utils.encode_img('users/' + \
                                                        str(benefit.benefit_user.id))
        return Response(serializer.data)

# DELETE benefit_remove/
class BenefitRemove(generics.RetrieveDestroyAPIView):
    def destroy(self, request):
        created_by = request.data['created_by']
        benefit_user = request.data['benefit_user']
        user = models.FullUser.objects.get(id=created_by)
        ben_user = models.User.objects.get(id=benefit_user)

        benefits = user.benefitlist.all()
        response = {'succes' : False, 'description' : 'User is not in the list.'}
        for b in benefits:
            if b.benefit_user.id == ben_user.id:
                user.benefitlist.remove(b)
                user.save()
                response = {'succes' : True}
                break

        return Response(response)

# POST address_add/
class AddressAdd(generics.ListCreateAPIView):
    serializer_class = serializers.AddressSerializer

    def create(self, request):
        address = parsers.parse_address(request.data)
        created_by = request.data['created_by']
        user = models.FullUser.objects.get(id=created_by)
        user.addresses.add(address)
        user.save()
        serializer = serializers.AddressSerializer(address)
        return Response(serializer.data)

# DELETE address_remove/
class AddressRemove(generics.RetrieveDestroyAPIView):
    def destroy(self, request):
        created_by = request.data['created_by']
        user = models.FullUser.objects.get(id=created_by)
        adr = request.data['address']

        found = False
        for a in user.addresses.all():
            if a.id == adr:
                found = True
                break

        if not found:
            return Response({
                'succes' : False,
                'description' : 'Address is not in the list.'})

        address = models.Address.objects.get(id=adr)
        user.addresses.remove(address)
        user.save()

        return Response({'succes' : True})

# POST working_hours_add/
class WorkingHoursAdd(generics.ListCreateAPIView):
    serializer_class = serializers.WorkingHourSerializer

    def create(self, request):
        working_hours = parsers.parse_working_hour(request.data)
        working_hours.save()
        created_by = request.data['created_by']
        user = models.FullUser.objects.get(id=created_by)
        user.working_hours.add(working_hours)
        user.save()
        serializer = serializers.WorkingHourSerializer(working_hours)
        return Response(serializer.data)

# DELETE working_hours_remove/
class WokringHoursRemove(generics.RetrieveDestroyAPIView):
    def destroy(self, request):
        created_by = request.data['created_by']
        user = models.FullUser.objects.get(id=created_by)
        wid = request.data['working_hours']

        found = False
        for w in user.working_hours.all():
            if w.id == wid:
                found = True
                break

        if not found:
            return Response({
                'succes' : False,
                'description' : 'Working hours are not in the list.'})

        _working_hours = models.WorkingHour.objects.get(id=wid)
        user.working_hours.remove(_working_hours)
        user.save()

        return Response({'succes' : True})

# POST user_service_add/
class UserServiceAdd(generics.ListCreateAPIView):
    serializer_class = serializers.UserServiceSerializer

    def create(self, request):
        user_service = parsers.parse_user_service(request.data)
        user_service.save()
        created_by = request.data['created_by']
        user = models.FullUser.objects.get(id=created_by)
        user.services.add(user_service)
        user.save()
        serializer = serializers.UserServiceSerializer(user_service)
        return Response(serializer.data)

# DELETE user_services_remove/
class UserServiceRemove(generics.RetrieveDestroyAPIView):
    def destroy(self, request):
        created_by = request.data['created_by']
        user = models.FullUser.objects.get(id=created_by)
        sid = request.data['service']

        found = False
        for s in user.services.all():
            if s.id == sid:
                found = True
                break

        if not found:
            return Response({
                'succes' : False,
                'description' : 'Service is not in the list.'})

        service = models.UserService.objects.get(id=sid)
        user.services.remove(service)
        user.save()

        return Response({'succes' : True})


''' 
    REQUEST
==============
'''

# POST request_create/
class RequestCreate(generics.ListCreateAPIView):
    serializer_class = serializers.RequestSerializer

    def create(self, request):
        req = parsers.parse_request(request.data)
        created_by = request.data['created_by']
        user = models.FullUser.objects.get(id=created_by)
        user.requests.add(req)
        user.save()
        serializer = serializers.RequestSerializer(req)
        return Response(serializer.data)

# DELETE user_services_remove/
class RequestCancel(generics.RetrieveDestroyAPIView):
    def destroy(self, request):
        created_by = request.data['created_by']
        user = models.FullUser.objects.get(id=created_by)
        rid = request.data['request']

        found = False
        for r in user.requests.all():
            if r.id == rid:
                found = True
                break

        if not found:
            return Response({
                'succes' : False,
                'description' : 'Request is not in the list.'})

        req = models.Request.objects.get(id=rid)
        user.requests.remove(req)
        user.save()

        return Response({'succes' : True})



# GET requests_info/{id}
class RequestViewSet(NestedViewSetMixin, viewsets.ModelViewSet):
    queryset = models.Request.objects.all()
    serializer_class = serializers.RequestSerializer

# GET requests/{id}
class FullRequestViewSet(NestedViewSetMixin, viewsets.ModelViewSet):
    queryset = models.FullRequest.objects.all()
    serializer_class = serializers.FullRequestSerializer

# GET achievements/{id}
class AchievementViewSet(viewsets.ModelViewSet):
    queryset = models.Achievement.objects.all()
    serializer_class = serializers.AchievementSerializer

# GET userservice/{id} (TODO: DELETE)
class UserServiceViewSet(NestedViewSetMixin, viewsets.ModelViewSet):
    queryset = models.UserService.objects.all()
    serializer_class = serializers.UserServiceSerializer

''' 
    ADMIN
=============
'''
# POST achievement_create/
class AchievementCreate(generics.ListCreateAPIView):
    serializer_class = serializers.AchievementSerializer

    def create(self, request):
        achievement = parsers.parse_achievement(request.data)
        achievement.save()
        icon = parsers.parse_picture(data=request.data['icon'],
                                     name='achievements/' + achievement.name)
        achievement.icon = icon
        achievement.save()
        serializer = serializers.AchievementSerializer(achievement)
        return Response(serializer.data)

# POST service_type_create/
class ServiceCreate(generics.ListCreateAPIView):
    serializer_class = serializers.ServiceSerializer

    def create(self, request):
        service = parsers.parse_service(request.data)
        service.save()
        serializer = serializers.ServiceSerializer(service)
        return Response(serializer.data)
