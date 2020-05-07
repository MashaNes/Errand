from rest_framework import viewsets, generics
from rest_framework.views import APIView
from rest_framework.authtoken.models import Token
from rest_framework.authtoken.views import ObtainAuthToken
from rest_framework.response import Response
from rest_framework_extensions.mixins import NestedViewSetMixin
from django.shortcuts import get_object_or_404
from django.http import HttpResponse
from . import models
from . import serializers
from . import parsers
from . import utils

'''
Endpoints:
+ GET users_info/{id} 				// Returns only basic user data
+ GET users/{id} 					// Returns full user data
+ GET filtered_users/ 				// Returns users based on filters
+ GET user_info_filtered            // Returns users info on filters
+ GET get_cookie/    				// Returns only basic user data

+ GET requests_info/{id} 			// Returns only basic request data
+ GET requests/{id} 				// Returns full request data (including offers and rating)
- GET requests_unrated              // Returns unrated requests
- GET offers_unrated                // Returns unrated accepted offers
- GET filtered_requests/ 			// Returns users based on filters
+ GET achievements/{id}				// Returns list of all achievements
+ GET services/{id}                 // Returns list of all types of services
- GET stats/ (A)		 			// Returns statistics based on filters

+ POST login/                       // Login as some user
+ POST user_create/					// Creates new user
+ POST benefit_add/					// Adds new user to benefit list
+ POST address_add/					// Adds new address to addresses
+ POST working_hours_add/ 			// Adds new working hours for user
+ POST user_service_add/			// Adds new user service for user
+ POST block_add/			        // Adds new user to blocklist
- POST offer_create/ 				// Creates offer for request
+ POST request_create/ 				// Creates request
+ POST rate_user/ 					// Adds new rating for completed request
- POST report_user/					// Reports user
- POST ban_user/ (A)	 			// Bans user
~ POST achievement_create/ (A)	 	// Creates new achievement
~ POST service_type_create/ (A)		// Creates new service type

+ PUT logout/                       // Logout user
+ PUT user_update/			        // Updates basic user data
+ PUT benefit_update/			    // Updates benefit to benefit list
+ PUT address_update/			    // Updates address to addresses
+ PUT working_hours_update/ 		// Updates working hours for user
+ PUT user_service_update/		    // Updates user service for user

+ DELETE benefit_remove/ 			// Removes user from benefit list
+ DELETE address_remove/ 			// Removes address from user
+ DELETE working_hours_remove/ 		// Removes working hours from user
+ DELETE user_service_remove/ 		// Removes user service from user
+ DELETE block_remove/ 			    // Removes user from blocklist
+ DELETE request_cancel/ 			// Cancels request (when pending)
- DELETE offer_cancel/ 			    // Cancels offer (when not accepted)
'''


# =================== USER ===================
# ============================================
# POST login/
class LogIn(ObtainAuthToken):
    def post(self, request, *args, **kwargs):
        serializer = self.serializer_class(data=request.data,
                                           context={'request':request})
        serializer.is_valid(raise_exception=True)
        user = serializer.validated_data['user']
        user.status = 1
        user.save()
        _s = serializers.UserSerializer(user)
        _s = _s.data
        _s['picture'] = utils.encode_img('users/' + str(user.id))
        token, _ = Token.objects.get_or_create(user=user)
        custom_response = {
            'token': token.key,
            'user': _s
        }
        return Response(custom_response)

# GET get_cookie/
class GetCookieViewSet(NestedViewSetMixin, viewsets.ModelViewSet):
    queryset = models.User.objects.all()
    authentication_classes = ()
    permission_classes = ()
    def list(self, request):
        user = get_object_or_404(self.queryset, pk=request.data['created_by'])
        _s = serializers.UserSerializer(user)
        _s = _s.data
        _s['picture'] = utils.encode_img('users/' + str(user.id))
        token, _ = Token.objects.get_or_create(user=user)
        response = HttpResponse()
        response.set_cookie('token', token)
        response.set_cookie('id', _s['id'])
        response.set_cookie('email', _s['email'])
        response.set_cookie('first_name', _s['first_name'])
        response.set_cookie('last_name', _s['last_name'])
        response.set_cookie('phone', _s['phone'])
        response.set_cookie('picture', _s['picture'])
        response.set_cookie('avg_rating', _s['avg_rating'])
        response.set_cookie('min_rating', _s['min_rating'])
        response.set_cookie('max_dist', _s['max_dist'])
        response.set_cookie('is_admin', _s['is_admin'])
        response.set_cookie('status', _s['status'])
        response.set_cookie('benefit_discount', _s['benefit_discount'])
        response.set_cookie('benefit_requirement', _s['benefit_requirement'])

        return response

