import base64
import os.path
from . import models

PIC_PATH = 'db/images/'

def encode_img(img_path):
    ext = ".png"
    path = PIC_PATH + img_path + ext
    base64_string = None

    if not os.path.exists(path):
        return None

    with open(path, 'rb') as img_file:
        base64_string = str(base64.b64encode(img_file.read()))
        base64_string = base64_string[2:-1]
    return base64_string

# =================== USER ===================
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

        if to_add and data['services']:
            for _s in data['services']:
                found = False
                for _q_s in _q.services.all():
                    if (_s['id'] == _q_s.service.id and _s['max_dist'] >= _q_s.max_dist):
                        if data['no_rating'] or _s['min_rating'] <= _q.avg_rating:
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
            to_add = False

        if to_add and data['rating_limit_up']:
            if _q.user.avg_rating > data['rating_limit_up']:
                to_add = False

        if to_add and data['rating_limit_down']:
            if _q.user.avg_rating < data['rating_limit_down']:
                to_add = False

        # TODO: Add other filter params if needed

        if to_add:
            new_queryset.append(_q)

    queryset = new_queryset
    new_queryset = list()

    for _q in queryset:
        new_queryset.append(models.User.objects.get(id=_q.user.id))

    return new_queryset


def filter_user_info(serializer, data):
    response = {}

    if data['blocked']:
        response['blocked'] = serializer['blocked']
    if data['working_hours']:
        response['working_hours'] = serializer['working_hours']
    if data['addresses']:
        response['addresses'] = serializer['addresses']
    if data['services']:
        response['services'] = serializer['services']
    if data['offers']:
        response['offers'] = serializer['offers']
    if data['notifications']:
        response['notifications'] = serializer['notifications']
    if data['ratings']:
        response['ratings'] = serializer['ratings']
    if data['benefitlist']:
        response['benefitlist'] = serializer['benefitlist']
    if data['achievements']:
        response['achievements'] = serializer['achievements']
        for _r, _a in zip(response['achievements'], serializer['achievements']):
            _r['picture'] = encode_img('achievements/' + str(_a.name))
    if data['requests']:
        response['requests'] = serializer['requests']

    return response

# ==================== REQUEST ====================
def filter_requests(queryset, data):
    new_queryset = list()

    for _q in queryset:
        to_add = True

        if data['created_by']:
            if _q.request.created_by.id != data['created_by']:
                to_add = False

        if to_add and data['done_by']:
            if not _q.accepted_offer:
                to_add = False
            elif _q.accepted_offer.created_by.id != data['done_by']:
                to_add = False

        if to_add and data['created_or_done_by']:
            found = False
            if _q.request.created_by.id == data['created_or_done_by']:
                found = True
            if (_q.accepted_offer and
                    _q.accepted_offer.created_by.id == data['created_or_done_by']):
                found = True
            to_add = found

        if to_add and data['statuses']:
            found = False
            for _s in data['statuses']:
                if _s == _q.request.status:
                    found = True
                    break
            to_add = found

        if to_add and data['unrated']:
            if _q.rating:
                to_add = False

        # TODO: Add other filter params if needed

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
    if data['accepted_offer']:
        response['accepted_offer'] = serializer['accepted_offer']
    if data['rating']:
        response['rating'] = serializer['rating']

    return response
