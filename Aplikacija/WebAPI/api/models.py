from django.db import models
from django.utils.timezone import now
from django.contrib.auth.models import AbstractUser

class Picture(models.Model):
    picture = models.ImageField()

class Location(models.Model):
    longitude = models.FloatField()
    latitude = models.FloatField()

class CheckList(models.Model):
    check_list = models.CharField(max_length=100)

class ConditionNumber(models.Model):
    condition_number = models.FloatField(default=0)

class Condition(models.Model):
    condition = models.IntegerField(default=0)
    condition_numbers = models.ManyToManyField(ConditionNumber)

class WorkingHour(models.Model):
    # 0-mon, 1-tue, ...
    day = models.IntegerField(default=0)
    work_from = models.TimeField()
    work_until = models.TimeField()

class Address(models.Model):
    name = models.CharField(max_length=128)
    longitude = models.FloatField()
    latitude = models.FloatField()
    home = models.BooleanField(default=False)
    arrived = models.BooleanField(default=False)

class Service(models.Model):
    service_type_sr = models.CharField(max_length=50)
    service_type_en = models.CharField(max_length=50)
    description_sr = models.CharField(max_length=256)
    description_en = models.CharField(max_length=256)
    picture_required = models.BooleanField(default=False)

class UserService(models.Model):
    max_dist = models.FloatField()
    payment_type = models.IntegerField(default=0)
    payment_ammount = models.FloatField(default=0)
    min_rating = models.FloatField(default=1)
    service = models.ForeignKey(Service, null=True, on_delete=models.CASCADE)

class Task(models.Model):
    name = models.CharField(max_length=50)
    description = models.CharField(max_length=256)
    checklist = models.ManyToManyField(CheckList)
    address = models.ForeignKey(Address, null=True, on_delete=models.SET_NULL)
    picture_required = models.BooleanField(default=False)
    pictures = models.ManyToManyField(Picture)
    service_type = models.ForeignKey(Service, on_delete=models.CASCADE)

class TaskEdit(models.Model):
    task = models.ForeignKey(Task, on_delete=models.CASCADE)
    address = models.ForeignKey(Address, on_delete=models.CASCADE)

class User(AbstractUser):
    STATUS_TYPES = (("offline", 0), ("online", 1))

    is_admin = models.BooleanField(default=False)
    email = models.EmailField(unique=True)
    phone = models.CharField(max_length=16)
    picture = models.ImageField(null=True, blank=True)
    avg_rating = models.FloatField(null=True, blank=True)
    min_rating = models.FloatField(null=True, default=1)
    max_dist = models.FloatField(null=True, default=1000)
    status = models.IntegerField(null=True, default=0, choices=STATUS_TYPES)
    benefit_discount = models.FloatField(null=True, default=0.1)
    benefit_requirement = models.IntegerField(null=True, default=5)
    location = models.ForeignKey(Location, null=True, on_delete=models.SET_NULL)

class Achievement(models.Model):
    name_sr = models.CharField(max_length=50)
    name_en = models.CharField(max_length=50)
    description_sr = models.CharField(max_length=256)
    description_en = models.CharField(max_length=256)
    icon = models.ImageField()
    levels = models.IntegerField()
    conditions = models.ManyToManyField(Condition)

class AchievementLevel(models.Model):
    level = models.IntegerField()
    achievement = models.ForeignKey(Achievement, on_delete=models.CASCADE)
    user = models.ForeignKey(User, on_delete=models.CASCADE)

class Benefit(models.Model):
    discount = models.FloatField()
    benefit_user = models.ForeignKey(User, related_name='benefit_user_id',
                                     on_delete=models.CASCADE)

class Banned(models.Model):
    until = models.DateTimeField()
    comment = models.CharField(max_length=256)
    banned_user = models.ForeignKey(User, on_delete=models.CASCADE)
    total_bans = models.IntegerField(default=0)

class Request(models.Model):
    STATUS_TYPES = (('pending', 0), ('active', 1),
                    ('finished', 2), ('cancelled', 3))

    name = models.CharField(max_length=50)
    status = models.IntegerField(default=0, choices=STATUS_TYPES)
    location_status = models.IntegerField(default=0)
    time = models.DateTimeField(default=now)
    broadcast = models.BooleanField(default=False)
    rated_created_by = models.BooleanField(default=False)
    rated_working_with = models.BooleanField(default=False)
    finished_created_by = models.BooleanField(default=False)
    finished_working_with = models.BooleanField(default=False)
    picture_required = models.BooleanField(default=False)
    pictures = models.ManyToManyField(Picture)
    note = models.CharField(max_length=256)
    tasklist = models.ManyToManyField(Task)
    max_dist = models.FloatField()
    min_rating = models.FloatField()
    destination = models.ForeignKey(Address, null=True, on_delete=models.SET_NULL)
    created_by = models.ForeignKey(User, null=True, related_name='created_by',
                                   on_delete=models.CASCADE)
    working_with = models.ForeignKey(User, null=True, related_name='working_with',
                                     on_delete=models.SET_NULL)
    direct_user = models.ForeignKey(User, null=True, related_name='direct_user',
                                    on_delete=models.CASCADE)
    location = models.ForeignKey(Location, null=True, on_delete=models.SET_NULL,
                                 default=None)
    timestamp = models.DateTimeField(default=None, null=True)
    price = models.FloatField(default=0)