# POST logout/
class LogOut(generics.UpdateAPIView):
    def update(self, request):
        created_by = request.data['created_by']
        user = models.User.objects.get(id=created_by)
        if user.status == 0:
            response = {'success' : 'Not logged in.'}
        else:
            user.auth_token.delete()
            user.status = 0
            user.save()
            response = {'success' : 'Logged out.'}
        return Response(response)

# POST user_create/
class UserCreate(generics.ListCreateAPIView):
    authentication_classes = ()
    permission_classes = ()
    serializer_class = serializers.UserSerializer

    def create(self, request):
        user = parsers.create_user(request.data)
        Token.objects.create(user=user)
        token, _ = Token.objects.get_or_create(user=user)
        _s = serializers.UserSerializer(user)
        _s = _s.data
        _s['picture'] = utils.encode_img('users/' + str(user.id))
        custom_response = {
            'token': token.key,
            'user': _s
        }
        return Response(custom_response)

# PUT user_update/
class UserUpdate(generics.UpdateAPIView):
    def update(self, request):
        created_by = request.data['created_by']
        user = models.User.objects.get(id=created_by)
        user = parsers.update_user(user, request.data)
        serializer = serializers.UserSerializer(user)
        response = serializer.data
        response['picture'] = utils.encode_img('users/' + str(user.id))

        return Response(response)

# GET users_info/{id}
class UserViewSet(NestedViewSetMixin, viewsets.ModelViewSet):
    queryset = models.User.objects.all()

    def list(self, request):
        new_q = []
        for _q in self.queryset:
            if not _q.is_admin:
                new_q.append(_q)
        self.queryset = new_q

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
        if user.is_admin:
            return Response({'success' : False,
                             'description' : 'Cannot return admin user.'})
        serializer = serializers.UserSerializer(user)
        response = serializer.data
        response['picture'] = utils.encode_img('users/' + str(user.id))

        return Response(response)

# GET users/{id}
class FullUserViewSet(NestedViewSetMixin, viewsets.ModelViewSet):
    queryset = models.FullUser.objects.all()

    def list(self, request):
        new_q = []
        for _q in self.queryset:
            if not _q.user.is_admin:
                new_q.append(_q)
        self.queryset = new_q

        serializer = serializers.FullUserSerializer(self.queryset, many=True)
        for _q, _s in zip(self.queryset, serializer.data):
            _s['user']['picture'] = utils.encode_img('users/' + str(_q.user.id))

        # TODO: Add pagination
        custom_response = {
            'count' : len(serializer.data),
            'next' : None,
            'previous' : None,
            'results' : serializer.data
        }

        return Response(custom_response)

    def retrieve(self, request, pk):
        fulluser = get_object_or_404(self.queryset, pk=pk)
        if fulluser.user.is_admin:
            return Response({'success' : False,
                             'description' : 'Cannot return admin user.'})
        serializer = serializers.FullUserSerializer(fulluser)
        serializer.data['user']['picture'] = utils.encode_img('users/' + str(fulluser.user.id))

        return Response(serializer.data)

# GET users_info_filtered/id
class UserInfoFilteredViewSet(NestedViewSetMixin, viewsets.ModelViewSet):
    queryset = models.FullUser.objects.all()

    def list(self, request):
        fulluser = get_object_or_404(self.queryset, pk=request.data['created_by'])
        serializer = serializers.FullUserSerializer(fulluser)
        response = utils.filter_user_info(serializer.data, request.data)

        return Response(response)

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
            'previous' : None,
            'results' : serializer.data
        }

        return Response(custom_response)


# ================= BENEFIT =================
# POST benefit_add/
class BenefitAdd(generics.ListCreateAPIView):
    def create(self, request):
        created_by = request.data['created_by']
        user = models.FullUser.objects.get(id=created_by)

        for _b in user.benefitlist.all():
            if _b.benefit_user.id == request.data['benefit_user']:
                return Response({
                    'success' : False,
                    'description' : 'User is in the list.'})

        benefit = parsers.create_benefit(request.data)
        benefit.save()
        user.benefitlist.add(benefit)
        user.save()
        serializer = serializers.BenefitSerializer(benefit)
        serializer.data['benefit_user']['picture'] = utils.encode_img('users/' + \
                                                        str(benefit.benefit_user.id))
        return Response(serializer.data)

