import base64
import datetime
from django.core.files.base import ContentFile
from . import models

PIC_PATH = 'db/images/'
ADMIN_KEY = "hR6s7RPPRtEhQNSL3IT1LwM5XC0J1LcdOvXPFFlk"

def create_picture(data, name):
    ext = '.png'
    img = ContentFile(base64.b64decode(data), name=PIC_PATH + name + ext)
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
                           status=1)
    else:
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
                           status=1)
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
        user.picture = create_picture(data['picture'], 'users/' + str(user.id))
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
    _wf = datetime.datetime.strptime(data['work_from'], '%Y-%m-%d %H:%M')
    _wu = datetime.datetime.strptime(data['work_until'], '%Y-%m-%d %H:%M')
    working_hour = models.WorkingHour(work_from=_wf,
                                      work_until=_wu)
    return working_hour

def update_working_hour(working_hour, data):
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
    # TODO: Check if rating is valid (request.status = 3)
    created_by = models.User.objects.get(id=data['created_by'])
    rated_user = models.User.objects.get(id=data['rated_user'])
    request = models.Request.objects.get(id=data['request'])
    rating = models.Rating(grade=data['grade'],
                           comment=data['comment'],
                           created_by=created_by,
                           rated_user=rated_user,
                           request=request)
    return rating


# ================== REPORT ==================
def create_report(data):
    reported_user = models.User.objects.filter(id=data['reported_user'])
    created_by = models.User.objects.filter(id=data['created_by'])
    report = models.Report(comment=data['comment'],
                           report_type=data['report_type'],
                           reported_user=reported_user,
                           created_by=created_by)
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

def create_request(data):
    created_by = models.User.objects.get(id=data['created_by'])

    time = None
    if data['time']:
        time = data['time']

    request = models.Request(name=data['name'],
                             time=time,
                             picture_required=data['picture_required'],
                             note=data['note'],
                             max_dist=data['max_dist'],
                             min_rating=data['min_rating'],
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
    return task_edit

def create_request_edit(data):
    # TODO: Create tasks as array
    tasks = create_task_edit(data['tasks'])
    request = models.Request.objects.get(id=data['request'])
    request_edit = models.RequestEdit(time=data['time'],
                                      tasks=tasks,
                                      request=request)
    return request_edit

def create_offer(data):
    created_by = models.User.objects.filter(id=data['created_by'])
    request = models.Request.objects.filter(id=data['request'])
    edit = create_task_edit(data['edit'])
    offer = models.Offer(payment_type=data['payment_type'],
                         payment_ammount=data['payment_ammount'],
                         created_by=created_by,
                         request=request,
                         edit=edit)
    return offer


# ================== ADMIN ==================
def create_achievement(data):
    icon = None
    achievement = models.Achievement(name_sr=data['name_sr'],
                                     name_en=data['name_en'],
                                     description_sr=data['description_sr'],
                                     description_en=data['description_en'],
                                     icon=icon,
                                     requirements=data['requirements'])
    achievement.save()
    if data['icon']:
        icon = create_picture(data=data['icon'],
                              name='achievements/' + str(achievement.id))
        achievement.icon = icon
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

def create_ban(data):
    banned_user = models.User.objects.filter(id=data['banned_user'])
    banned = models.Banned(until=data['until'],
                           comment=data['comment'],
                           banned_user=banned_user)
    return banned

def create_notification(data):
    notification = models.Notification(title=data['title'],
                                       body=data['body'],
                                       notification_type=data['notofication_type'],
                                       type_id=data['type_id'])
    return notification
