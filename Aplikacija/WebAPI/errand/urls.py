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
router.register(r'requests_other', api_views.RequestOtherViewSet)
router.register(r'achievements', api_views.AchievementViewSet)
router.register(r'services', api_views.ServiceViewSet)
router.register(r'filtered_users', api_views.FilterUserViewSet)
router.register(r'user_info_filtered', api_views.UserInfoFilteredViewSet)
router.register(r'filtered_requests', api_views.FilterRequestViewSet)
router.register(r'search_requests', api_views.SearchRequestViewSet)
router.register(r'request_info_filtered', api_views.RequestInfoFilteredViewSet)
router.register(r'reports', api_views.ReportViewSet)
router.register(r'notification', api_views.NotificationViewSet)
router.register(r'unseen_notifications', api_views.UnseenNotificationsViewSet)

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
         name='user_benefit_update'),
    path('api/v1/user_location_update/', api_views.UserLocationUpdate.as_view(),
         name='user_location_update'),
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
         name='block_remove'),
    path('api/v1/report_create/', api_views.ReportCreate.as_view(),
         name='report_create'),
    path('api/v1/report_handle/', api_views.ReportHandle.as_view(),
         name='report_handle'),
]
urlpatterns += urlblocked

urlrequest = [
    path('api/v1/request_create/', api_views.RequestCreate.as_view(),
         name='request_create'),
    path('api/v1/request_start/', api_views.RequestStart.as_view(),
         name='request_start'),
    path('api/v1/request_cancel/', api_views.RequestCancel.as_view(),
         name='request_cancel'),
    path('api/v1/request_finish/', api_views.RequestFinish.as_view(),
         name='request_finish'),
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
         name='edit_cancel'),
    path('api/v1/picture_upload/', api_views.PictureUpload.as_view(),
         name='picture_upload'),
    path('api/v1/picture_remove/', api_views.PictureRemove.as_view(),
         name='picture_remove'),
    path('api/v1/ban_create/', api_views.BanCreate.as_view(),
         name='ban_create'),
    path('api/v1/set_arrived/', api_views.SetArrived.as_view(),
         name='set_arrived'),
]
urlpatterns += urlrequest

urladmin = [
    path('api/v1/achievement_create/', api_views.AchievementCreate.as_view(),
         name='achievement_create'),
    path('api/v1/service_create/', api_views.ServiceCreate.as_view(),
         name='service_create'),
    path('api/v1/service_update/', api_views.ServiceUpdate.as_view(),
         name='service_update'),
    path('api/v1/service_task_update/', api_views.ServiceTaskUpdate.as_view(),
         name='service_task_update'),
    path('api/v1/stats/', api_views.Stats.as_view(),
         name='stats')
]
urlpatterns += urladmin

urlfcm = [
    path('api/v1/fcm_register/', api_views.FCMRegister.as_view(),
         name='fcm_register'),
    path('api/v1/fcm_unregister/', api_views.FCMUnregister.as_view(),
         name='fcm_unregister'),
    path('api/v1/fcm_test_notification/', api_views.FCMTestNotification.as_view(),
         name='fcm_test_notification'),
    path('api/v1/notification_flags_update/', api_views.NotificationFlagsUpdate.as_view(),
         name='notification_flags_update')
]
urlpatterns += urlfcm
