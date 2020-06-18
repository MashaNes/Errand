import os
import base64
from django.core.files.base import ContentFile
from . import models

PIC_PATH = 'db/images/'
ADMIN_KEY = "hR6s7RPPRtEhQNSL3IT1LwM5XC0J1LcdOvXPFFlk"

def create_picture(data, name):
    ext = '.png'
    path = PIC_PATH + name + ext
    if os.path.exists(path):
        os.remove(path)
    img = ContentFile(base64.b64decode(data), name=path)
    return img


# ================== USER ==================
def create_user(data):
    if data['is_admin'] and data['admin_key'] == ADMIN_KEY:
        user = models.User(is_admin=1,
                           email=data['email'],
                           username=data['email'],
                           first_name=data['first_name'],
                           last_name=data['last_name'],
                           picture=None,
                           status=0)
    elif not data['is_admin']:
        _bd = 0.1
        _br = 5
        if data['benefit_discount']:
            _bd = data['benefit_discount']
        if data['benefit_requirement']:
            _br = data['benefit_requirement']
        user = models.User(is_admin=0,
                           email=data['email'],
                           username=data['email'],
                           first_name=data['first_name'],
                           last_name=data['last_name'],
                           phone=data['phone'],
                           picture=None,
                           benefit_discount=_bd,
                           benefit_requirement=_br,
                           status=0)
    else:
        return None

    user.save()

    fulluser = models.FullUser(user=user)
    fulluser.save()

    if data['password']:
        user.set_password(data['password'])

    if data['picture']:
        picture = create_picture(data['picture'], 'users/' + str(user.id))
        user.picture = picture

    user.save()

    return user

def update_user(user, data):
    if user.is_admin:
        user.email = data['email']
        user.username = data['email']
        user.first_name = data['first_name']
        user.last_name = data['last_name']
    else:
        user.email = data['email']
        user.username = data['email']
        user.first_name = data['first_name']
        user.last_name = data['last_name']
        user.phone = data['phone']
        user.min_rating = data['min_rating']
        user.max_dist = data['max_dist']
        user.benefit_discount = data['benefit_discount']
        user.benefit_requirement = data['benefit_requirement']

    if data['password']:
        user.set_password(data['password'])

    if data['picture']:
        picture = create_picture(data['picture'], 'users/' + str(user.id))
        user.picture = picture
    user.save()

    return user


# ================== BENEFIT ==================
def create_benefit(data):
    benefit_user = models.User.objects.get(id=data['benefit_user'])
    benefit = models.Benefit(discount=data['discount'],
                             benefit_user=benefit_user)
    return benefit

def update_benefit(benefit, data):
    benefit.discount = data['discount']
    benefit.save()
    return benefit


# ================== ADDRESS ==================
def create_address(data):
    address = models.Address(name=data['name'],
                             longitude=data['longitude'],
                             latitude=data['latitude'],
                             home=data['home'],
                             arrived=data['arrived'])
    address.save()
    return address

def update_address(address, data):
    address.name = data['name']
    address.longitude = data['longitude']
    address.latitude = data['latitude']
    address.home = data['home']
    address.arrived = data['arrived']
    address.save()
    return address


# ================== WORKING HOURS ==================
def create_working_hour(data):
    working_hour = models.WorkingHour(day=data['day'],
                                      work_from=data['work_from'],
                                      work_until=data['work_until'])
    working_hour.save()
    return working_hour

def update_working_hour(working_hour, data):
    working_hour.day = data['day']
    working_hour.work_from = data['work_from']
    working_hour.work_until = data['work_until']
    working_hour.save()
    return working_hour


# ================== USER SERVICE ==================
def create_user_service(data):
    service = models.Service.objects.get(id=data['service'])

    user_service = models.UserService(max_dist=data['max_dist'],
                                      payment_type=data['payment_type'],
                                      payment_ammount=data['payment_ammount'],
                                      min_rating=data['min_rating'],
                                      service=service)
    user_service.save()
    return user_service

