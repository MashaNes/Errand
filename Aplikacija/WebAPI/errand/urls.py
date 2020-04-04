from django.contrib import admin
from django.urls import include, path
from rest_framework import routers
from rest_framework_extensions.routers import NestedRouterMixin
from api import views as api_views

class NestedDefaultRouter(NestedRouterMixin, routers.DefaultRouter):
    pass

router = NestedDefaultRouter()
router.register(r'users', api_views.FullUserViewSet)
router.register(r'users_info', api_views.UserViewSet)
router.register(r'requests', api_views.RequestViewSet)
router.register(r'userservice', api_views.UserServiceViewSet)

urlpatterns = [
    path('admin/', admin.site.urls),
    path('api/v1/', include(router.urls)),
    path('api/v1/user_create/', api_views.UserCreate.as_view(), name='user_create'),
    path('api/auth/', include('djoser.urls.authtoken')),
]
