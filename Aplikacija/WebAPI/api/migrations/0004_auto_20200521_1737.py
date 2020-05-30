# Generated by Django 3.0.4 on 2020-05-21 17:37

from django.conf import settings
from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0003_auto_20200521_1728'),
    ]

    operations = [
        migrations.CreateModel(
            name='Edit',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('created_by', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='created_by_id', to=settings.AUTH_USER_MODEL)),
                ('request_edit', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='api.RequestEdit')),
                ('working_with', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='working_with_id', to=settings.AUTH_USER_MODEL)),
            ],
        ),
        migrations.AlterField(
            model_name='fullrequest',
            name='edits',
            field=models.ManyToManyField(to='api.Edit'),
        ),
    ]