def update_user_service(user_service, data):
    user_service.max_dist = data['max_dist']
    user_service.payment_type = data['payment_type']
    user_service.payment_ammount = data['payment_ammount']
    user_service.min_rating = data['min_rating']
    user_service.save()
    return user_service


# ================== RATING ==================
def create_rating(data):
    created_by = models.User.objects.get(id=data['created_by'])
    rated_user = models.User.objects.get(id=data['rated_user'])
    request = models.Request.objects.get(id=data['request'])
    rating = models.Rating(grade=data['grade'],
                           comment=data['comment'],
                           created_by=created_by,
                           rated_user=rated_user,
                           request=request)
    rating.save()
    return rating


# ================== REPORT ==================
def create_report(data):
    created_by = models.User.objects.get(id=data['created_by'])
    reported_user = models.User.objects.get(id=data['reported_user'])
    request = None
    if data['request']:
        request = models.Request.objects.get(id=data['request'])

    report = models.Report(created_by=created_by,
                           reported_user=reported_user,
                           request=request,
                           comment=data['comment'])
    report.save()

    if data['pictures']:
        for _p in data['pictures']:
            pic = models.Picture(picture=None)
            pic.save()
            pic.picture = create_picture(_p['picture'], 'pictures/' + str(pic.id))
            pic.save()
            report.pictures.add(pic)
    report.save()

    return report


# ================== REQUEST ==================
def create_task(data):
    address = None
    if data['address']:
        address = create_address(data['address'])
    service_type = models.Service.objects.get(id=data['service_type'])

    task = models.Task(name=data['name'],
                       description=data['description'],
                       address=address,
                       picture_required=data['picture_required'],
                       service_type=service_type)

    task.save()
    _checklist = data['checklist']

    if len(_checklist) > 0:
        for _c in _checklist:
            _nc = models.CheckList(check_list=_c['item'])
            _nc.save()
            task.checklist.add(_nc)

    task.save()
    return task

def update_task(data):
    task = models.Task.get(id=data['task'])
    address = create_address(data['address'])
    task.address = address
    task.save()

    return task

def create_request(data):
    created_by = models.User.objects.get(id=data['created_by'])
    direct_user = None
    if data['direct_user']:
        direct_user = models.User.objects.get(id=data['direct_user'])

    time = None
    if data['time']:
        time = data['time']

    destination = None
    if data['destination']:
        destination = create_address(data['destination'])

    request = models.Request(name=data['name'],
                             time=time,
                             picture_required=data['picture_required'],
                             note=data['note'],
                             max_dist=data['max_dist'],
                             min_rating=data['min_rating'],
                             destination=destination,
                             broadcast=data['broadcast'],
                             direct_user=direct_user,
                             created_by=created_by)
    request.save()
    _task_list = data['tasklist']

    if len(_task_list) > 0:
        for _t in _task_list:
            _nt = create_task(_t)
            request.tasklist.add(_nt)
    request.save()

    fullrequest = models.FullRequest(request=request)
    fullrequest.save()

    return request

def create_task_edit(data):
    task = models.Task.objects.get(id=data['task'])
    address = create_address(data['address'])
    task_edit = models.TaskEdit(task=task,
                                address=address)
    task_edit.save()
    return task_edit

def create_request_edit(data, request):
    time = None
    if data['time']:
        time = data['time']
    request_edit = models.RequestEdit(time=time,
                                      request=request)
    request_edit.save()

    if data['tasks']:
        _task_list = data['tasks']
        for _t in _task_list:
            _nt = create_task_edit(_t)
            request_edit.tasks.add(_nt)
        request_edit.save()

    return request_edit

