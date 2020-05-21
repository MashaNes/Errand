from django.contrib import admin
from django.urls import include, path
from rest_framework import routers
from api import views as api_views

router = routers.DefaultRouter()
router.register(r'get_cookie', api_views.GetCookieViewSet)
router.register(r'users', api_views.FullUserViewSet)
router.register(r'users_info', api_views.UserViewSet)
router.register(r'requests', api_views.FullRequestViewSet)
router.register(r'requests_info', api_views.RequestViewSet)
router.register(r'achievements', api_views.AchievementViewSet)
router.register(r'services', api_views.ServiceViewSet)
router.register(r'filtered_users', api_views.FilterUserViewSet)
router.register(r'user_info_filtered', api_views.UserInfoFilteredViewSet)
router.register(r'filtered_requests', api_views.FilterRequestViewSet)
router.register(r'request_info_filtered', api_views.RequestInfoFilteredViewSet)

urlpatterns = [
    path('admin/', admin.site.urls),
    path('api/v1/', include(router.urls))
]

urluser = [
    path('api/v1/login/', api_views.LogIn.as_view(),
         name='login'),
    path('api/v1/logout/', api_views.LogOut.as_view(),
         name='logout'),
    path('api/v1/user_create/', api_views.UserCreate.as_view(),
         name='user_create'),
    path('api/v1/user_update/', api_views.UserUpdate.as_view(),
         name='user_update'),
    path('api/v1/user_status_update/', api_views.UserStatusUpdate.as_view(),
         name='user_status_update'),
    path('api/v1/user_benefit_update/', api_views.UserBenefitUpdate.as_view(),
         name='user_benefit_update')
]
urlpatterns += urluser

urlbenefit = [
    path('api/v1/benefit_add/', api_views.BenefitAdd.as_view(),
         name='benefit_add'),
    path('api/v1/benefit_update/', api_views.BenefitUpdate.as_view(),
         name='benefit_update'),
    path('api/v1/benefit_remove/', api_views.BenefitRemove.as_view(),
         name='benefit_remove')
]
urlpatterns += urlbenefit

urladdress = [
    path('api/v1/address_add/', api_views.AddressAdd.as_view(),
         name='address_add'),
    path('api/v1/address_update/', api_views.AddressUpdate.as_view(),
         name='address_update'),
    path('api/v1/address_remove/', api_views.AddressRemove.as_view(),
         name='address_remove')
]
urlpatterns += urladdress

urlworkinghours = [
    path('api/v1/working_hours_add/', api_views.WorkingHoursAdd.as_view(),
         name='working_hours_add'),
    path('api/v1/working_hours_update/', api_views.WorkingHoursUpdate.as_view(),
         name='working_hours_update'),
    path('api/v1/working_hours_remove/', api_views.WokringHoursRemove.as_view(),
         name='working_hours_remove')
]
urlpatterns += urlworkinghours

urluserservice = [
    path('api/v1/user_service_add/', api_views.UserServiceAdd.as_view(),
         name='user_service_add'),
    path('api/v1/user_service_update/', api_views.UserServiceUpdate.as_view(),
         name='user_service_update'),
    path('api/v1/user_service_remove/', api_views.UserServiceRemove.as_view(),
         name='user_service_remove')
]
urlpatterns += urluserservice

urlblocked = [
    path('api/v1/block_add/', api_views.BlockAdd.as_view(),
         name='block_add'),
    path('api/v1/block_remove/', api_views.BlockRemove.as_view(),
         name='block_remove')
]
urlpatterns += urlblocked

urlrequest = [
    path('api/v1/request_create/', api_views.RequestCreate.as_view(),
         name='request_create'),
    path('api/v1/request_cancel/', api_views.RequestCancel.as_view(),
         name='request_cancel'),
    path('api/v1/rate_user/', api_views.RateUser.as_view(),
         name='rate_user'),
    path('api/v1/offer_create/', api_views.OfferCreate.as_view(),
         name='offer_create'),
    path('api/v1/offer_accept/', api_views.OfferAccept.as_view(),
         name='offer_accept'),
    path('api/v1/offer_cancel/', api_views.OfferCancel.as_view(),
         name='offer_cancel'),
    path('api/v1/edit_create/', api_views.EditCreate.as_view(),
         name='edit_create'),
    path('api/v1/edit_accept/', api_views.EditAccept.as_view(),
         name='edit_accept'),
    path('api/v1/edit_cancel/', api_views.EditCancel.as_view(),
         name='edit_cancel')
]
urlpatterns += urlrequest

urladmin = [
    path('api/v1/achievement_create/', api_views.AchievementCreate.as_view(),
         name='achievement_create'),
    path('api/v1/service_create/', api_views.ServiceCreate.as_view(),
         name='service_create')
]
urlpatterns += urladmin
