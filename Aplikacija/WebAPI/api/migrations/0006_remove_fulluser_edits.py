# Generated by Django 3.0.4 on 2020-05-21 20:03

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0005_auto_20200521_1945'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='fulluser',
            name='edits',
        ),
    ]