# PUT benefit_update/
class BenefitUpdate(generics.UpdateAPIView):
    def update(self, request):
        created_by = request.data['created_by']
        user = models.FullUser.objects.get(id=created_by)

        response = {'success' : False, 'description' : 'User is not in the list.'}
        for _b in user.benefitlist.all():
            if _b.id == request.data['benefit']:
                _b = parsers.update_benefit(_b, request.data)
                serializer = serializers.BenefitSerializer(_b)
                serializer.data['benefit_user']['picture'] = utils.encode_img('users/' + \
                                                        str(_b.benefit_user.id))

                response = serializer.data
                break

        return Response(response)

# DELETE benefit_remove/
class BenefitRemove(generics.RetrieveDestroyAPIView):
    def destroy(self, request):
        created_by = request.data['created_by']
        benefit_user = request.data['benefit_user']
        user = models.FullUser.objects.get(id=created_by)
        ben_user = models.User.objects.get(id=benefit_user)

        benefits = user.benefitlist.all()
        response = {'success' : False, 'description' : 'User is not in the list.'}
        for _b in benefits:
            if _b.benefit_user.id == ben_user.id:
                user.benefitlist.remove(_b)
                user.save()
                response = {'success' : True}
                break

        return Response(response)


# ================= ADDRESS =================
# POST address_add/
class AddressAdd(generics.ListCreateAPIView):
    def create(self, request):
        address = parsers.create_address(request.data)
        created_by = request.data['created_by']
        user = models.FullUser.objects.get(id=created_by)
        user.addresses.add(address)
        user.save()
        serializer = serializers.AddressSerializer(address)
        return Response(serializer.data)

# PUT address_update/
class AddressUpdate(generics.UpdateAPIView):
    def update(self, request):
        created_by = request.data['created_by']
        user = models.FullUser.objects.get(id=created_by)
        aid = request.data['address']

        response = {'success' : False,
                    'description' : 'Address is not in the list.'}

        for _a in user.addresses.all():
            if _a.id == aid:
                _a = parsers.update_address(_a, request.data)
                serializer = serializers.AddressSerializer(_a)
                response = serializer.data
                break

        return Response(response)

# DELETE address_remove/
class AddressRemove(generics.RetrieveDestroyAPIView):
    def destroy(self, request):
        created_by = request.data['created_by']
        user = models.FullUser.objects.get(id=created_by)
        aid = request.data['address']

        found = False
        for _a in user.addresses.all():
            if _a.id == aid:
                found = True
                break

        if not found:
            return Response({
                'success' : False,
                'description' : 'Address is not in the list.'})

        address = models.Address.objects.get(id=aid)
        user.addresses.remove(address)
        user.save()

        return Response({'success' : True})


# ======================= WORKING HOURS =======================
# POST working_hours_add/
class WorkingHoursAdd(generics.ListCreateAPIView):
    def create(self, request):
        working_hours = parsers.create_working_hour(request.data)
        working_hours.save()
        created_by = request.data['created_by']
        user = models.FullUser.objects.get(id=created_by)
        user.working_hours.add(working_hours)
        user.save()
        serializer = serializers.WorkingHourSerializer(working_hours)
        return Response(serializer.data)

# PUT working_hours_update/
class WorkingHoursUpdate(generics.UpdateAPIView):
    def update(self, request):
        created_by = request.data['created_by']
        user = models.FullUser.objects.get(id=created_by)
        wid = request.data['working_hours']

        response = {'success' : False,
                    'description' : 'Working hours are not in the list.'}

        for _w in user.working_hours.all():
            if _w.id == wid:
                _w = parsers.update_working_hour(_w, request.data)
                serializer = serializers.WorkingHourSerializer(_w)
                response = serializer.data
                break

        return Response(response)

# DELETE working_hours_remove/
class WokringHoursRemove(generics.RetrieveDestroyAPIView):
    def destroy(self, request):
        created_by = request.data['created_by']
        user = models.FullUser.objects.get(id=created_by)
        wid = request.data['working_hours']

        found = False
        for _w in user.working_hours.all():
            if _w.id == wid:
                found = True
                break

        if not found:
            return Response({
                'succes' : False,
                'description' : 'Working hours are not in the list.'})

        _working_hours = models.WorkingHour.objects.get(id=wid)
        user.working_hours.remove(_working_hours)
        user.save()

        return Response({'success' : True})


