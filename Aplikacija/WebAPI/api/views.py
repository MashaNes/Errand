from datetime import datetime

from rest_framework import viewsets, generics
from rest_framework.authtoken.models import Token
from rest_framework.authtoken.views import ObtainAuthToken
from rest_framework.response import Response

from django.shortcuts import get_object_or_404
from django.http import HttpResponse
from django.utils import timezone
from fcm_django.models import FCMDevice

from . import models
from . import serializers
from . import parsers
from . import utils

'''
Endpoints:
__GET_________________________________________________________________________________________
+ GET users_info/{id} 				    // Returns only basic user data
+ GET users/{id} 					    // Returns full user data
+ POST filtered_users/ 				    // Returns users based on filters
+ POST user_info_filtered               // Returns users info based on filters
+ GET get_cookie/{id}    			    // Returns only basic user data

+ GET requests_info/{id} 			    // Returns only basic request data
+ GET requests/{id} 				    // Returns full request data (including offers and rating)
+ POST filtered_requests/ 			    // Returns requests based on filters
+ POST requests_info_filtered/          // Returns requests info based on filters
+ POST requests_search/                 // Returns avaliable requests based on your filters

+ GET achievements/{id}				    // Returns list of all achievements
+ GET services/{id}                     // Returns list of all types of services
+ GET stats/ (A)		 			    // Returns statistics based on filters
+ GET requests_other/ (A)               // Returns requests with at least 1 task of type 0 (other)
+ GET notification/{id}                 // Returns notification with id
+ GET unseen_notifications/{user_id}    // Returns number of unseen notifications
+ POST reports/ (A)                     // Returns filtered reports

__POST____________________________________________________________________________________________
+ POST login/                           // Login as some user
+ POST user_create/					    // Creates new user
+ POST benefit_add/					    // Adds new user to benefit list
+ POST address_add/					    // Adds new address to addresses
+ POST working_hours_add/ 			    // Adds new working hours for user
+ POST user_service_add/			    // Adds new user service for user
+ POST block_add/			            // Adds new user to blocklist
+ POST offer_create/ 				    // Creates offer for request
+ POST edit_create/                     // Creates edit request
+ POST request_create/ 				    // Creates request
+ POST picture_upload/                  // Uploads picture to request or task
+ POST rate_user/ 					    // Adds new rating for completed request
+ POST report_create/				    // Reports user
+ POST ban_create/ (A)	 			    // Bans reported user
+ POST achievement_create/ (A)	 	    // Creates new achievement
+ POST service_create/ (A)		        // Creates new service type
+ POST fcm_register/                    // Registers FCM device

__PUT_____________________________________________________________________________________________
+ PUT logout/                           // Logout user
+ PUT user_update/			            // Updates basic user data
+ PUT user_benefit_update/              // Updates user's benefit settings
+ PUT user_status_update/               // Updates user's status
+ PUT user_location_update/             // Updates user's location
+ PUT benefit_update/			        // Updates benefit to benefit list
+ PUT address_update/			        // Updates address to addresses
+ PUT working_hours_update/ 		    // Updates working hours for user
+ PUT user_service_update/		        // Updates user service for user
+ PUT service_type_update/ (A)          // Updates service type of given task
+ PUT service_update/ (A)               // Updates service
+ PUT report_handle/ (A)                // Handles reported user
+ PUT offer_accept/ 				    // Accepts offer for request
+ PUT edit_accept/                      // Accpets edit request
+ PUT request_finish/                   // Finishes request
+ PUT notification_flags_update/        // Updates notification flags (seen, opened)
+ PUT request_start/                    // Starts request
+ PUT set_arrived/  			        // Set arrived flag to address

__DELETE__________________________________________________________________________________________
+ DELETE benefit_remove/ 			    // Removes user from benefit list
+ DELETE address_remove/ 			    // Removes address from user
+ DELETE working_hours_remove/ 		    // Removes working hours from user
+ DELETE user_service_remove/ 		    // Removes user service from user
+ DELETE block_remove/ 			        // Removes user from blocklist
+ DELETE request_cancel/ 			    // Cancels request (when pending)
+ DELETE offer_cancel/ 			        // Cancels offer (when not accepted)
+ DELETE edit_cancel/                   // Cancels edit request
+ DELETE picture_remove/                // Removes picture
+ DELETE fcm_unregister/                // Removes FCM device
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
        fulluser = models.FullUser.objects.get(user=user)
        if fulluser.ban:
            if fulluser.ban.until > datetime.now(timezone.utc):
                custom_response = {
                    'detail' : 'Your account has been banned.',
                    'datetime' : fulluser.ban.until
                }
                return Response(custom_response)

        _s = serializers.UserSerializer(user)
        _s = _s.data
        _s['picture'] = utils.load_img(_s['picture'])
        token, _ = Token.objects.get_or_create(user=user)
        custom_response = {
            'token': token.key,
            'user': _s
        }
        return Response(custom_response)

# GET get_cookie/{id}
class GetCookieViewSet(viewsets.ModelViewSet):
    queryset = models.User.objects.all()

    def retrieve(self, request, pk):
        user = get_object_or_404(self.queryset, pk=pk)
        _s = serializers.UserSerializer(user)
        _s = _s.data
        _s['picture'] = utils.load_img(_s['picture'])
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
        token, _ = Token.objects.get_or_create(user=user)
        if token:
            user.auth_token.delete()
            user.status = 0
            user.save()
            response = {'detail' : 'Logged out.'}
        else:
            response = {'detail' : 'Not logged in.'}
        return Response(response)

# POST user_create/
class UserCreate(generics.ListCreateAPIView):
    authentication_classes = ()
    permission_classes = ()
    serializer_class = serializers.UserSerializer

    def create(self, request):
        user = parsers.create_user(request.data)
        if user:
            Token.objects.create(user=user)
            token, _ = Token.objects.get_or_create(user=user)
            _s = serializers.UserSerializer(user)
            _s = _s.data
            _s['picture'] = utils.load_img(_s['picture'])
            custom_response = {
                'token': token.key,
                'user': _s
            }
            return Response(custom_response)
        return Response({'detail' : 'failed'})

# PUT user_update/
class UserUpdate(generics.UpdateAPIView):
    def update(self, request):
        created_by = request.data['created_by']
        user = models.User.objects.get(id=created_by)
        user = parsers.update_user(user, request.data)
        serializer = serializers.UserSerializer(user)
        response = serializer.data
        response['picture'] = utils.load_img(response['picture'])

        return Response(response)

# PUT user_status_update/
class UserStatusUpdate(generics.UpdateAPIView):
    def update(self, request):
        created_by = request.data['created_by']
        user = models.User.objects.get(id=created_by)
        user.status = request.data['status']
        user.save()

        return Response({'detail' : 'success'})

# PUT user_location_update/
class UserLocationUpdate(generics.UpdateAPIView):
    def update(self, request):
        created_by = request.data['created_by']
        user = models.User.objects.get(id=created_by)

        if user.location:
            user.location.longitude = request.data['longitude']
            user.location.latitude = request.data['latitude']
            user.location.save()
        else:
            loc = models.Location(longitude=request.data['longitude'],
                                  latitude=request.data['latitude'])
            loc.save()
            user.location = loc
        user.save()

        return Response({'detail' : 'success'})

# PUT user_benefit_update/
class UserBenefitUpdate(generics.UpdateAPIView):
    def update(self, request):
        created_by = request.data['created_by']
        user = models.User.objects.get(id=created_by)
        user.benefit_discount = request.data['benefit_discount']
        user.benefit_requirement = request.data['benefit_requirement']
        user.save()

        return Response({'detail' : 'success'})

# GET users_info/{id}
class UserViewSet(viewsets.ModelViewSet):
    queryset = models.User.objects.all()

    def list(self, request):
        self.queryset = models.User.objects.all()
        new_q = []
        for _q in self.queryset:
            if not _q.is_admin:
                new_q.append(_q)
        self.queryset = new_q

        if request.GET.get('paginate'):
            page = self.paginate_queryset(self.queryset)
        else:
            page = self.queryset

        serializer = serializers.UserSerializer(page, many=True)
        for _s in serializer.data:
            _s['picture'] = utils.load_img(_s['picture'])

        if request.GET.get('paginate'):
            return self.get_paginated_response(serializer.data)

        custom_response = {
            'count' : len(serializer.data),
            'results' : serializer.data
        }
        return Response(custom_response)

    def retrieve(self, request, pk):
        user = get_object_or_404(self.queryset, pk=pk)
        serializer = serializers.UserSerializer(user)
        response = serializer.data
        response['picture'] = utils.load_img(response['picture'])

        return Response(response)

# GET users/{id}
class FullUserViewSet(viewsets.ModelViewSet):
    queryset = models.FullUser.objects.all()

    def list(self, request):
        self.queryset = models.FullUser.objects.all()
        new_q = []
        for _q in self.queryset:
            if not _q.user.is_admin:
                new_q.append(_q)
        self.queryset = new_q

        if request.GET.get('paginate'):
            page = self.paginate_queryset(self.queryset)
        else:
            page = self.queryset

        serializer = serializers.FullUserSerializer(page, many=True)

        _r = serializer.data
        _r = utils.load_pictures_multiple_users(_r)

        if request.GET.get('paginate'):
            return self.get_paginated_response(_r)

        custom_response = {
            'count' : len(_r),
            'results' : _r
        }

        return Response(custom_response)

    def retrieve(self, request, pk):
        fulluser = get_object_or_404(self.queryset, pk=pk)
        if fulluser.user.is_admin:
            return Response({'detail' : 'Cannot return admin user.'})
        serializer = serializers.FullUserSerializer(fulluser)
        _r = serializer.data
        _r = utils.load_pictures_user(_r)

        return Response(_r)

# POST user_info_filtered/id
class UserInfoFilteredViewSet(viewsets.ModelViewSet):
    queryset = models.FullUser.objects.all()

    def create(self, request):
        self.queryset = models.FullUser.objects.all()
        fulluser = get_object_or_404(self.queryset, pk=request.data['created_by'])
        serializer = serializers.FullUserSerializer(fulluser)
        response = utils.filter_user_info(serializer.data, request.data)
        _next = None
        _prev = None
        _count = len(response)

        if request.GET.get('paginate'):
            if request.GET.get('page'):
                page = int(request.GET.get('page'))
                if page > 1:
                    _prev = "127.0.0.1:8000/api/v1/user_info_filtered/?paginate=true&page=" \
                        + str(page-1)
            else:
                page = 1
            if _count > page * 10:
                _next = "127.0.0.1:8000/api/v1/user_info_filtered/?paginate=true&page=" \
                    + str(page + 1)
            response = response[(page-1)*10 : page*10]

        custom_response = {
            'next' : _next,
            'previous' : _prev,
            'count' : _count,
            'results' : response
        }

        return Response(custom_response)

# POST filtered_users/
class FilterUserViewSet(viewsets.ModelViewSet):
    queryset = models.FullUser.objects.all()

    def create(self, request):
        self.queryset = models.FullUser.objects.all()
        self.queryset = utils.filter_user(self.queryset, request.data)

        if request.GET.get('paginate'):
            page = self.paginate_queryset(self.queryset)
        else:
            page = self.queryset

        serializer = serializers.UserSerializer(page, many=True)

        for _s in serializer.data:
            _s['picture'] = utils.load_img(_s['picture'])

        if request.GET.get('paginate'):
            return self.get_paginated_response(serializer.data)

        custom_response = {
            'count' : len(serializer.data),
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
                return Response({'detail' : 'User is in the list.'})

        benefit = parsers.create_benefit(request.data)
        benefit.save()
        user.benefitlist.add(benefit)
        user.save()

        utils.check_achievements(user)
        user.refresh_from_db()

        serializer = serializers.BenefitSerializer(benefit)
        serializer.data['benefit_user']['picture'] = \
            utils.load_img(serializer.data['benefit_user']['picture'])
        return Response(serializer.data)

# PUT benefit_update/
class BenefitUpdate(generics.UpdateAPIView):
    def update(self, request):
        created_by = request.data['created_by']
        user = models.FullUser.objects.get(id=created_by)

        response = {'detail' : 'User is not in the list.'}
        for _b in user.benefitlist.all():
            if _b.id == request.data['benefit']:
                _b = parsers.update_benefit(_b, request.data)
                serializer = serializers.BenefitSerializer(_b)
                serializer.data['benefit_user']['picture'] = \
                    utils.load_img(serializer.data['benefit_user']['picture'])

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
        response = {'detail' : 'User is not in the list.'}
        for _b in benefits:
            if _b.benefit_user.id == ben_user.id:
                user.benefitlist.remove(_b)
                user.save()
                _b.delete()
                response = {'detail' : 'success'}
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

        response = {'detail' : 'Address is not in the list.'}

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
            return Response({'detail' : 'Address is not in the list.'})

        address = models.Address.objects.get(id=aid)
        user.addresses.remove(address)
        user.save()
        address.delete()

        return Response({'detail' : 'success'})


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

        response = {'detail' : 'Working hours are not in the list.'}

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
            return Response({'detail' : 'Working hours are not in the list.'})

        working_hour = models.WorkingHour.objects.get(id=wid)
        user.working_hours.remove(working_hour)
        user.save()
        working_hour.delete()

        return Response({'detail' : 'success'})


# ====================== USER SERVICE ======================
# POST user_service_add/
class UserServiceAdd(generics.ListCreateAPIView):
    def create(self, request):
        created_by = request.data['created_by']
        user_service = parsers.create_user_service(request.data)
        user = models.FullUser.objects.get(id=created_by)

        for _s in user.services.all():
            if _s.service.id == request.data['service']:
                return Response({'detail' : 'Service is in the list.'})

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

        response = {'detail' : 'User service is not in the list.'}

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
            return Response({'detail' : 'Service is not in the list.'})

        service = models.UserService.objects.get(id=sid)
        user.services.remove(service)
        user.save()
        service.delete()

        return Response({'detail' : 'success'})


# !!! REMOVE !!!
# ================= BLOCK =================
# POST block_add/
class BlockAdd(generics.ListCreateAPIView):
    def create(self, request):
        created_by = request.data['created_by']
        user = models.FullUser.objects.get(id=created_by)
        blocked = models.User.objects.get(id=request.data['blocked'])

        for _b in user.blocked.all():
            if _b.id == blocked.id:
                return Response({
                    'detail' : 'User is blocked.'})

        user.blocked.add(blocked)
        user.save()
        serializer = serializers.FullUserSerializer(user)
        _r = serializer.data
        for _b in _r['blocked']:
            _b['picture'] = utils.load_img(_b['picture'])

        return Response(_r['blocked'])

# DELETE block_remove/
class BlockRemove(generics.RetrieveDestroyAPIView):
    def destroy(self, request):
        created_by = request.data['created_by']
        user = models.FullUser.objects.get(id=created_by)

        response = {'detail' : 'User is not in the list.'}
        for _b in user.blocked.all():
            if _b.id == request.data['blocked']:
                user.blocked.remove(_b)
                user.save()
                response = {'detail' : 'success'}
                break

        return Response(response)


# ================= REPORT =================
# POST report_create/
class ReportCreate(generics.ListCreateAPIView):
    def create(self, request):
        report = parsers.create_report(request.data)
        serializer = serializers.ReportSerializer(report)
        response = serializer.data
        if response['created_by']['picture']:
            response['created_by']['picture'] = \
                utils.load_img(response['created_by']['picture'])
        if response['reported_user']['picture']:
            response['reported_user']['picture'] = \
                utils.load_img(response['reported_user']['picture'])
        if response['pictures']:
            for _p in response['pictures']:
                _p['picture'] = utils.load_img(_p['picture'])
        if response['request']:
            response['request'] = utils.load_pictures_request_info(response['request'])

        return Response(response)


# ================= BAN =================
# POST ban_create/
class BanCreate(generics.ListCreateAPIView):
    def create(self, request):
        admin = models.User.objects.get(id=request.data['created_by'])
        if not admin.is_admin:
            return Response({'detail' : 'User is not admin.'})

        user = models.User.objects.get(id=request.data['banned_user'])
        banned_user = models.FullUser.objects.get(user=user)
        if banned_user.ban:
            banned_user.ban.until = request.data['until']
            banned_user.ban.total_bans = banned_user.ban.total_bans + 1
            banned_user.ban.save()
        else:
            ban = models.Banned(until=request.data['until'],
                                comment=request.data['comment'],
                                banned_user=user,
                                total_bans=1)
            ban.save()
            banned_user.ban = ban
            banned_user.save()
        token, _ = Token.objects.get_or_create(user=user)
        if token:
            user.auth_token.delete()
            user.status = 0
            user.save()

        return Response({'detail' : 'success'})


# =============== RATE USER ===============
# POST rate_user/
class RateUser(generics.ListCreateAPIView):
    def create(self, request):
        rating = parsers.create_rating(request.data)
        user = models.FullUser.objects.get(id=request.data['rated_user'])
        req = models.FullRequest.objects.get(id=request.data['request'])

        if req.request.status < 2:
            return Response({'detail' : 'Request is not finished'})

        if req.request.created_by.id == request.data['rated_user'] and \
                    req.request.rated_created_by:
            return Response({'detail' : 'User is already rated for this request'})

        if req.request.working_with.id == request.data['rated_user'] and \
                    req.request.rated_working_with:
            return Response({'detail' : 'User is already rated for this request'})

        user.ratings.add(rating)
        user.save()
        utils.update_rating(user)

        if req.request.created_by.id == request.data['rated_user']:
            req.rating_created_by = rating
            req.request.rated_created_by = True
        else:
            req.rating_working_with = rating
            req.request.rated_working_with = True

        req.request.save()
        req.save()

        rating.refresh_from_db()

        # send notification rating
        _cb = models.User.objects.get(id=request.data['created_by'])
        notif_body, notif = utils.create_notification(8, rating.id,
                                                      first_name=_cb.first_name,
                                                      last_name=_cb.last_name,
                                                      rating=rating.grade,
                                                      working_with=_cb)
        utils.send_notification(user, notif, notif_body)

        utils.check_achievements(user)

        serializer = serializers.RatingSerializer(rating)
        _r = serializer.data
        _r['created_by']['picture'] = utils.load_img(_r['created_by']['picture'])
        _r['rated_user']['picture'] = utils.load_img(_r['rated_user']['picture'])
        return Response(_r)


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
        req.refresh_from_db()

        # send notification req_direct
        if req.direct_user:
            _u2 = models.FullUser.objects.get(user__id=req.direct_user.id)
            notif_body, notif = \
                utils.create_notification(0, req.id,
                                          first_name=user.user.first_name,
                                          last_name=user.user.last_name,
                                          request=req.name,
                                          working_with=user.user)
            utils.send_notification(_u2, notif, notif_body)

        utils.check_achievements(user)

        serializer = serializers.RequestSerializer(req)
        _s = serializer.data
        _s = utils.load_pictures_request_info(_s)
        return Response(serializer.data)

# POST request_start/
class RequestStart(generics.UpdateAPIView):
    def update(self, request):
        req = models.Request.objects.get(id=request.data['request'])
        req.timestamp = request.data['timestamp']
        loc = models.Location(latitude=request.data['location']['latitude'],
                              longitude=request.data['location']['longitude'])
        loc.save()
        req.location = loc
        req.save()

        return Response({'detail' : 'success'})

# DELETE request_cancel/
class RequestCancel(generics.RetrieveDestroyAPIView):
    def destroy(self, request):
        created_by = request.data['created_by']
        user = models.FullUser.objects.get(id=created_by)
        rid = request.data['request']

        found = False
        for _r in user.requests.all():
            if _r.id == rid:
                found = True
                break

        if not found:
            return Response({'detail' : 'Request is not in the list.'})

        req = models.Request.objects.get(id=rid)
        user.requests.remove(req)
        user.save()

        req.delete()

        return Response({'detail' : 'success'})

# GET requests_info/{id}
class RequestViewSet(viewsets.ModelViewSet):
    queryset = models.Request.objects.all()

    def list(self, request):
        self.queryset = models.Request.objects.all()
        if request.GET.get('paginate'):
            page = self.paginate_queryset(self.queryset)
        else:
            page = self.queryset

        serializer = serializers.RequestSerializer(page, many=True)

        if request.GET.get('paginate'):
            return self.get_paginated_response(serializer.data)

        _s = serializer.data
        _s = utils.load_pictures_multiple_requests_info(_s)

        custom_response = {
            'count' : len(_s),
            'results' : _s
        }
        return Response(custom_response)

    def retrieve(self, request, pk):
        service = get_object_or_404(self.queryset, pk=pk)
        serializer = serializers.RequestSerializer(service)
        _s = serializer.data
        _s = utils.load_pictures_request_info(_s)
        return Response(_s)

# GET requests/{id}
class FullRequestViewSet(viewsets.ModelViewSet):
    queryset = models.FullRequest.objects.all()

    def list(self, request):
        self.queryset = models.FullRequest.objects.all()
        if request.GET.get('paginate'):
            page = self.paginate_queryset(self.queryset)
        else:
            page = self.queryset

        serializer = serializers.FullRequestSerializer(page, many=True)

        if request.GET.get('paginate'):
            return self.get_paginated_response(serializer.data)

        _s = serializer.data
        _s = utils.load_pictures_multiple_requests(_s)

        custom_response = {
            'count' : len(_s),
            'results' : _s
        }
        return Response(custom_response)

    def retrieve(self, request, pk):
        service = get_object_or_404(self.queryset, pk=pk)
        serializer = serializers.FullRequestSerializer(service)
        _s = serializer.data
        _s = utils.load_pictures_request(_s)
        return Response(_s)

# GET requests_other/
class RequestOtherViewSet(viewsets.ModelViewSet):
    queryset = models.Request.objects.all()

    def list(self, request):
        self.queryset = models.Request.objects.all()
        queryset = list()

        for _q in self.queryset:
            found = False
            for _t in _q.tasklist.all():
                if _t.service_type.id == 1:
                    found = True
            if found:
                queryset.append(_q)

        if request.GET.get('paginate'):
            page = self.paginate_queryset(queryset)
        else:
            page = queryset

        serializer = serializers.RequestSerializer(page, many=True)

        if request.GET.get('paginate'):
            return self.get_paginated_response(serializer.data)

        _s = serializer.data
        _s = utils.load_pictures_multiple_requests_info(_s)

        custom_response = {
            'count' : len(_s),
            'results' : _s
        }

        return Response(custom_response)

# POST requests_info_filtered/
class RequestInfoFilteredViewSet(viewsets.ModelViewSet):
    queryset = models.FullRequest.objects.all()

    def create(self, request):
        self.queryset = models.FullRequest.objects.all()
        fullrequest = get_object_or_404(self.queryset, pk=request.data['request'])
        serializer = serializers.FullRequestSerializer(fullrequest)
        response = utils.filter_request_info(serializer.data, request.data)

        return Response(response)

# POST filtered_requests/
class FilterRequestViewSet(viewsets.ModelViewSet):
    queryset = models.FullRequest.objects.all()

    def create(self, request):
        self.queryset = models.FullRequest.objects.all().order_by('-id')
        self.queryset = utils.filter_requests(self.queryset, request.data)

        if request.GET.get('paginate'):
            page = self.paginate_queryset(self.queryset)
        else:
            page = self.queryset

        serializer = serializers.RequestSerializer(page, many=True)

        _r = serializer.data
        _r = utils.load_pictures_multiple_requests_info(_r)

        if request.GET.get('paginate'):
            return self.get_paginated_response(_r)

        custom_response = {
            'count' : len(_r),
            'results' : _r
        }

        return Response(custom_response)

# POST search_requests/
class SearchRequestViewSet(viewsets.ModelViewSet):
    queryset = models.FullRequest.objects.all()

    def create(self, request):
        self.queryset = models.Request.objects.all().order_by('-id')
        direct, broadcast, ddist, bdist = utils.search_requests(self.queryset, request.data)

        direct_serializer = serializers.RequestSerializer(direct, many=True)
        broadcast_serializer = serializers.RequestSerializer(broadcast, many=True)

        _d = direct_serializer.data
        _d = utils.load_pictures_multiple_requests_info(_d)
        _b = broadcast_serializer.data
        _b = utils.load_pictures_multiple_requests_info(_b)

        for direct, dist in zip(_d, ddist):
            direct.update({'dist' : dist})
        
        for bcast, dist in zip(_b, bdist):
            bcast.update({'dist' : dist})

        if request.data['latitude'] and request.data['longitude']:
            if _d:
                _d = sorted(_d, key=lambda x: x['dist'])

            if _b:
                _b = sorted(_b, key=lambda x: x['dist'])

        custom_response = {
            'count' : len(_d) + len(_b),
            'direct' : _d,
            'broadcast' : _b
        }

        return Response(custom_response)

# PUT set_arrived/
class SetArrived(generics.UpdateAPIView):
    def update(self, request):
        aid = request.data['address']
        address = models.Address.objects.get(id=aid)
        address.arrived = True
        address.save()

        rid = request.data['request']
        req = models.FullRequest.objects.get(request__id=rid)
        if req.accepted_offer.payment_type == "1" and req.request.location:
            lon1 = req.request.location.longitude
            lat1 = req.request.location.latitude
            lon2 = address.longitude
            lat2 = address.latitude

            dist = utils.calc_distance(lon1, lat1, lon2, lat2)
            req.request.price += dist
            req.request.save()
        req.request.location.latitude = address.latitude
        req.request.location.longitude = address.longitude
        req.request.location.save()

        return Response({'detail' : 'success'})

# PUT request_finish/
class RequestFinish(generics.UpdateAPIView):
    def update(self, request):
        created_by = models.User.objects.get(id=request.data['created_by'])
        req = models.FullRequest.objects.get(id=request.data['request'])

        if not req:
            return Response({'detail' : 'Request does not exist.'})

        if req.request.status != 1:
            return Response({'detail' : 'Request is pending or already finished.'})

        if created_by.id == req.request.created_by.id:
            if request.data['status'] == 3:
                req.request.status = 3
                req.request.save()
            elif request.data['status'] == 2:
                req.request.finished_created_by = True
                req.request.save()

        elif created_by.id == req.request.working_with.id:
            if request.data['status'] == 3:
                req.request.status = 3
                req.request.save()
            elif request.data['status'] == 2:
                req.request.finished_working_with = True
                req.request.save()

                if (req.accepted_offer.payment_type == "1" and
                        req.request.location and
                        request.data['location'] and
                        request.data['location']['longitude'] and
                        request.data['location']['latitude']):
                    lon1 = req.request.location.longitude
                    lat1 = req.request.location.latitude
                    lon2 = request.data['location']['longitude']
                    lat2 = request.data['location']['latitude']

                    dist = utils.calc_distance(lon1, lat1, lon2, lat2)
                    req.request.price += dist
                    req.request.save()

        price = None
        if req.request.finished_created_by and req.request.finished_working_with:
            req.request.status = 2
            req.request.save()

            price = req.request.price
            rate = req.accepted_offer.payment_ammount
            if req.accepted_offer.payment_type == "1":
                price = (price / 1000) * rate
            elif req.accepted_offer.payment_type == "0":
                duration = datetime.now(timezone.utc) - req.request.timestamp
                sec = duration.total_seconds()
                hours = sec // 60 / 60
                price = rate * hours
            else:
                price = rate

            _cb = models.FullUser.objects.get(user__id=req.request.created_by.id)
            discount = 0.0
            for _b in _cb.benefitlist.all():
                if _b.benefit_user.id == req.request.working_with.id:
                    discount = _b.discount
                    break

            price = price * (1 - discount)
            req.request.price = price
            req.request.save()

        # automatic adding to benefitlist
        uid1 = req.request.working_with.id
        uid2 = req.request.created_by.id
        user1 = models.FullUser.objects.get(user__id=uid1)
        user2 = models.FullUser.objects.get(user__id=uid2)

        found1, found2 = 0, 0
        for _b in user1.benefitlist.all():
            if _b.benefit_user.id == uid2:
                found1 = 1
        for _b in user2.benefitlist.all():
            if _b.benefit_user.id == uid1:
                found2 = 1

        if found1 == 0 or found2 == 0:
            count = 0
            for _r in user1.requests.all():
                if (_r.status == 2 and _r.working_with.id == uid2):
                    count += 1

            for _r in user2.requests.all():
                if (_r.status == 2 and _r.working_with.id == uid1):
                    count += 1

            if found1 == 0 and count >= user1.user.benefit_requirement:
                ben = models.Benefit(benefit_user=user2,
                                     discount=user1.user.benefit_discount)
                ben.save()
                user1.benefitlist.add(ben)
                user1.save()

            if found2 == 0 and count >= user2.user.benefit_requirement:
                ben = models.Benefit(benefit_user=user1,
                                     discount=user2.user.benefit_discount)
                ben.save()
                user2.benefitlist.add(ben)
                user2.save()

        # send notification req_failed OR req_success
        if request.data['status'] == 2:
            _t = 10
        elif request.data['status'] == 3:
            _t = 1
        notif_body, notif = utils.create_notification(_t, req.id,
                                                      request=req.request.name)
        if request.data['created_by'] == uid1:
            utils.send_notification(user2, notif, notif_body)
        else:
            utils.send_notification(user1, notif, notif_body)

        utils.check_achievements(user1)
        utils.check_achievements(user2)

        return Response({'price' : price})

# POST offer_create/
class OfferCreate(generics.ListCreateAPIView):
    def create(self, request):
        offer = parsers.create_offer(request.data)
        created_by = request.data['created_by']
        user = models.FullUser.objects.get(id=created_by)
        user.offers.add(offer)
        user.save()

        offer.refresh_from_db()

        # send notification offer_created
        _u2 = models.FullUser.objects.get(user__id=offer.request.created_by.id)
        notif_body, notif = utils.create_notification(2, offer.request.id,
                                                      request=offer.request.name,
                                                      first_name=user.user.first_name,
                                                      last_name=user.user.last_name,
                                                      working_with=user.user)
        utils.send_notification(_u2, notif, notif_body)

        serializer = serializers.OfferSerializer(offer)
        serializer.data['created_by']['picture'] = \
            utils.load_img(serializer.data['created_by']['picture'])

        return Response(serializer.data)

# PUT offer_accept/
class OfferAccept(generics.UpdateAPIView):
    def update(self, request):
        created_by = models.User.objects.get(id=request.data['created_by'])
        req = models.FullRequest.objects.get(id=request.data['request'])

        if created_by.id != req.request.created_by.id or req.request.status > 0:
            return Response({'detail' : 'Offer cannot be accepted.'})

        req = parsers.accept_offer(request.data)

        if not req:
            return Response({'detail' : 'Offer cannot be accepted.'})

        # send notification offer_accepted
        _u2 = models.FullUser.objects.get(id=req.accepted_offer.created_by.id)
        notif_body, notif = utils.create_notification(3, req.request.id,
                                                      request=req.request.name)
        utils.send_notification(_u2, notif, notif_body)

        serializer = serializers.FullRequestSerializer(req)
        _s = serializer.data
        _s = utils.load_pictures_request(_s)

        return Response(_s)

# DELETE offer_cancel/
class OfferCancel(generics.RetrieveDestroyAPIView):
    def destroy(self, request):
        rid = request.data['offer']
        if models.Offer.objects.filter(pk=rid).exists():
            offer = models.Offer.objects.get(id=rid)
            if offer.created_by != request.data['created_by']:
                # send notification offer_rejected
                _u2 = models.FullUser.objects.get(id=offer.created_by.id)
                notif_body, notif = utils.create_notification(4, offer.request.id,
                                                              request=offer.request.name)
                utils.send_notification(_u2, notif, notif_body)

            if offer.edit:
                offer.edit.delete()
            offer.delete()

            return Response({'detail' : 'success'})
        else:
            return Response({'detail' : 'Offer does not exist.'})

# POST edit_create/
class EditCreate(generics.ListCreateAPIView):
    def create(self, request):
        edit = parsers.create_edit(request.data)
        serializer = serializers.EditSerializer(edit)

        # send notification edit_created
        user = models.User.objects.get(id=request.data['created_by'])
        _u2 = models.FullUser.objects.get(user__id=edit.working_with.id)
        notif_body, notif = utils.create_notification(5, edit.request_edit.request.id,
                                                      request=edit.request_edit.request.name,
                                                      first_name=user.first_name,
                                                      last_name=user.last_name,
                                                      working_with=user)
        utils.send_notification(_u2, notif, notif_body)

        return Response(serializer.data)

# PUT edit_accept/
class EditAccept(generics.UpdateAPIView):
    def update(self, request):
        created_by = models.User.objects.get(id=request.data['created_by'])
        req = models.FullRequest.objects.get(id=request.data['request'])
        edit = models.Edit.objects.get(id=request.data['edit'])

        if created_by.id != req.request.created_by.id or req.request.status != 1:
            return Response({'detail' : 'Edit cannot be accepted.'})

        req = parsers.accept_edit(request.data)

        if not req:
            return Response({'detail' : 'Edit cannot be accepted.'})

        # send notification edit_accepted
        _u2 = models.FullUser.objects.get(user__id=edit.working_with.id)
        notif_body, notif = utils.create_notification(6, req.request.id,
                                                      request=req.request.name)
        utils.send_notification(_u2, notif, notif_body)

        serializer = serializers.FullRequestSerializer(req)
        _s = serializer.data
        _s = utils.load_pictures_request(_s)

        return Response(_s)

# DELETE edit_cancel/
class EditCancel(generics.RetrieveDestroyAPIView):
    def destroy(self, request):
        rid = request.data['edit']
        if models.Edit.objects.filter(pk=rid).exists():
            edit = models.Edit.objects.get(id=rid)

            if edit.created_by != request.data['created_by']:
                _u2 = edit.created_by
            else:
                _u2 = edit.working_with

            # send notification edit_rejected
            _u2 = models.FullUser.objects.get(user__id=_u2.id)
            notif_body, notif = utils.create_notification(7, edit.request_edit.request.id,
                                                          request=edit.request_edit.request.name)
            utils.send_notification(_u2, notif, notif_body)

            edit.request_edit.delete()
            return Response({'detail' : 'success'})
        else:
            return Response({'detail' : 'Edit does not exist.'})

# POST picture_upload/
class PictureUpload(generics.ListCreateAPIView):
    def create(self, request):
        rid = request.data['request_id']
        tid = request.data['task_id']
        pic = request.data['picture']

        if rid and models.Request.objects.filter(pk=rid).exists():
            req = models.Request.objects.get(id=rid)

            picture = models.Picture(picture=None)
            picture.save()
            pic = parsers.create_picture(pic, 'pictures/' + str(picture.id))
            picture.picture = pic
            picture.save()

            req.pictures.add(picture)
            req.save()
            serializer = serializers.RequestSerializer(req)
            response = utils.load_pictures_request_info(serializer.data)
            return Response(response)

        if tid and models.Task.objects.filter(pk=tid).exists():
            task = models.Task.objects.get(id=tid)

            picture = models.Picture(picture=None)
            picture.save()
            pic = parsers.create_picture(pic, 'pictures/' + str(picture.id))
            picture.picture = pic
            picture.save()

            task.pictures.add(picture)
            task.save()
            serializer = serializers.TaskSerializer(task)
            response = serializer.data
            for _r in response['pictures']:
                _r['picture'] = utils.load_img(_r['picture'])
            return Response(response)

        return Response({'detail' : 'request_id or task_id not valid.'})

# DELETE picture_remove/
class PictureRemove(generics.RetrieveDestroyAPIView):
    def destroy(self, request):
        pid = request.data['picture']
        if models.Picture.objects.filter(pk=pid).exists():
            pic = models.Picture.objects.get(id=pid)
            utils.remove_img(str(pic.picture))
            pic.delete()
            return Response({'detail' : 'success'})
        else:
            return Response({'detail' : 'Picture does not exist.'})

# PUT notification_flags_update/
class NotificationFlagsUpdate(generics.UpdateAPIView):
    def update(self, request):
        if request.data['ids']:
            for nid in request.data['ids']:
            # nid = request.data['ids']
                notif = models.Notification.objects.get(id=nid)

                if request.data['seen']:
                    notif.seen = True
                    notif.save()

                if request.data['opened']:
                    notif.opened = True
                    notif.save()

            return Response({'detail' : 'success'})
        return Response({'detail' : 'failed'})

# GET notification/{id}
class NotificationViewSet(viewsets.ModelViewSet):
    queryset = models.Notification.objects.all()
    def list(self, request):
        pass

    def retrieve(self, request, pk):
        queryset = models.Notification.objects.all()
        notif = get_object_or_404(queryset, pk=pk)
        serializer = serializers.NotificationSerializer(notif)
        return Response(serializer.data)

# GET unseen_notifications/{id}
class UnseenNotificationsViewSet(viewsets.ModelViewSet):
    queryset = models.FullUser.objects.all()
    def list(self, request):
        pass

    def retrieve(self, request, pk):
        queryset = models.FullUser.objects.all()
        user = get_object_or_404(queryset, pk=pk)
        count = 0
        for _n in user.notifications.all():
            if not _n.seen:
                count += 1
        return Response({'count' : count})


# ================= ACHIEVEMENTS =================
# GET achievements/{id}
class AchievementViewSet(viewsets.ModelViewSet):
    queryset = models.Achievement.objects.all()
    serializer_class = serializers.AchievementSerializer

    def list(self, request):
        self.queryset = models.Achievement.objects.all()
        if request.GET.get('paginate'):
            page = self.paginate_queryset(self.queryset)
        else:
            page = self.queryset

        serializer = serializers.AchievementSerializer(page, many=True)
        for _q, _s in zip(page, serializer.data):
            _s['icon'] = utils.load_img(_s['icon'])

        if request.GET.get('paginate'):
            return self.get_paginated_response(serializer.data)

        custom_response = {
            'count' : len(serializer.data),
            'results' : serializer.data
        }
        return Response(custom_response)

    def retrieve(self, request, pk):
        achievement = get_object_or_404(self.queryset, pk=pk)
        serializer = serializers.AchievementSerializer(achievement)
        response = serializer.data
        response['icon'] = utils.load_img(response['icon'])
        return Response(response)


# ================= SERVICES =================
# GET services/{id}
class ServiceViewSet(viewsets.ModelViewSet):
    queryset = models.Service.objects.all().order_by('-id')

    def list(self, request):
        self.queryset = models.Service.objects.all().order_by('-id')
        if request.GET.get('paginate'):
            page = self.paginate_queryset(self.queryset)
        else:
            page = self.queryset

        serializer = serializers.ServiceSerializer(page, many=True)

        if request.GET.get('paginate'):
            return self.get_paginated_response(serializer.data)

        custom_response = {
            'count' : len(serializer.data),
            'results' : serializer.data
        }
        return Response(custom_response)

    def retrieve(self, request, pk):
        service = get_object_or_404(self.queryset, pk=pk)
        serializer = serializers.ServiceSerializer(service)
        return Response(serializer.data)


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
            if response['icon']:
                response['icon'] = utils.load_img(response['icon'])

            users = models.FullUser.objects.all()
            for _u in users:
                utils.check_achievements(_u)

            return Response(response)
        else:
            return Response({'detail' : 'User is not admin.'})

# POST service_create/
class ServiceCreate(generics.ListCreateAPIView):
    def create(self, request):
        created_by = request.data['created_by']
        user = models.User.objects.get(id=created_by)

        if user.is_admin:
            service = parsers.create_service(request.data)
            serializer = serializers.ServiceSerializer(service)
            return Response(serializer.data)
        else:
            return Response({'detail' : 'User is not admin.'})

# PUT service_update/
class ServiceUpdate(generics.UpdateAPIView):
    def update(self, request):
        created_by = request.data['created_by']
        user = models.User.objects.get(id=created_by)
        service = models.Service.objects.get(id=request.data['service'])

        if not service:
            return Response({'detail' : 'Service does not exist.'})

        if user.is_admin:
            service = parsers.update_service(service, request.data)
            serializer = serializers.ServiceSerializer(service)
            return Response(serializer.data)
        else:
            return Response({'detail' : 'User is not admin.'})

# PUT service_task_update/
class ServiceTaskUpdate(generics.UpdateAPIView):
    def update(self, request):
        created_by = request.data['created_by']
        user = models.User.objects.get(id=created_by)
        task = models.Task.objects.get(id=request.data['task'])
        service = models.Service.objects.get(id=request.data['service'])

        if not task:
            return Response({'detail' : 'Task does not exist.'})

        if not service:
            return Response({'detail' : 'Service does not exist.'})

        if user.is_admin:
            task.service_type = service
            task.save()
            return Response({'detal' : 'success'})
        else:
            return Response({'detail' : 'User is not admin.'})


# POST reports/
class ReportViewSet(viewsets.ModelViewSet):
    queryset = models.Report.objects.all()
    serializer_class = serializers.ReportSerializer

    def create(self, request):
        created_by = request.data['created_by']
        user = models.User.objects.get(id=created_by)

        if user.is_admin:
            if request.data['handled'] == 0:
                self.queryset = models.Report.objects.all().order_by('-id')
            else:
                self.queryset = models.Report.objects.all()

            self.queryset = utils.filter_reports(self.queryset, request.data)

            if request.GET.get('paginate'):
                page = self.paginate_queryset(self.queryset)
            else:
                page = self.queryset

            serializer = serializers.ReportSerializer(page, many=True)
            response = serializer.data
            for _r in response:
                if _r['created_by']['picture']:
                    _r['created_by']['picture'] = utils.load_img(_r['created_by']['picture'])
                if _r['reported_user']['picture']:
                    _r['reported_user']['picture'] = utils.load_img(_r['reported_user']['picture'])
                if _r['pictures']:
                    for _p in _r['pictures']:
                        _p['picture'] = utils.load_img(_p['picture'])
                if _r['request']:
                    _r['request'] = utils.load_pictures_request_info(_r['request'])

            if request.GET.get('paginate'):
                return self.get_paginated_response(response)

            custom_response = {
                'count' : len(response),
                'results' : response
            }
            return Response(custom_response)
        else:
            return Response({'detail' : 'User is not admin.'})

# PUT report_handle/
class ReportHandle(generics.UpdateAPIView):
    queryset = models.Report.objects.all()
    serializer_class = serializers.ReportSerializer

    def update(self, request):
        self.queryset = models.Report.objects.all()
        report = get_object_or_404(self.queryset, id=request.data['id'])
        created_by = request.data['created_by']
        user = models.User.objects.get(id=created_by)

        if user.is_admin:
            report.status = request.data['status']
            report.save()
            return Response({'detail' : 'success'})
        else:
            return Response({'detail' : 'User is not admin.'})

# GET stats/
class Stats(generics.CreateAPIView):
    def create(self, request):
        created_by = request.data['created_by']
        user = models.User.objects.get(id=created_by)

        if user.is_admin:
            stats = utils.get_stats()
            return Response({'number_of_users' : stats[0],
                             'new_users' : stats[1],
                             'requests_created' : stats[2],
                             'finished_requests' : stats[3],
                             'tasks_per_service' : stats[4],
                             'user_service_per_service' : stats[5],
                             'achievements_distribution' : stats[6],
                             'users_per_grade' : stats[7]})
        else:
            return Response({'detail' : 'User is not admin.'})


# =============== FIREBASE CLOUD MESSAGING ==============
# =======================================================
class FCMRegister(generics.CreateAPIView):
    def create(self, request):
        user = models.User.objects.get(id=request.data['created_by'])
        reg_id = request.data['reg_id']
        fcm_devices = FCMDevice.objects.filter(registration_id=reg_id).count()

        if fcm_devices > 0:
            return Response({'detail' : 'Device already exists.'})

        fcm_device = FCMDevice(registration_id=reg_id,
                               user=user)
        fcm_device.save()
        return Response({'fcm' : fcm_device.id})

class FCMUnregister(generics.DestroyAPIView):
    def destroy(self, request):
        reg_id = request.data['reg_id']
        fcm_device = FCMDevice.objects.filter(registration_id=reg_id).count()
        if not fcm_device:
            return Response({'detail' : 'Device does not exist.'})

        fcm_device = FCMDevice.objects.get(registration_id=reg_id)
        fcm_device.delete()
        return Response({'detail' : 'success'})

class FCMTestNotification(generics.CreateAPIView):
    def create(self, request):
        dist = utils.calc_distance(43.31752396, 21.89941025, 43.32719, 21.903399)

        return Response({'result' : dist})
