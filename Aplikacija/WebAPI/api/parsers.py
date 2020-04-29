from . import models
import logging

def parse_checklist(data):
    checklist = models.CheckList(check_list=data['check_list'])
    return checklist

def parse_working_hour(data):
    working_hour = models.WorkingHour(day=data['day'],
                                      work_from=data['work_from'],
                                      work_until=data['work_until'])
    return working_hour

def parse_address(data):
    address = models.Address(name=data['name'],
                             longitude=data['longitude'],
                             latitude=data['latitude'],
                             home=data['home'],
                             arrived=data['arrived'])
    return address

def parse_service(data):
    service = models.Service(service_type=data['service_type'],
                             description=data['description'],
                             picture_required=data['picture_required'])
    return service

def parse_user_service(data):
    service = models.Service.objects.get(id=data['service'])

    user_service = models.UserService(max_dist=data['max_dist'],
                                      payment_type=data['payment_type'],
                                      payment_ammount=data['payment_ammount'],
                                      min_rating=data['min_rating'],
                                      service=service)
    return user_service

def parse_task(data):
    # TODO: Parse checklist as array
    checklist = parse_checklist(data['checklist'])
    address = parse_address(data['address'])
    service_type = parse_service(data['service'])

    task = models.Task(name=data['name'],
                       description=data['description'],
                       checklist=checklist,
                       address=address,
                       picture_required=data['picture_required'],
                       service_type=service_type)
    return task

def parse_task_edit(data):
    task = models.Task.objects.filter(id=data['task'])
    address = parse_address(data['address'])
    task_edit = models.TaskEdit(task=task,
                                address=address)
    return task_edit

def parse_user(data):
    user = models.User(email=data['email'],
                       username=data['email'],
                       first_name=data['first_name'],
                       last_name=data['last_name'],
                       phone=data['phone'])
    return user

def parse_achievement(data):
    # TODO: Add icon
    icon = None
    achievement = models.Achievement(name=data['name'],
                                     description=data['description'],
                                     icon=icon,
                                     requirements=data['requirements'])
    return achievement

def parse_achievement_level(data):
    achievement = models.Achievement.objects.filter(id=data['achievement'])
    user = models.User.objects.filter(id=data['user'])
    achievements_level = models.AchievementLevel(level=data['level'],
                                                 achievement=achievement,
                                                 user=user)
    return achievements_level

def parse_benefit(data):
    benefit_user = models.User.objects.get(id=data['benefit_user'])
    benefit = models.Benefit(discount=data['discount'],
                             benefit_user=benefit_user)
    return benefit

def parse_report(data):
    reported_user = models.User.objects.filter(id=data['reported_user'])
    created_by = models.User.objects.filter(id=data['created_by'])
    report = models.Report(comment=data['comment'],
                           report_type=data['report_type'],
                           reported_user=reported_user,
                           created_by=created_by)
    return report

def parse_banned(data):
    banned_user = models.User.objects.filter(id=data['banned_user'])
    banned = models.Banned(until=data['until'],
                           comment=data['comment'],
                           banned_user=banned_user)
    return banned

def parse_request(data):
    service_type = models.Service.objects.filter(id=data['service_type'])
    created_by = models.User.objects.filter(id=data['created_by'])
    # TODO: Parse tasklist as array
    tasklist = parse_task(data['tasklist'])

    request = models.Request(name=data['name'],
                             time=data['time'],
                             picture_required=data['picture_required'],
                             note=data['note'],
                             request_type=data['request_type'],
                             tasklist=tasklist,
                             max_dist=data['max_dist'],
                             min_rating=data['min_rating'],
                             service_type=service_type,
                             created_by=created_by)
    return request

def parse_request_edit(data):
    # TODO: Parse tasks as array
    tasks = parse_task_edit(data['tasks'])
    request = models.Request.objects.filter(id=data['request'])
    request_edit = models.RequestEdit(time=data['time'],
                                      tasks=tasks,
                                      request=request)
    return request_edit

def parse_offer(data):
    created_by = models.User.objects.filter(id=data['created_by'])
    request = models.Request.objects.filter(id=data['request'])
    edit = parse_task_edit(data['edit'])
    offer = models.Offer(payment_type=data['payment_type'],
                         payment_ammount=data['payment_ammount'],
                         created_by=created_by,
                         request=request,
                         edit=edit)
    return offer

# TODO: DELETE???
def parse_notification(data):
    notification = models.Notification(title=data['title'],
                                       body=data['body'],
                                       notification_type=data['notofication_type'],
                                       type_id=data['type_id'])
    return notification

def parse_rating(data):
    created_by = models.User.objects.filter(id=data['created_by'])
    rated_user = models.User.objects.filter(id=data['rated_user'])
    request = models.Request.objects.filter(id=data['request'])
    rating = models.Rating(grade=data['grade'],
                           comment=data['comment'],
                           created_by=created_by,
                           rated_user=rated_user,
                           request=request)
    return rating
