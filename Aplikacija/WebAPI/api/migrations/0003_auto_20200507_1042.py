# Generated by Django 3.0.4 on 2020-05-07 10:42

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0002_auto_20200507_0955'),
    ]

    operations = [
        migrations.RenameField(
            model_name='service',
            old_name='service_type_rs',
            new_name='service_type_sr',
        ),
    ]