# ====================== USER SERVICE ======================
# POST user_service_add/
class UserServiceAdd(generics.ListCreateAPIView):
    def create(self, request):
        created_by = request.data['created_by']
        user_service = parsers.create_user_service(request.data)
        user = models.FullUser.objects.get(id=created_by)

        for _s in user.services.all():
            if _s.service.id == request.data['service']:
                return Response({
                    'success' : False,
                    'description' : 'Service is in the list.'})

        user.services.add(user_service)
        user.save()
        serializer = serializers.UserServiceSerializer(user_service)
        return Response(serializer.data)

# PUT user_service_update/
class UserServiceUpdate(generics.UpdateAPIView):
    def update(self, request):
        created_by = request.data['created_by']
        user = models.FullUser.objects.get(id=created_by)
        sid = request.data['user_service']

        response = {'success' : False,
                    'description' : 'User service is not in the list.'}

        for _s in user.services.all():
            if _s.id == sid:
                _s = parsers.update_user_service(_s, request.data)
                serializer = serializers.UserServiceSerializer(_s)
                response = serializer.data
                break

        return Response(response)

# DELETE user_services_remove/
class UserServiceRemove(generics.RetrieveDestroyAPIView):
    def destroy(self, request):
        created_by = request.data['created_by']
        user = models.FullUser.objects.get(id=created_by)
        sid = request.data['service']

        found = False
        for _s in user.services.all():
            if _s.id == sid:
                found = True
                break

        if not found:
            return Response({
                'success' : False,
                'description' : 'Service is not in the list.'})

        service = models.UserService.objects.get(id=sid)
        user.services.remove(service)
        user.save()

        return Response({'success' : True})


# ================= BENEFIT =================
# POST block_add/
class BlockAdd(generics.ListCreateAPIView):
    def create(self, request):
        created_by = request.data['created_by']
        user = models.FullUser.objects.get(id=created_by)
        blocked = models.User.objects.get(id=request.data['blocked'])

        for _b in user.blocked.all():
            if _b.id == blocked.id:
                return Response({
                    'success' : False,
                    'description' : 'User is blocked.'})

        user.blocked.add(blocked)
        user.save()
        serializer = serializers.FullUserSerializer(user)
        return Response(serializer.data['blocked'])


# DELETE block_remove/
class BlockRemove(generics.RetrieveDestroyAPIView):
    def destroy(self, request):
        created_by = request.data['created_by']
        user = models.FullUser.objects.get(id=created_by)

        response = {'success' : False,
                    'description' : 'User is not in the list.'}
        for _b in user.blocked.all():
            if _b.id == request.data['blocked']:
                user.blocked.remove(_b)
                user.save()
                response = {'success' : True}
                break

        return Response(response)


# =============== RATE USER ===============
# POST rate_user/
class RateUser(generics.ListCreateAPIView):
    def create(self, request):
        rating = parsers.create_rating(request.data)
        rating.save()
        created_by = request.data['rated_user']
        user = models.FullUser.objects.get(id=created_by)
        user.ratings.add(rating)
        user.save()
        serializer = serializers.RatingSerializer(rating)
        return Response(serializer.data)


# ==================== REQUEST ====================
# =================================================
# POST request_create/
class RequestCreate(generics.ListCreateAPIView):
    def create(self, request):
        req = parsers.create_request(request.data)
        created_by = request.data['created_by']
        user = models.FullUser.objects.get(id=created_by)
        user.requests.add(req)
        user.save()
        serializer = serializers.RequestSerializer(req)
        return Response(serializer.data)

# DELETE request/
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

# GET services/{id}
class ServiceViewSet(viewsets.ModelViewSet):
    queryset = models.Service.objects.all()
    serializer_class = serializers.ServiceSerializer


# ======================== ADMIN ========================
# =======================================================
# POST achievement_create/
class AchievementCreate(generics.ListCreateAPIView):
    def create(self, request):
        created_by = request.data['created_by']
        user = models.User.objects.get(id=created_by)

        if user.is_admin:
            achievement = parsers.create_achievement(request.data)
            serializer = serializers.AchievementSerializer(achievement)
            response = serializer.data
            response['icon'] = utils.encode_img('achievements/' + str(achievement.id))
            return Response(response)
        else:
            return Response({"success" : False,
                             "description" : "User is not admin."})

# POST service_type_create/
class ServiceCreate(generics.ListCreateAPIView):
    def create(self, request):
        created_by = request.data['created_by']
        user = models.User.objects.get(id=created_by)

        if user.is_admin:
            service = parsers.create_service(request.data)
            serializer = serializers.ServiceSerializer(service)
            return Response(serializer.data)
        else:
            return Response({"success" : False,
                             "description" : "User is not admin."})
