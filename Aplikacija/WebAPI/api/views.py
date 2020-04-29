from rest_framework import viewsets, generics
from rest_framework.views import APIView
from rest_framework.response import Response

from rest_framework_extensions.mixins import NestedViewSetMixin
from . import models
from . import serializers
from . import parsers

'''
Endpoints:
+ GET users_info/{id} 				// Returns only basic user data
+ GET users/{id} 					// Returns full user data
- GET filtered_users/ 				// Returns users based on filters
+ GET requests_info/{id} 			// Returns only basic request data
- GET requests/{id} 				// Returns full request data (including offers and rating)
+ GET achievements/{id}				// Returns list of all achievements
- GET stats/ (A)		 			// Returns statistics based on filters

+ POST user_create/					// Creates new user
+ POST benefit_add/					// Adds new user to benefit list
+ POST address_add/					// Adds new address to addresses
+ POST working_hours_add/ 			// Adds new working hours for user
+ POST userservice_add/				// Adds new user service for user
- POST offer_create/ 				// Creates offer for request
- POST request_create/ 				// Creates request
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

# USER
class UserCreate(generics.CreateAPIView):
    authentication_classes = ()
    permission_classes = ()
    serializer_class = serializers.UserSerializer

class UserViewSet(NestedViewSetMixin, viewsets.ModelViewSet):
    queryset = models.User.objects.all()
    serializer_class = serializers.UserSerializer

class FullUserViewSet(NestedViewSetMixin, viewsets.ModelViewSet):
    queryset = models.FullUser.objects.all()
    serializer_class = serializers.FullUserSerializer

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

class AddressAdd(generics.ListCreateAPIView):
    serializer_class = serializers.AddressSerializer

    def create(self, request):
        address = parsers.parse_address(request.data)
        address.save()
        created_by = request.data['created_by']
        user = models.FullUser.objects.get(id=created_by)
        user.addresses.add(address)
        user.save()
        serializer = serializers.AddressSerializer(address)
        return Response(serializer.data)

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

# REQUEST
class RequestViewSet(NestedViewSetMixin, viewsets.ModelViewSet):
    queryset = models.Request.objects.all()
    serializer_class = serializers.RequestSerializer

class FullRequestViewSet(NestedViewSetMixin, viewsets.ModelViewSet):
    queryset = models.FullRequest.objects.all()
    serializer_class = serializers.FullRequestSerializer

### ADMIN ###

# ACHIEVEMENTS
class AchievementViewSet(NestedViewSetMixin, viewsets.ModelViewSet):
    queryset = models.Achievement.objects.all()
    serializer_class = serializers.AchievementSerializer

class AchievementCreate(generics.ListCreateAPIView):
    serializer_class = serializers.AchievementSerializer

    def create(self, request):
        achievement = parsers.parse_achievement(request.data)
        achievement.save()
        serializer = serializers.AchievementSerializer(achievement)
        return Response(serializer.data)

# SERVICES
class UserServiceViewSet(NestedViewSetMixin, viewsets.ModelViewSet):
    queryset = models.UserService.objects.all()
    serializer_class = serializers.UserServiceSerializer

class ServiceCreate(generics.ListCreateAPIView):
    serializer_class = serializers.ServiceSerializer

    def create(self, request):
        service = parsers.parse_service(request.data)
        service.save()
        serializer = serializers.ServiceSerializer(service)
        return Response(serializer.data)
