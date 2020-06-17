import base64
import os.path

from fcm_django.models import FCMDevice
from datetime import datetime
from pytz import UTC

from . import models
from . import parsers

# =================== IMAGES ===================
# ==============================================
def load_img(img_path):
    base64_string = ""

    if not os.path.exists(img_path):
        return None

    with open(img_path, 'rb') as img_file:
        base64_string = str(base64.b64encode(img_file.read()))
        base64_string = base64_string[2:-1]
    return base64_string

def remove_img(img_path):
    if os.path.exists(img_path):
        os.remove(img_path)

def load_pictures_multiple_users(data):
    for _d in data:
        _d = load_pictures_user(_d)
    return data

def load_pictures_user(data):
    if data['user']:
        data['user']['picture'] = load_img(data['user']['picture'])

    if data['blocked']:
        for _d in data['blocked']:
            _d['picture'] = load_img(_d['picture'])

    if data['offers']:
        for _d in data['offers']:
            _d['created_by']['picture'] = load_img(_d['created_by']['picture'])

    if data['ratings']:
        for _d in data['ratings']:
            _d['created_by']['picture'] = load_img(_d['created_by']['picture'])
            _d['rated_user'] = _d['rated_user']['id']

    if data['benefitlist']:
        for _d in data['benefitlist']:
            _d['benefit_user']['picture'] = load_img(_d['benefit_user']['picture'])

    if data['achievements']:
        for _d in data['achievements']:
            _d['icon'] = load_img(_d['icon'])

    if data['requests']:
        data['requests'] = load_pictures_multiple_requests_info(data['requests'])

    return data

def load_pictures_multiple_requests_info(data):
    for _d in data:
        _d = load_pictures_request_info(_d)
    return data

def load_pictures_request_info(data):
    if data['created_by']:
        data['created_by']['picture'] = load_img(data['created_by']['picture'])

    if data['working_with']:
        data['working_with']['picture'] = load_img(data['working_with']['picture'])

    if data['direct_user']:
        data['direct_user']['picture'] = load_img(data['direct_user']['picture'])

    if data['pictures']:
        for _d in data['pictures']:
            _d['picture'] = load_img(_d['picture'])

    if data['tasklist']:
        for _d in data['tasklist']:
            for _p in _d['pictures']:
                _p['picture'] = load_img(_p['picture'])

    return data

def load_pictures_multiple_requests(data):
    for _d in data:
        _d = load_pictures_request(_d)
    return data

def load_pictures_request(data):
    if data['request']:
        data['request'] = load_pictures_request_info(data['request'])

    if data['offers']:
        for _d in data['offers']:
            _d['created_by']['picture'] = load_img(_d['created_by']['picture'])

    if data['accepted_offer']:
        data['accepted_offer']['created_by']['picture'] = \
            load_img(data['accepted_offer']['created_by']['picture'])

    if data['rating_created_by']:
        data['rating_created_by']['created_by']['picture'] = \
            load_img(data['rating_created_by']['created_by']['picture'])
        data['rating_created_by']['rated_user']['picture'] = \
            load_img(data['rating_created_by']['rated_user']['picture'])

    if data['rating_working_with']:
        data['rating_working_with']['created_by']['picture'] = \
            load_img(data['rating_working_with']['created_by']['picture'])
        data['rating_working_with']['rated_user']['picture'] = \
            load_img(data['rating_working_with']['rated_user']['picture'])

    return data


# =================== USER ===================
# ============================================
def update_rating(user):
    avg = 0

    if user.ratings.all():
        for _r in user.ratings.all():
            avg += _r.grade
        avg /= len(user.ratings.all())
        user.user.avg_rating = avg
        user.user.save()