def create_offer(data):
    created_by = models.User.objects.get(id=data['created_by'])
    request = models.Request.objects.get(id=data['request'])

    edit = None
    if data['edit']:
        edit = create_request_edit(data['edit'], request)

    offer = models.Offer(payment_type=data['payment_type'],
                         payment_ammount=data['payment_ammount'],
                         created_by=created_by,
                         request=request,
                         edit=edit)
    offer.save()
    fullrequest = models.FullRequest.objects.get(id=request.id)
    fullrequest.offers.add(offer)
    fullrequest.save()

    return offer

def create_edit(data):
    created_by = models.User.objects.get(id=data['created_by'])
    request = models.Request.objects.get(id=data['request'])
    working_with = request.created_by
    edit_req = create_request_edit(data['edit'], request)
    edit = models.Edit(created_by=created_by,
                       working_with=working_with,
                       request_edit=edit_req)
    edit.save()
    fullrequest = models.FullRequest.objects.get(id=request.id)
    fullrequest.edits.add(edit)
    fullrequest.save()

    return edit

def accept_offer(data):
    request = models.FullRequest.objects.get(id=data['request'])
    offer = models.Offer.objects.get(id=data['offer'])
    found = False

    if request.offers:
        for _f in request.offers.all():
            if _f.id == offer.id:
                found = True
                request.offers.remove(_f)
            else:
                _f.delete()

    if found:
        if offer.edit:
            if offer.edit.time:
                request.request.time = offer.edit.time
            if offer.edit.tasks:
                for _t in offer.edit.tasks.all():
                    for _ot in request.request.tasklist.all():
                        if _t.task.id == _ot.id:
                            _ot.address = _t.address
                            _ot.save()

        request.request.working_with = offer.created_by
        request.accepted_offer = offer
        request.request.status = 1 # active

        request.request.save()
        request.save()
        return request

    return None

def accept_edit(data):
    request = models.FullRequest.objects.get(id=data['request'])
    edit = models.Edit.objects.get(id=data['edit'])
    found = False

    if request.edits:
        for _e in request.edits.all():
            if _e.id == edit.id:
                found = True

    if found:
        if edit.request_edit:
            if edit.request_edit.time:
                request.request.time = edit.request_edit.time

            for _t in edit.request_edit.tasks.all():
                for _ot in request.request.tasklist.all():
                    if _t.task.id == _ot.id:
                        _ot.address = _t.address
                        _ot.save()

        edit.request_edit.delete()

        request.save()
        return request

    return None


# ================== ADMIN ==================
def create_achievement(data):
    icon = None
    achievement = models.Achievement(name_sr=data['name_sr'],
                                     name_en=data['name_en'],
                                     description_sr=data['description_sr'],
                                     description_en=data['description_en'],
                                     levels=data['levels'])
    achievement.save()
    if data['icon']:
        icon = create_picture(data=data['icon'],
                              name='achievements/' + str(achievement.id))
        achievement.icon = icon
        achievement.save()

    for i, _c in enumerate(data['conditions']):
        cond = models.Condition(condition=_c)
        cond.save()
        for _c_num in data['condition_numbers'][i]:
            cond_num = models.ConditionNumber(condition_number=_c_num)
            cond_num.save()
            cond.condition_numbers.add(cond_num)
        cond.save()
        achievement.conditions.add(cond)
    achievement.save()

    return achievement

def create_service(data):
    service = models.Service(service_type_sr=data['service_type_sr'],
                             service_type_en=data['service_type_en'],
                             description_sr=data['description_sr'],
                             description_en=data['description_en'],
                             picture_required=data['picture_required'])
    service.save()
    return service

def update_service(service, data):
    service.service_type_sr = data['service_type_sr']
    service.service_type_en = data['service_type_en']
    service.description_sr = data['description_sr']
    service.description_en = data['description_en']
    service.picture_required = data['picture_required']
    service.save()
    return service

def create_ban(data):
    banned_user = models.User.objects.get(id=data['banned_user'])
    banned = models.Banned(until=data['until'],
                           comment=data['comment'],
                           banned_user=banned_user)
    return banned
