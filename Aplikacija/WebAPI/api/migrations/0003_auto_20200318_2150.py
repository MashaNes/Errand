# Generated by Django 3.0.4 on 2020-03-18 21:50

from django.conf import settings
from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0002_auto_20200312_2150'),
    ]

    operations = [
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
            name='FullUser',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('benefit_discount', models.FloatField(blank=True, null=True)),
                ('benefit_requirement', models.IntegerField(blank=True, null=True)),
                ('achievements', models.ManyToManyField(to='api.AchievementLevel')),
                ('addresses', models.ManyToManyField(blank=True, to='api.Address')),
                ('benefitlist', models.ManyToManyField(to='api.Benefit')),
            ],
        ),
        migrations.RemoveField(
            model_name='extrauser',
            name='achievements',
        ),
        migrations.RemoveField(
            model_name='extrauser',
            name='benefitlist',
        ),
        migrations.RemoveField(
            model_name='extrauser',
            name='blocked',
        ),
        migrations.RemoveField(
            model_name='extrauser',
            name='notifications',
        ),
        migrations.RemoveField(
            model_name='extrauser',
            name='offers',
        ),
        migrations.RemoveField(
            model_name='extrauser',
            name='ratings',
        ),
        migrations.RemoveField(
            model_name='extrauser',
            name='requests',
        ),
        migrations.RemoveField(
            model_name='extrauser',
            name='user',
        ),
        migrations.RemoveField(
            model_name='user',
            name='addresses',
        ),
        migrations.RemoveField(
            model_name='user',
            name='benefit_discount',
        ),
        migrations.RemoveField(
            model_name='user',
            name='benefit_requirement',
        ),
        migrations.RemoveField(
            model_name='user',
            name='services',
        ),
        migrations.RemoveField(
            model_name='user',
            name='working_hours',
        ),
        migrations.DeleteModel(
            name='ExtraRequest',
        ),
        migrations.DeleteModel(
            name='ExtraUser',
        ),
        migrations.AddField(
            model_name='fulluser',
            name='blocked',
            field=models.ManyToManyField(related_name='blocked_id', to=settings.AUTH_USER_MODEL),
        ),
        migrations.AddField(
            model_name='fulluser',
            name='notifications',
            field=models.ManyToManyField(to='api.Notification'),
        ),
        migrations.AddField(
            model_name='fulluser',
            name='offers',
            field=models.ManyToManyField(to='api.Offer'),
        ),
        migrations.AddField(
            model_name='fulluser',
            name='ratings',
            field=models.ManyToManyField(to='api.Rating'),
        ),
        migrations.AddField(
            model_name='fulluser',
            name='requests',
            field=models.ManyToManyField(to='api.Request'),
        ),
        migrations.AddField(
            model_name='fulluser',
            name='services',
            field=models.ManyToManyField(blank=True, to='api.UserService'),
        ),
        migrations.AddField(
            model_name='fulluser',
            name='user',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='user_id', to=settings.AUTH_USER_MODEL),
        ),
        migrations.AddField(
            model_name='fulluser',
            name='working_hours',
            field=models.ManyToManyField(blank=True, to='api.WorkingHour'),
        ),
    ]
