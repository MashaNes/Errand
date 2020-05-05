import base64
import os.path
from . import models

PIC_PATH = 'db/images/'

def encode_img(img_path):
    ext = ".png"
    path = PIC_PATH + img_path + ext

    if not os.path.exists(path):
        return None

    with open(path, 'rb') as img_file:
        base64_string = str(base64.b64encode(img_file.read()))
        base64_string = "data:image/png;base64," + base64_string[2:-1]
    return base64_string

def calc_rating(user):
    avg = 0

    if user.ratings.all():
        for _r in user.ratings.all():
            avg += _r
        avg /= len(user.ratings.all())
    return avg

def filter_user(queryset, data):
    new_queryset = list()
    user = models.FullUser.objects.get(id=data['created_by'])

    for _q in queryset:
        to_add = True
        if user.id == _q.id:
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
                        if data['no_rating'] or _s['min_rating'] <= calc_rating(_q):
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

        # TODO: Add other filter params if needed

        if to_add:
            new_queryset.append(_q)

    queryset = new_queryset
    new_queryset = list()

    for _q in queryset:
        new_queryset.append(models.User.objects.get(id=_q.user.id))

    return new_queryset