def filter_user(queryset, data):
    new_queryset = list()
    if data['sort_rating']:
        sort = 'user__avg_rating'
        if data['sort_rating_asc']:
            sort = '-user__avg_rating'
        queryset = models.FullUser.objects.all().order_by(sort)
    else:
        queryset = models.FullUser.objects.all()
    user = models.FullUser.objects.get(id=data['created_by'])

    for _q in queryset:
        to_add = True
        if user.id == _q.id or _q.user.is_admin:
            to_add = False

        found = False
        if to_add:
            for _b in user.blocked.all():
                if _b.id == _q.user.id:
                    found = True
                    break
            to_add = not found

        if data['active']:
            if _q.user.status == 0:
                to_add = False

        if to_add and data['services']:
            for _s in data['services']:
                found = False
                if _s['id'] == 1:
                    found = True
                for _q_s in _q.services.all():
                    # TODO: Filter against location
                    if (_s['id'] == _q_s.service.id and _s['max_dist'] >= _q_s.max_dist):
                        if data['no_rating'] or _s['min_rating'] <= _q.user.avg_rating:
                            found = True
                            break
                to_add = found

        if to_add and data['name']:
            found = False
            name = _q.user.first_name + " " + _q.user.last_name
            if data['name'] in name:
                found = True
            to_add = found

        if to_add and data['not_in_benefit']:
            found = False
            for _b in user.benefitlist.all():
                if _b.benefit_user.id == _q.user.id:
                    found = True
                    break
            to_add = not found

        if to_add and data['sort_rating'] and len(_q.ratings.all()) == 0:
            if not data['no_rating']:
                to_add = False

        if _q.user.avg_rating:
            if to_add and data['rating_limit_up']:
                if _q.user.avg_rating > data['rating_limit_up']:
                    to_add = False

            if to_add and data['rating_limit_down']:
                if _q.user.avg_rating < data['rating_limit_down']:
                    to_add = False

        if to_add:
            new_queryset.append(_q)

    queryset = new_queryset
    new_queryset = list()

    for _q in queryset:
        new_queryset.append(models.User.objects.get(id=_q.user.id))

    return new_queryset

def filter_user_info(serializer, data):
    if data['blocked']:
        response = serializer['blocked']
        for _r in response:
            _r['picture'] = load_img(_r['picture'])
        return response

    if data['working_hours']:
        return serializer['working_hours']

    if data['addresses']:
        return serializer['addresses']

    if data['services']:
        return serializer['services']

    if data['offers']:
        response = serializer['offers']
        for _r in response:
            _r['created_by']['picture'] = load_img(_r['created_by']['picture'])
        return response

    if data['notifications']:
        return serializer['notifications']

    if data['ratings']:
        response = serializer['ratings']
        for _r in response:
            _r['created_by']['picture'] = load_img(_r['created_by']['picture'])
            _r['rated_user']['picture'] = load_img(_r['rated_user']['picture'])
        return response

    if data['benefitlist']:
        response = serializer['benefitlist']
        for _r in response:
            _r['benefit_user']['picture'] = load_img(_r['benefit_user']['picture'])
        return response

    if data['achievements']:
        response = serializer['achievements']
        for _r in response:
            _r['icon'] = load_img(_r['icon'])
        return response

    if data['requests']:
        response = serializer['requests']
        response = load_pictures_multiple_requests_info(response)
        return response

def filter_reports(queryset, data):
    new_queryset = list()

    for _q in queryset:
        to_add = True

        if data['handled']:
            if _q.status == 0:
                to_add = False
        else:
            if _q.status > 0:
                to_add = False

        if to_add and data['reported_by']:
            if _q.created_by.email != data['reported_by']:
                to_add = False

        if to_add and data['reported']:
            if _q.reported_user.email != data['reported']:
                to_add = False

        if to_add and data['reported_by_or_reported']:
            found = False

            if _q.created_by.email == data['reported_by_or_reported'] or \
                 _q.reported_user.email == data['reported_by_or_reported']:
                found = True

            to_add = found

        if to_add:
            new_queryset.append(_q)

    return new_queryset


