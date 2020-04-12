# Generated by Django 3.0.4 on 2020-03-18 21:52

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0003_auto_20200318_2150'),
    ]

    operations = [
        migrations.AlterField(
            model_name='fulluser',
            name='services',
            field=models.ManyToManyField(blank=True, related_name='service_id', to='api.UserService'),
        ),
    ]