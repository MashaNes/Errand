# Generated by Django 3.0.5 on 2020-05-08 01:09

from django.conf import settings
import django.contrib.auth.models
import django.contrib.auth.validators
from django.db import migrations, models
import django.db.models.deletion
import django.utils.timezone


class Migration(migrations.Migration):

    initial = True

    dependencies = [
        ('auth', '0011_update_proxy_permissions'),
    ]

    operations = [
        migrations.CreateModel(
            name='User',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('password', models.CharField(max_length=128, verbose_name='password')),
                ('last_login', models.DateTimeField(blank=True, null=True, verbose_name='last login')),
                ('is_superuser', models.BooleanField(default=False, help_text='Designates that this user has all permissions without explicitly assigning them.', verbose_name='superuser status')),
                ('username', models.CharField(error_messages={'unique': 'A user with that username already exists.'}, help_text='Required. 150 characters or fewer. Letters, digits and @/./+/-/_ only.', max_length=150, unique=True, validators=[django.contrib.auth.validators.UnicodeUsernameValidator()], verbose_name='username')),
                ('first_name', models.CharField(blank=True, max_length=30, verbose_name='first name')),
                ('last_name', models.CharField(blank=True, max_length=150, verbose_name='last name')),
                ('is_staff', models.BooleanField(default=False, help_text='Designates whether the user can log into this admin site.', verbose_name='staff status')),
                ('is_active', models.BooleanField(default=True, help_text='Designates whether this user should be treated as active. Unselect this instead of deleting accounts.', verbose_name='active')),
                ('date_joined', models.DateTimeField(default=django.utils.timezone.now, verbose_name='date joined')),
                ('is_admin', models.BooleanField(default=False)),
                ('email', models.EmailField(max_length=254, unique=True)),
                ('phone', models.CharField(max_length=16)),
                ('picture', models.ImageField(blank=True, null=True, upload_to='')),
                ('avg_rating', models.FloatField(blank=True, null=True)),
                ('min_rating', models.FloatField(default=1, null=True)),
                ('max_dist', models.FloatField(default=1000, null=True)),
                ('status', models.IntegerField(choices=[('offline', 0), ('online', 1)], default=0, null=True)),
                ('benefit_discount', models.FloatField(default=0.1, null=True)),
                ('benefit_requirement', models.IntegerField(default=5, null=True)),
                ('groups', models.ManyToManyField(blank=True, help_text='The groups this user belongs to. A user will get all permissions granted to each of their groups.', related_name='user_set', related_query_name='user', to='auth.Group', verbose_name='groups')),
                ('user_permissions', models.ManyToManyField(blank=True, help_text='Specific permissions for this user.', related_name='user_set', related_query_name='user', to='auth.Permission', verbose_name='user permissions')),
            ],
            options={
                'verbose_name': 'user',
                'verbose_name_plural': 'users',
                'abstract': False,
            },
            managers=[
                ('objects', django.contrib.auth.models.UserManager()),
            ],
        ),
        migrations.CreateModel(
            name='Achievement',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name_sr', models.CharField(max_length=50)),
                ('name_en', models.CharField(max_length=50)),
                ('description_sr', models.CharField(max_length=256)),
                ('description_en', models.CharField(max_length=256)),
                ('icon', models.ImageField(upload_to='')),
                ('requirements', models.CharField(max_length=256)),
            ],
        ),
        migrations.CreateModel(
            name='AchievementLevel',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('level', models.IntegerField()),
                ('achievement', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='api.Achievement')),
                ('user', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to=settings.AUTH_USER_MODEL)),
            ],
        ),
        migrations.CreateModel(
            name='Address',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=50)),
                ('longitude', models.FloatField()),
                ('latitude', models.FloatField()),
                ('home', models.BooleanField(default=False)),
                ('arrived', models.BooleanField(default=False)),
            ],
        ),
        migrations.CreateModel(
            name='Benefit',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('discount', models.FloatField()),
                ('benefit_user', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='benefit_user_id', to=settings.AUTH_USER_MODEL)),
            ],
        ),
        migrations.CreateModel(
            name='CheckList',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('check_list', models.CharField(max_length=100)),
            ],
        ),
        migrations.CreateModel(
            name='Notification',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('title', models.CharField(max_length=50)),
                ('body', models.CharField(max_length=256)),
                ('notification_type', models.IntegerField(choices=[('request', 0), ('offer', 1), ('edit_request', 2), ('rating', 3), ('achievement', 4)])),
                ('timestamp', models.DateTimeField(auto_now=True)),
                ('type_id', models.IntegerField()),
            ],
        ),
        migrations.CreateModel(
            name='Picture',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('picture', models.ImageField(upload_to='')),
            ],
        ),
        migrations.CreateModel(
            name='Request',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=50)),
                ('status', models.IntegerField(choices=[('pending', 0), ('active', 1), ('finished', 2)], default=0)),
                ('location_status', models.IntegerField(choices=[], default=0)),
                ('time', models.DateTimeField(auto_now=True)),
                ('picture_required', models.BooleanField(default=False)),
                ('note', models.CharField(max_length=256)),
                ('max_dist', models.FloatField()),
                ('min_rating', models.FloatField()),
                ('created_by', models.ForeignKey(null=True, on_delete=django.db.models.deletion.CASCADE, to=settings.AUTH_USER_MODEL)),
                ('pictures', models.ManyToManyField(to='api.Picture')),
            ],
        ),
        migrations.CreateModel(
            name='Service',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('service_type_sr', models.CharField(max_length=50)),
                ('service_type_en', models.CharField(max_length=50)),
                ('description_sr', models.CharField(max_length=256)),
                ('description_en', models.CharField(max_length=256)),
                ('picture_required', models.BooleanField(default=False)),
            ],
        ),
        migrations.CreateModel(
            name='Task',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=50)),
                ('description', models.CharField(max_length=256)),
                ('picture_required', models.BooleanField(default=False)),
                ('address', models.ForeignKey(null=True, on_delete=django.db.models.deletion.SET_NULL, to='api.Address')),
                ('checklist', models.ManyToManyField(to='api.CheckList')),
                ('pictures', models.ManyToManyField(to='api.Picture')),
                ('service_type', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='api.Service')),
            ],
        ),
        migrations.CreateModel(
            name='WorkingHour',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('work_from', models.DateTimeField()),
                ('work_until', models.DateTimeField()),
            ],
        ),
        migrations.CreateModel(
            name='UserService',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('max_dist', models.FloatField()),
                ('payment_type', models.IntegerField(default=0)),
                ('payment_ammount', models.FloatField(default=0)),
                ('min_rating', models.FloatField(default=1)),
                ('service', models.ForeignKey(null=True, on_delete=django.db.models.deletion.CASCADE, to='api.Service')),
            ],
        ),
        migrations.CreateModel(
            name='TaskEdit',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('address', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='api.Address')),
                ('task', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='api.Task')),
            ],
        ),
        migrations.CreateModel(
            name='RequestEdit',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('time', models.DateTimeField()),
                ('request', models.ManyToManyField(to='api.Request')),
                ('tasks', models.ManyToManyField(to='api.TaskEdit')),
            ],
        ),
        migrations.AddField(
            model_name='request',
            name='tasklist',
            field=models.ManyToManyField(to='api.Task'),
        ),
        migrations.CreateModel(
            name='Report',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('comment', models.CharField(max_length=256)),
                ('created_by', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='report_created_by_id', to=settings.AUTH_USER_MODEL)),
                ('reported_user', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='report_reported_user_id', to=settings.AUTH_USER_MODEL)),
            ],
        ),
        migrations.CreateModel(
            name='Rating',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('grade', models.FloatField()),
                ('comment', models.CharField(max_length=256)),
                ('created_by', models.ForeignKey(null=True, on_delete=django.db.models.deletion.SET_NULL, related_name='rating_created_by_id', to=settings.AUTH_USER_MODEL)),
                ('rated_user', models.ForeignKey(null=True, on_delete=django.db.models.deletion.SET_NULL, related_name='rating_rated_user_id', to=settings.AUTH_USER_MODEL)),
                ('request', models.ForeignKey(null=True, on_delete=django.db.models.deletion.CASCADE, to='api.Request')),
            ],
        ),
        migrations.CreateModel(
            name='Offer',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('payment_type', models.CharField(max_length=4)),
                ('payment_ammount', models.FloatField()),
                ('created_by', models.ForeignKey(null=True, on_delete=django.db.models.deletion.SET_NULL, to=settings.AUTH_USER_MODEL)),
                ('edit', models.ForeignKey(null=True, on_delete=django.db.models.deletion.SET_NULL, to='api.RequestEdit')),
                ('request', models.ForeignKey(null=True, on_delete=django.db.models.deletion.SET_NULL, to='api.Request')),
            ],
        ),
        migrations.CreateModel(
            name='FullUser',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('achievements', models.ManyToManyField(to='api.AchievementLevel')),
                ('addresses', models.ManyToManyField(blank=True, to='api.Address')),
                ('benefitlist', models.ManyToManyField(to='api.Benefit')),
                ('blocked', models.ManyToManyField(related_name='blocked_id', to=settings.AUTH_USER_MODEL)),
                ('notifications', models.ManyToManyField(to='api.Notification')),
                ('offers', models.ManyToManyField(to='api.Offer')),
                ('ratings', models.ManyToManyField(to='api.Rating')),
                ('requests', models.ManyToManyField(to='api.Request')),
                ('services', models.ManyToManyField(blank=True, to='api.UserService')),
                ('user', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='user_id', to=settings.AUTH_USER_MODEL)),
                ('working_hours', models.ManyToManyField(blank=True, to='api.WorkingHour')),
            ],
        ),
        migrations.CreateModel(
            name='FullRequest',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('accepted_offer', models.ForeignKey(null=True, on_delete=django.db.models.deletion.SET_NULL, related_name='accepted_offer_id', to='api.Offer')),
                ('offers', models.ManyToManyField(related_name='offers_id', to='api.Offer')),
                ('rating', models.ForeignKey(null=True, on_delete=django.db.models.deletion.SET_NULL, to='api.Rating')),
                ('request', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='request_id', to='api.Request')),
            ],
        ),
        migrations.CreateModel(
            name='Banned',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('until', models.DateTimeField()),
                ('comment', models.CharField(max_length=256)),
                ('banned_user', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to=settings.AUTH_USER_MODEL)),
            ],
        ),
    ]