# ==================== REQUEST ====================
# =================================================
def filter_requests(queryset, data):
    new_queryset = list()

    for _q in queryset:
        to_add = True

        if data['created_by']:
            if _q.request.created_by.id != data['created_by']:
                to_add = False
            if data['unrated_done_by'] and _q.request.rated_working_with:
                to_add = False

        if to_add and data['done_by']:
            if (_q.request.working_with and
                    _q.request.working_with.id != data['done_by']):
                to_add = False
            if data['unrated_created_by'] and _q.request.rated_created_by:
                to_add = False

        if to_add and data['created_or_done_by']:
            found = False

            if _q.request.created_by.id == data['created_or_done_by']:
                if not data['unrated_done_by']:
                    found = True
                if data['unrated_done_by'] and not _q.request.rated_working_with:
                    found = True

            if (_q.request.working_with and
                    _q.request.working_with.id == data['created_or_done_by']):
                if not data['unrated_created_by']:
                    found = True
                if data['unrated_created_by'] and not _q.request.rated_created_by:
                    found = True

            to_add = found

        if to_add and data['statuses']:
            found = False
            for _s in data['statuses']:
                if _s == _q.request.status:
                    found = True
                    break
            to_add = found

        if to_add:
            new_queryset.append(_q)

    queryset = new_queryset
    new_queryset = list()

    for _q in queryset:
        new_queryset.append(models.Request.objects.get(id=_q.request.id))

    return new_queryset

def filter_request_info(serializer, data):
    response = {}

    if data['offers']:
        response['offers'] = serializer['offers']
        for _d in response['offers']:
            _d['created_by']['picture'] = load_img(_d['created_by']['picture'])

    if data['accepted_offer']:
        response['accepted_offer'] = serializer['accepted_offer']
        response['accepted_offer']['created_by']['picture'] = \
            load_img(response['accepted_offer']['created_by']['picture'])

    if data['rating_created_by']:
        response['rating_created_by'] = serializer['rating_created_by']
    if data['rating_working_with']:
        response['rating_working_with'] = serializer['rating_working_with']

    if data['rating_created_by']:
        response['rating_created_by'] = serializer['rating_created_by']
        response['rating_created_by']['created_by']['picture'] = \
            load_img(response['rating_created_by']['created_by']['picture'])
        response['rating_created_by']['rated_user']['picture'] = \
            load_img(response['rating_created_by']['rated_user']['picture'])

    if data['rating_working_with']:
        response['rating_working_with'] = serializer['rating_working_with']
        response['rating_working_with']['created_by']['picture'] = \
            load_img(response['rating_working_with']['created_by']['picture'])
        response['rating_working_with']['rated_user']['picture'] = \
            load_img(response['rating_working_with']['rated_user']['picture'])

    if data['edits']:
        response['edits'] = serializer['edits']

    if data['tasklist']:
        response['tasklist'] = serializer['request']['tasklist']
        for _t in response['tasklist']:
            for _p in _t['pictures']:
                _p['picture'] = load_img(_p['picture'])

    if data['destination']:
        response['destination'] = serializer['request']['destination']

    if data['pictures']:
        response['pictures'] = serializer['request']['pictures']
        for _p in response['pictures']:
            _p['picture'] = load_img(_p['picture'])

    return response



