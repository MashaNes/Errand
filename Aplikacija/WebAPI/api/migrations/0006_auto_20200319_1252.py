# Generated by Django 3.0.4 on 2020-03-19 12:52

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0005_delete_userblocked'),
    ]

    operations = [
        migrations.AlterField(
            model_name='fulluser',
            name='services',
            field=models.ManyToManyField(blank=True, to='api.UserService'),
        ),
    ]
