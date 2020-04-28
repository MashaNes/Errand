from rest_framework import viewsets, generics
from rest_framework.views import APIView
from rest_framework.response import Response

from rest_framework_extensions.mixins import NestedViewSetMixin
from . import models
from . import serializers
from . import parsers

'''
Endpoints:
+ POST user_create			// Creates user
+ GET users_info/{id}		// Returns only basic user data
+ GET users/{id} 			// Returns full user data
+ POST benefit_add          // Adds user to benefit list
- DELETE benefit_remove     // Removes user from benefit list

- POST requests_create		// Creates request
+ GET requests_info/{id}	// Returns only basic request data
- GET requests/{id}			// Returns full request data (including offers and rating)

? + GET userservice/{id}		// Returns full userservice data

- 
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

class BenefitRemove(generics.RetrieveDestroyAPIView):
    serializers_class = serializers.BenefitSerializer

    def delete(self, request):
        created_by = request.data['created_by']
        benefit_user = request.data['benefit_user']
        user = models.FullUser.objects.get(id=created_by)
        #ben_user = models.User.objects.get(id=benefit_user)
        benefit = user.benefitlist.get(id=31)

        user.benefitlist.remove(benefit)
        user.save()
        serializer = serializers.BenefitSerializer(benefit)
        return Response(serializer.data)

# REQUEST
class RequestViewSet(NestedViewSetMixin, viewsets.ModelViewSet):
    queryset = models.Request.objects.all()
    serializer_class = serializers.RequestSerializer

class FullRequestViewSet(NestedViewSetMixin, viewsets.ModelViewSet):
    queryset = models.FullRequest.objects.all()
    serializer_class = serializers.FullRequestSerializer

# SERVICE
class UserServiceViewSet(NestedViewSetMixin, viewsets.ModelViewSet):
    queryset = models.UserService.objects.all()
    serializer_class = serializers.UserServiceSerializer