# =================== STATS ===================
# =============================================
def get_stats():
    number_of_users = None
    new_users = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
    requests_created = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
    finished_requests = list()
    tasks_per_service = list()
    user_service_per_service = list()
    achievements_distribution = list()
    values = [0, 5, 10, 15, 20, 25, 30, 1000]
    no_of_users = [0, 0, 0, 0, 0, 0, 0]
    users_per_grade = [0, 0, 0, 0, 0]

    users = models.User.objects.filter(is_admin=0).all()
    fullusers = models.FullUser.objects.all()
    req = models.Request.objects.all()
    services = models.Service.objects.all()

    date = list()
    for i in range(12):
        date.append(UTC.localize(datetime(2020, i+1, 1)))

    # number_of_users
    number_of_users = users.count()

    # new_users
    for _u in users:
        for i in range(11):
            if _u.date_joined > date[i] and _u.date_joined <= date[i+1]:
                new_users[i] = new_users[i] + 1

    # requests_created
    for _r in req:
        for i in range(11):
            if _r.time > date[i] and _r.time < date[i+1]:
                requests_created[i] += 1

    # finished_requests
    finished_requests.append(models.Request.objects.filter(status=2).count())
    finished_requests.append(models.Request.objects.filter(status=3).count())

    # tasks_per_service
    for _ in services:
        tasks_per_service.append(0)

    for _r in req:
        for _t in _r.tasklist.all():
            tasks_per_service[_t.service_type.id-1] += 1

    # user_service_per_service
    for _ in services:
        user_service_per_service.append(0)

    for _fu in fullusers:
        for _s in _fu.services.all():
            user_service_per_service[_s.service.id-1] += 1

    # achievements_distribution
    for _fu in fullusers:
        for i in range(7):
            if (_fu.user.is_admin == 0 and
                    _fu.achievements.count() >= values[i] and
                    _fu.achievements.count() < values[i+1]):
                no_of_users[i] += 1
    achievements_distribution = (values[:-1], no_of_users)

    # users_per_grade
    grade = [1, 1.5, 2.5, 3.5, 4.5, 5]

    for _u in users:
        for i in range(5):
            if (_u.avg_rating and _u.avg_rating > grade[i] and
                    _u.avg_rating <= grade[i+1]):
                users_per_grade[i] += 1


    return (number_of_users,
            new_users,
            requests_created,
            finished_requests,
            tasks_per_service,
            user_service_per_service,
            achievements_distribution,
            users_per_grade)


# =================== NOTIFICATION ===================
# ====================================================
def send_notification(user, notification):
    if user:
        if FCMDevice.objects.get(user=user).count:
            device = FCMDevice.objects.get(user=user)
            fulluser = models.FullUser.objects.get(user__id=user.id)
            fulluser.notifications.add(notification)
            fulluser.save()
    else:
        device = FCMDevice.objects.all()
        for _d in device:
            fulluser = models.FullUser.objects.get(user__id=_d.user.id)
            fulluser.notifications.add(notification)
            fulluser.save()

    device.send_message(title=notification.title, body=notification.body,
                        data={'notification_type' : notification.notification_type,
                              'type_id' : notification.type_id})


# =================== ACHIEVEMENT ===================
# ===================================================
def check_achievements(user):
    c = [0, 0, 0, 0, 0, 0, 0, 0, 0]

    c[0] = len(user.requests.all())
    c[2] = user.user.avg_rating
    c[5] = len(user.benefitlist.all())

    done_req = list()
    reqs = models.Request.objects.all()
    for _r in reqs:
        if _r.working_with.id == user.user.id:
            done_req.append(_r)

    services = set()
    for _r in done_req:
        if _r.status == 2:
            c[3] += 1
        elif _r.status == 3:
            c[6] += 1
        for _t in _r.tasklist.all():
            services.add(_t.service_type.id)
    c[7] = len(services)

    services.clear()
    for _r in user.requests.all():
        if _r.status == 2:
            c[4] += 1
        elif _r.status == 3:
            c[6] += 1
        for _t in _r.tasklist.all():
            services.add(_t.service_type.id)
    c[8] = len(services)

    c[1] = c[3] + c[4]

    achievements = models.Achievement.objects.all()
    for ach in achievements:
        level = 1000
        for cond in ach.conditions.all():
            lvl = 0
            for _cn in cond.condition_numbers:
                if _cn == 7:
                    if _cn >= c[6]:
                        lvl += 1
                elif _cn <= c[cond.codition - 1]:
                    lvl += 1

            if lvl < level:
                level = lvl

        if level > 0:
            achlvl = None
            for _a in user.achievements.all():
                if _a.achievement.id == ach.id:
                    achlvl = _a

            if not achlvl:
                achlvl = models.AchievementLevel(level=level,
                                                 achievement=ach,
                                                 user=user)
                achlvl.save()
                user.achievements.add(achlvl)
                user.save()
            else:
                achlvl.level = level
                achlvl.save()

            # send notification NEW_ACHIEVEMENT
            # TODO: Change strings (?)
            title = 'New achievement'
            body = 'You have unlocked new achievement!'
            notif = parsers.create_notification(title, body, 4, achlvl.id)
            send_notification(user.user, notif)
