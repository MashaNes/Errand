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
- GET filtered_users/ 				// Returns users based on filters
+ GET requests_info/{id} 			// Returns only basic request data
- GET requests/{id} 				// Returns full request data (including offers and rating)
+ GET achievements/{id}				// Returns list of all achievements
- GET stats/ (A)		 			// Returns statistics based on filters

+ POST login/                       // Login as some user
+ POST user_create/					// Creates new user
+ POST benefit_add/					// Adds new user to benefit list
+ POST address_add/					// Adds new address to addresses
+ POST working_hours_add/ 			// Adds new working hours for user
+ POST userservice_add/				// Adds new user service for user
- POST offer_create/ 				// Creates offer for request
+ POST request_create/ 				// Creates request
- POST rate_user/ 					// Adds new rating for completed request
- POST report_user/					// Reports user
- POST ban_user/ (A)	 			// Bans user
~ POST achievement_create/ (A)	 	// Creates new achievement
~ POST service_type_create/ (A)		// Creates new service type

- UPDATE request_update/ 			// Edits request
- UPDATE users_info_update/			// Updates basic user data
- UPDATE user_update/ 				// Updates full user data

- DELETE request_remove/ 			// Cancels request
- DELETE benefit_remove/ 			// Removes user from benefit list
- DELETE address_remove/ 			// Removes address from user
- DELETE working_hours_remove/ 		// Removes working hours from user
- DELETE userservice_remove/ 		// Removes user service from user

(DEL)
+ GET userservice/{id}				// Returns full userservice data
'''

# POST login/
class TokenObtainView(ObtainAuthToken):
    def post(self, request, *args, **kwargs):
        serializer = self.serializer_class(data=request.data,
                                           context={'request':request})
        serializer.is_valid(raise_exception=True)
        user = serializer.validated_data['user']
        fulluser = models.FullUser.objects.get(id=user.id)
        s = serializers.FullUserSerializer(fulluser)
        token, created = Token.objects.get_or_create(user=user)
        custom_response = {
            'token': token.key,
            'user': s.data
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
        extrauser = models.FullUser(user=user)
        extrauser.save()
        serializer = serializers.UserSerializer(user)
        return Response(serializer.data)

# GET users_info/{id}
class UserViewSet(NestedViewSetMixin, viewsets.ModelViewSet):
    queryset = models.User.objects.all()
    serializer_class = serializers.UserSerializer

# GET users/{id}
class FullUserViewSet(NestedViewSetMixin, viewsets.ModelViewSet):
    queryset = models.FullUser.objects.all()
    serializer_class = serializers.FullUserSerializer

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

# POST benefit_add/
class BenefitAdd(generics.ListCreateAPIView):
    serializer_class = serializers.BenefitSerializer

    def create(self, request):
        benefit = parsers.parse_benefit(request.data)
        benefit.save()
        created_by = request.data['created_by']
        user = models.FullUser.objects.get(id=created_by)
        user.benefitlist.add(benefit)
        user.save()
        serializer = serializers.BenefitSerializer(benefit)
        return Response(serializer.data)

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

# POST userservice_add/
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

# class BenefitRemove(generics.RetrieveDestroyAPIView):
#     serializer_class = serializers.BenefitSerializer

#     def delete(self, request):
#         created_by = request.data['created_by']
#         benefit_user = request.data['benefit_user']
#         user = models.FullUser.objects.get(id=created_by)
#         #ben_user = models.User.objects.get(id=benefit_user)
#         benefit = user.benefitlist.get(id=31)

#         user.benefitlist.remove(benefit)
#         user.save()
#         serializer = serializers.BenefitSerializer(benefit)
#         return Response(serializer.data)

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
