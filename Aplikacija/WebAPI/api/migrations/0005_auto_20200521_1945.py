# Generated by Django 3.0.4 on 2020-05-21 19:45

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0004_auto_20200521_1737'),
    ]

    operations = [
        migrations.AddField(
            model_name='fulluser',
            name='ban',
            field=models.ForeignKey(blank=True, null=True, on_delete=django.db.models.deletion.SET_NULL, to='api.Banned'),
        ),
        migrations.AddField(
            model_name='fulluser',
            name='edits',
            field=models.ManyToManyField(to='api.Edit'),
        ),
    ]