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
router.register(r'requests', api_views.FullRequestViewSet)
router.register(r'requests_info', api_views.RequestViewSet)
router.register(r'userservice', api_views.UserServiceViewSet)
router.register(r'achievements', api_views.AchievementViewSet)

urlpatterns = [
    path('admin/', admin.site.urls),
    path('api/v1/', include(router.urls)),

    path('api/v1/user_create/', api_views.UserCreate.as_view(), name='user_create'),
    path('api/v1/benefit_add/', api_views.BenefitAdd.as_view(), name='benefit_add'),
    path('api/v1/address_add/', api_views.AddressAdd.as_view(), name='address_add'),
    path('api/v1/working_hours_add/', api_views.WorkingHoursAdd.as_view(),
         name='working_hours_add'),
    path('api/v1/user_service_add/', api_views.UserServiceAdd.as_view(),
         name='user_service_add'),
    # path('api/v1/benefit_remove/', api_views.BenefitRemove.as_view(), name='benefit_remove'),
    path('api/v1/achievement_create/', api_views.AchievementCreate.as_view(),
         name='achievement_create'),
    path('api/v1/service_create/', api_views.ServiceCreate.as_view(),
         name='achievemservice_createent_create'),

    path('api/auth/', include('djoser.urls.authtoken')),
]
