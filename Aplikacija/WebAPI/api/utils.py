import base64
import os.path
from . import models

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
    response = {}

    if data['blocked']:
        response['blocked'] = serializer['blocked']
        for _r in response['blocked']:
            _r['picture'] = load_img(_r['picture'])

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
        for _r in response['ratings']:
            _r['created_by']['picture'] = load_img(_r['created_by']['picture'])
            _r['rated_user']['picture'] = load_img(_r['rated_user']['picture'])

    if data['benefitlist']:
        response['benefitlist'] = serializer['benefitlist']
        for _r in response['benefitlist']:
            _r['benefit_user']['picture'] = load_img(_r['benefit_user']['picture'])

    if data['achievements']:
        response['achievements'] = serializer['achievements']
        for _r in response['achievements']:
            _r['icon'] = load_img(_r['icon'])

    if data['requests']:
        response['requests'] = serializer['requests']

    return response

# ==================== REQUEST ====================
# =================================================
def filter_requests(queryset, data):
    new_queryset = list()

    for _q in queryset:
        to_add = True

        if data['created_by']:
            if _q.request.created_by.id != data['created_by']:
                to_add = False
            if data['unrated'] and _q.request.rated_working_with:
                to_add = False

        if to_add and data['done_by']:
            if (_q.request.working_with and
                    _q.request.working_with.id != data['done_by']):
                to_add = False
            if data['unrated'] and _q.request.rated_created_by:
                to_add = False

        if to_add and data['created_or_done_by']:
            found = False

            if _q.request.created_by.id == data['created_or_done_by']:
                if not data['unrated']:
                    found = True
                if data['unrated'] and not _q.request.rated_working_with:
                    found = True

            if (_q.request.working_with and
                    _q.request.working_with.id == data['created_or_done_by']):
                if not data['unrated']:
                    found = True
                if data['unrated'] and not _q.request.rated_created_by:
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