class RequestEdit(models.Model):
    time = models.DateTimeField(null=True, blank=True)
    tasks = models.ManyToManyField(TaskEdit)
    request = models.ForeignKey(Request, on_delete=models.CASCADE)

class Edit(models.Model):
    created_by = models.ForeignKey(User, related_name='created_by_id',
                                   on_delete=models.CASCADE)
    working_with = models.ForeignKey(User, related_name='working_with_id',
                                     on_delete=models.CASCADE)
    request_edit = models.ForeignKey(RequestEdit, on_delete=models.CASCADE)

class Report(models.Model):
    comment = models.CharField(max_length=256)
    request = models.ForeignKey(Request, null=True, blank=True,
                                on_delete=models.SET_NULL)
    pictures = models.ManyToManyField(Picture)
    status = models.IntegerField(default=0)
    # status -> 0, 1(b), 2(ignored), 3(b-reversed)
    reported_user = models.ForeignKey(User, related_name='report_reported_user_id',
                                      on_delete=models.CASCADE)
    created_by = models.ForeignKey(User, related_name='report_created_by_id',
                                   on_delete=models.CASCADE)

class Rating(models.Model):
    grade = models.FloatField()
    comment = models.CharField(max_length=256)
    created_by = models.ForeignKey(User, related_name='rating_created_by_id', null=True,
                                   on_delete=models.SET_NULL)
    rated_user = models.ForeignKey(User, related_name='rating_rated_user_id', null=True,
                                   on_delete=models.SET_NULL)
    request = models.ForeignKey(Request, null=True, on_delete=models.CASCADE)

class Offer(models.Model):
    payment_type = models.CharField(max_length=4)
    payment_ammount = models.FloatField()
    created_by = models.ForeignKey(User, null=True, on_delete=models.SET_NULL)
    request = models.ForeignKey(Request, null=True, on_delete=models.SET_NULL)
    edit = models.ForeignKey(RequestEdit, null=True, on_delete=models.SET_NULL)

class Notification(models.Model):
    NOTIFICATION_TYPES = (('req_direct', 0), ('req_failed', 1), ('offer_created', 2),
                          ('offer_accepted', 3), ('offer_canceled', 4), ('edit_created', 5),
                          ('edit_accepted', 6), ('edit_canceled', 7), ('rating', 8),
                          ('achievement', 9), ('req_success', 10))

    title_sr = models.CharField(max_length=50, null=True)
    body_sr = models.CharField(max_length=256, null=True)
    title_en = models.CharField(max_length=50, null=True)
    body_en = models.CharField(max_length=256, null=True)
    notification_type = models.IntegerField(choices=NOTIFICATION_TYPES)
    timestamp = models.DateTimeField(default=now)
    type_id = models.IntegerField()
    working_with = models.ForeignKey(User, null=True, default=None, on_delete=models.SET_NULL)
    seen = models.BooleanField(default=False)
    opened = models.BooleanField(default=False)

class FullUser(models.Model):
    user = models.ForeignKey(User, related_name='user_id', on_delete=models.CASCADE)
    working_hours = models.ManyToManyField(WorkingHour, blank=True)
    addresses = models.ManyToManyField(Address, blank=True)
    services = models.ManyToManyField(UserService, blank=True)
    blocked = models.ManyToManyField(User, related_name='blocked_id')
    offers = models.ManyToManyField(Offer)
    notifications = models.ManyToManyField(Notification)
    ratings = models.ManyToManyField(Rating)
    benefitlist = models.ManyToManyField(Benefit)
    achievements = models.ManyToManyField(AchievementLevel)
    requests = models.ManyToManyField(Request)
    ban = models.ForeignKey(Banned, null=True, blank=True, on_delete=models.SET_NULL)

class FullRequest(models.Model):
    request = models.ForeignKey(Request, related_name='request_id', on_delete=models.CASCADE)
    offers = models.ManyToManyField(Offer, related_name='offers_id')
    edits = models.ManyToManyField(Edit)
    accepted_offer = models.ForeignKey(Offer, related_name='accepted_offer',
                                       null=True, on_delete=models.SET_NULL)
    rating_created_by = models.ForeignKey(Rating, related_name='rating_created_by',
                                          null=True, on_delete=models.SET_NULL)
    rating_working_with = models.ForeignKey(Rating, related_name='rating_working_with',
                                            null=True, on_delete=models.SET_NULL)
