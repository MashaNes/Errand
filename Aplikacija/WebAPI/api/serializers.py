from rest_framework import serializers
from rest_framework.authtoken.models import Token
from . import models
from . import parsers
from . import utils

class PictureSerializer(serializers.ModelSerializer):
    class Meta:
        model = models.Picture
        fields = '__all__'

class WorkingHourSerializer(serializers.ModelSerializer):
    class Meta:
        model = models.WorkingHour
        fields = '__all__'

class AddressSerializer(serializers.ModelSerializer):
    class Meta:
        model = models.Address
        fields = '__all__'

class ServiceSerializer(serializers.ModelSerializer):
    class Meta:
        model = models.Service
        fields = '__all__'

class UserServiceSerializer(serializers.ModelSerializer):
    service = ServiceSerializer()

    class Meta:
        model = models.UserService
        fields = '__all__'

class CheckListSerializer(serializers.ModelSerializer):
    class Meta:
        model = models.CheckList
        fields = '__all__'

class TaskSerializer(serializers.ModelSerializer):
    address = AddressSerializer()
    service_type = ServiceSerializer()
    checklist = CheckListSerializer(many=True)

    class Meta:
        model = models.Task
        fields = '__all__'

class TaskEditSerializer(serializers.ModelSerializer):
    task = TaskSerializer()
    address = AddressSerializer()

    class Meta:
        model = models.TaskEdit
        fields = '__all__'

class UserSerializer(serializers.ModelSerializer):
    picture = serializers.CharField()
    class Meta:
        model = models.User
        fields = ('id', 'email', 'first_name', 'last_name', 'phone',
                  'picture', 'avg_rating', 'min_rating', 'max_dist', 'is_admin',
                  'status', 'password', 'benefit_discount', 'benefit_requirement')
        extra_kwargs = {'password': {'write_only': True}}

class AchievementSerializer(serializers.ModelSerializer):
    class Meta:
        model = models.Achievement
        fields = '__all__'

class AchievementLevelSerializer(serializers.ModelSerializer):
    achievement = AchievementSerializer()
    user = UserSerializer()

    class Meta:
        model = models.AchievementLevel
        fields = '__all__'

class BenefitSerializer(serializers.ModelSerializer):
    benefit_user = UserSerializer()

    class Meta:
        model = models.Benefit
        fields = '__all__'

class ReportSerializer(serializers.ModelSerializer):
    reported_user = UserSerializer()
    created_by = UserSerializer()

    class Meta:
        model = models.Report
        fields = '__all__'

class BannedSerializer(serializers.ModelSerializer):
    banned_user = UserSerializer()

    class Meta:
        model = models.Banned
        fields = '__all__'

class RequestSerializer(serializers.ModelSerializer):
    created_by = UserSerializer()
    tasklist = TaskSerializer(many=True)

    class Meta:
        model = models.Request
        fields = '__all__'

class RequestEditSerializer(serializers.ModelSerializer):
    class Meta:
        model = models.RequestEdit
        fields = '__all__'

class OfferSerializer(serializers.ModelSerializer):
    created_by = UserSerializer()
    request = RequestSerializer()
    edit = RequestEditSerializer()

    class Meta:
        model = models.Offer
        fields = '__all__'

class NotificationSerializer(serializers.ModelSerializer):
    class Meta:
        model = models.Notification
        fields = '__all__'

class RatingSerializer(serializers.ModelSerializer):
    created_by = UserSerializer()
    rated_user = UserSerializer()
    request = RequestSerializer()

    class Meta:
        model = models.Rating
        fields = '__all__'

class FullUserSerializer(serializers.ModelSerializer):
    user = UserSerializer()
    blocked = UserSerializer(many=True)
    working_hours = WorkingHourSerializer(many=True)
    addresses = AddressSerializer(many=True)
    services = UserServiceSerializer(many=True)
    offers = OfferSerializer(many=True)
    notifications = NotificationSerializer(many=True)
    ratings = RatingSerializer(many=True)
    benefitlist = BenefitSerializer(many=True)
    achievements = AchievementLevelSerializer(many=True)
    requests = RequestSerializer(many=True)

    class Meta:
        model = models.FullUser
        fields = '__all__'

class FullRequestSerializer(serializers.ModelSerializer):
    request = RequestSerializer()
    offers = OfferSerializer(many=True)
    accepted_offer = OfferSerializer()
    rating = RatingSerializer()

    class Meta:
        model = models.FullRequest
        fields = '__all__'
