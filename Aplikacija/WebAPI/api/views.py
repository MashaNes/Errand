from rest_framework import viewsets, generics
from rest_framework.views import APIView
from rest_framework.response import Response

from rest_framework_extensions.mixins import NestedViewSetMixin
from . import models
from . import serializers

'''
Endpoints:
- user (create, full, info, achievements, ratings) --> user_create, users_info, users
- requests (all, requested, history, running) --> requests
'''

class UserViewSet(NestedViewSetMixin, viewsets.ModelViewSet):
    queryset = models.User.objects.all()
    serializer_class = serializers.UserSerializer

class UserCreate(generics.CreateAPIView):
    authentication_classes = ()
    permission_classes = ()
    serializer_class = serializers.UserSerializer

class FullUserViewSet(NestedViewSetMixin, viewsets.ModelViewSet):
    queryset = models.FullUser.objects.all()
    serializer_class = serializers.FullUserSerializer

class RequestViewSet(NestedViewSetMixin, viewsets.ModelViewSet):
    queryset = models.Request.objects.all()
    serializer_class = serializers.RequestSerializer

class UserServiceViewSet(NestedViewSetMixin, viewsets.ModelViewSet):
    queryset = models.UserService.objects.all()
    serializer_class = serializers.UserServiceSerializer
    