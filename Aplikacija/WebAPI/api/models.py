from django.db import models
from django.contrib.auth.models import AbstractUser

class Picture(models.Model):
    picture = models.ImageField()

class CheckList(models.Model):
    check_list = models.CharField(max_length=100)

class WorkingHour(models.Model):
    # DONE: Change to DateField
    work_from = models.DateTimeField()
    work_until = models.DateTimeField()

class Address(models.Model):
    name = models.CharField(max_length=50)
    longitude = models.FloatField()
    latitude = models.FloatField()
    home = models.BooleanField(default=False)
    arrived = models.BooleanField(default=False)

class Service(models.Model):
    # DONE: Add sr and en version of service_type
    service_type_sr = models.CharField(max_length=50)
    service_type_en = models.CharField(max_length=50)
    # DONE: Add sr and en version of description
    description_sr = models.CharField(max_length=256)
    description_en = models.CharField(max_length=256)
    picture_required = models.BooleanField(default=False)

class UserService(models.Model):
    max_dist = models.FloatField()
    # DONE: Change payment_type to IntegerField
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
    # DONE: Added is_admin for admin users
    is_admin = models.BooleanField(default=False)
    email = models.EmailField(unique=True)
    phone = models.CharField(max_length=16)
    picture = models.ImageField(null=True, blank=True)
    avg_rating = models.FloatField(null=True, blank=True)
    min_rating = models.FloatField(null=True, default=1)
    max_dist = models.FloatField(null=True, default=1000)
    status = models.IntegerField(null=True, default=0, choices=STATUS_TYPES)
    # DONE: Add benefit_discount, benefit_requirement
    benefit_discount = models.FloatField(null=True, default=0.1)
    benefit_requirement = models.IntegerField(null=True, default=5)

class Achievement(models.Model):
    # DONE: Add sr and en version of name
    name_sr = models.CharField(max_length=50)
    name_en = models.CharField(max_length=50)
    # DONE: Add sr and en version of description
    description_sr = models.CharField(max_length=256)
    description_en = models.CharField(max_length=256)
    icon = models.ImageField()
    requirements = models.CharField(max_length=256)

class AchievementLevel(models.Model):
    level = models.IntegerField()
    achievement = models.ForeignKey(Achievement, on_delete=models.CASCADE)
    user = models.ForeignKey(User, on_delete=models.CASCADE)

class Benefit(models.Model):
    discount = models.FloatField()
    benefit_user = models.ForeignKey(User, related_name='benefit_user_id',
                                     on_delete=models.CASCADE)

class Report(models.Model):
    # DONE: Delete report_type
    comment = models.CharField(max_length=256)
    reported_user = models.ForeignKey(User, related_name='report_reported_user_id',
                                      on_delete=models.CASCADE)
    created_by = models.ForeignKey(User, related_name='report_created_by_id',
                                   on_delete=models.CASCADE)

class Banned(models.Model):
    until = models.DateTimeField()
    comment = models.CharField(max_length=256)
    banned_user = models.ForeignKey(User, on_delete=models.CASCADE)

class Request(models.Model):
    STATUS_TYPES = (('pending', 0), ('active', 1), ('finished', 2)) #TODO: Add types
    LOCATION_STATUS_TYPES = () #TODO: Add types

    name = models.CharField(max_length=50)
    status = models.IntegerField(default=0, choices=STATUS_TYPES)
    location_status = models.IntegerField(default=0, choices=LOCATION_STATUS_TYPES)
    time = models.DateTimeField(auto_now=True)
    picture_required = models.BooleanField(default=False)
    pictures = models.ManyToManyField(Picture)
    note = models.CharField(max_length=256)
    # DONE: Delete request_type
    tasklist = models.ManyToManyField(Task)
    max_dist = models.FloatField()
    min_rating = models.FloatField()
    # DONE: Delete service_type
    created_by = models.ForeignKey(User, null=True, on_delete=models.CASCADE)

class RequestEdit(models.Model):
    time = models.DateTimeField()
    tasks = models.ManyToManyField(TaskEdit)
    request = models.ManyToManyField(Request)

class Offer(models.Model):
    payment_type = models.CharField(max_length=4)
    payment_ammount = models.FloatField()
    created_by = models.ForeignKey(User, null=True, on_delete=models.SET_NULL)
    request = models.ForeignKey(Request, null=True, on_delete=models.SET_NULL)
    edit = models.ForeignKey(RequestEdit, null=True, on_delete=models.SET_NULL)

class Notification(models.Model):
    NOTIFICATION_TYPES = (('request', 0), ('offer', 1), ('edit_request', 2), 
                          ('rating', 3), ('achievement', 4))

    title = models.CharField(max_length=50)
    body = models.CharField(max_length=256)
    notification_type = models.IntegerField(choices=NOTIFICATION_TYPES)
    timestamp = models.DateTimeField(auto_now=True)
    type_id = models.IntegerField()

class Rating(models.Model):
    grade = models.FloatField()
    comment = models.CharField(max_length=256)
    created_by = models.ForeignKey(User, related_name='rating_created_by_id', null=True,
                                   on_delete=models.SET_NULL)
    rated_user = models.ForeignKey(User, related_name='rating_rated_user_id', null=True,
                                   on_delete=models.SET_NULL)
    request = models.ForeignKey(Request, null=True, on_delete=models.CASCADE)

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
    # DONE: Move benefit_discount, benefit_requirement to User

class FullRequest(models.Model):
    request = models.ForeignKey(Request, related_name='request_id', on_delete=models.CASCADE)
    offers = models.ManyToManyField(Offer, related_name='offers_id')
    accepted_offer = models.ForeignKey(Offer, related_name='accepted_offer_id', null=True,
                                       on_delete=models.SET_NULL)
    rating = models.ForeignKey(Rating, null=True, on_delete=models.SET_NULL)
