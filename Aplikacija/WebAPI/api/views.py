from rest_framework import viewsets, generics
from rest_framework.authtoken.models import Token
from rest_framework.authtoken.views import ObtainAuthToken
from rest_framework.response import Response

from django.shortcuts import get_object_or_404
from django.http import HttpResponse
from django.db import transaction

from . import models
from . import serializers
from . import parsers
from . import utils

'''
Endpoints:
__GET_________________________________________________________________________________________
+ GET users_info/{id} 				// Returns only basic user data
+ GET users/{id} 					// Returns full user data
+ POST filtered_users/ 				// Returns users based on filters
+ POST user_info_filtered           // Returns users info based on filters
+ GET get_cookie/{id}    			// Returns only basic user data

+ GET requests_info/{id} 			// Returns only basic request data
+ GET requests/{id} 				// Returns full request data (including offers and rating)
+ POST filtered_requests/ 			// Returns requests based on filters
+ POST requests_info_filtered/      // Returns requests info based on filters
- POST requests_search/             // Returns avaliable requests based on your filters

+ GET achievements/{id}				// Returns list of all achievements
+ GET services/{id}                 // Returns list of all types of services
- GET stats/ (A)		 			// Returns statistics based on filters
+ POST reports/ (A)                 // Returns filtered reports

__POST________________________________________________________________________________________
+ POST login/                       // Login as some user
+ POST user_create/					// Creates new user
+ POST benefit_add/					// Adds new user to benefit list
+ POST address_add/					// Adds new address to addresses
+ POST working_hours_add/ 			// Adds new working hours for user
+ POST user_service_add/			// Adds new user service for user
+ POST block_add/			        // Adds new user to blocklist
+ POST offer_create/ 				// Creates offer for request
+ POST edit_create/                 // Creates edit request
+ POST request_create/ 				// Creates request
+ POST picture_upload/              // Uploads picture to request or task
+ POST rate_user/ 					// Adds new rating for completed request
+ POST report_create/				// Reports user
- POST ban_create/ (A)	 			// Bans reported user
+ POST achievement_create/ (A)	 	// Creates new achievement
+ POST service_type_create/ (A)		// Creates new service type

__PUT_________________________________________________________________________________________
+ PUT logout/                       // Logout user
+ PUT user_update/			        // Updates basic user data
+ PUT user_benefit_update/          // Updates user's benefit settings
+ PUT user_status_update/           // Updates user's status
+ PUT benefit_update/			    // Updates benefit to benefit list
+ PUT address_update/			    // Updates address to addresses
+ PUT working_hours_update/ 		// Updates working hours for user
+ PUT user_service_update/		    // Updates user service for user
+ PUT report_handle/ (A)            // Handles reported user
- PUT service_edit/ (A)             // Edits service
+ PUT offer_accept/ 				// Accepts offer for request
+ PUT edit_accept/                  // Accpets edit request
+ PUT request_finish/               // Finishes request

__DELETE______________________________________________________________________________________
+ DELETE benefit_remove/ 			// Removes user from benefit list
+ DELETE address_remove/ 			// Removes address from user
+ DELETE working_hours_remove/ 		// Removes working hours from user
+ DELETE user_service_remove/ 		// Removes user service from user
+ DELETE block_remove/ 			    // Removes user from blocklist
- DELETE ban_remove/ (A)	 		// Remove user from banned list
+ DELETE request_cancel/ 			// Cancels request (when pending)
+ DELETE offer_cancel/ 			    // Cancels offer (when not accepted)
+ DELETE edit_cancel/               // Cancels edit request
+ DELETE picture_remove/            // Removes picture
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

# POST users_info_filtered/id
class UserInfoFilteredViewSet(viewsets.ModelViewSet):
    queryset = models.FullUser.objects.all()

    def create(self, request):
        self.queryset = models.FullUser.objects.all()
        fulluser = get_object_or_404(self.queryset, pk=request.data['created_by'])
        serializer = serializers.FullUserSerializer(fulluser)
        response = utils.filter_user_info(serializer.data, request.data)

        return Response(response)

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
            return Response({
                'detail' : 'Address is not in the list.'})

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
            return Response({
                'detail' : 'Working hours are not in the list.'})

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
                return Response({
                    'detail' : 'Service is in the list.'})

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
            return Response({
                'detail' : 'Service is not in the list.'})

        service = models.UserService.objects.get(id=sid)
        user.services.remove(service)
        user.save()
        service.delete()

        return Response({'detail' : 'success'})


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

# =============== RATE USER ===============
# POST rate_user/
class RateUser(generics.ListCreateAPIView):
    def create(self, request):
        rating = parsers.create_rating(request.data)
        user = models.FullUser.objects.get(id=request.data['rated_user'])
        req = models.FullRequest.objects.get(id=request.data['request'])

        if req.request.status != 2:
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
        serializer = serializers.RatingSerializer(rating)
        _r = serializer.data
        _r['created_by']['picture'] = utils.load_img(_r['created_by']['picture'])
        _r['rated_user']['picture'] = utils.load_img(_r['rated_user']['picture'])
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
        _s = serializer.data
        _s = utils.load_pictures_request_info(_s)
        return Response(serializer.data)

# DELETE request_cancel/
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
                'detail' : 'Request is not in the list.'})

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

        if req.request.finished_created_by and req.request.finished_working_with:
            req.request.status = 2
            req.request.save()

        return Response({'detail' : 'success'})

# POST offer_create/
class OfferCreate(generics.ListCreateAPIView):
    def create(self, request):
        offer = parsers.create_offer(request.data)
        created_by = request.data['created_by']
        user = models.FullUser.objects.get(id=created_by)
        user.offers.add(offer)
        user.save()
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
                notification = None
                #TODO: Send notification OFFER_REJECTED
            offer.edit.delete()
            offer.delete()
            return Response({'detail' : 'success'})
        else:
            return Response({
                'detail' : 'Offer does not exist.'})

# POST edit_create/
class EditCreate(generics.ListCreateAPIView):
    def create(self, request):
        edit = parsers.create_edit(request.data)
        serializer = serializers.EditSerializer(edit)

        return Response(serializer.data)

# PUT edit_accept/
class EditAccept(generics.UpdateAPIView):
    def update(self, request):
        created_by = models.User.objects.get(id=request.data['created_by'])
        req = models.FullRequest.objects.get(id=request.data['request'])

        if created_by.id != req.request.created_by.id or req.request.status != 1:
            return Response({'detail' : 'Edit cannot be accepted.'})

        req = parsers.accept_edit(request.data)

        if not req:
            return Response({'detail' : 'Edit cannot be accepted.'})

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
                notification = None
                # TODO: Send notification (EDIT REJECTED)
            else:
                notification = None
                # TODO: Send notification (EDIT CANCELED)

            edit.request_edit.delete()
            return Response({'detail' : 'success'})
        else:
            return Response({
                'detail' : 'Edit does not exist.'})

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
            return Response({
                'detail' : 'Picture does not exist.'})

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
            response['icon'] = utils.load_img(response['icon'])
            return Response(response)
        else:
            return Response({"detail" : "User is not admin."})

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
            return Response({"detail" : "User is not admin."})

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
