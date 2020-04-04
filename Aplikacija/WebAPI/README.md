# Instaliranje i pokretanje backend-a #


### Instaliranje potrebnih paketa za rad servera: ###

Instalirati XAMPP:
https://www.apachefriends.org/index.html
U web browser-u ukucati localhost/phpmyadmin i napraviti novu bazu pod nazivom errand
(U si.20.30.errand/Aplikacija/WebAPI/errand/settings.py se u DATEBASES u NAME postavlja ime baze, ako zelite da pomenite, da imate vise za testiranje ili slicno)

Instalirati Python >=3.5 (najbolje najnoviji):
https://www.python.org/

Instalirati pip (Python Package Manager, uglavnom vec instaliran uz python) i venv:
https://packaging.python.org/guides/installing-using-pip-and-virtual-environments/

Napraviti virtual environment (najbolje u si.20.30.errand/Aplikacija/WebAPI, navigirati u terminalu na zeljenu lokaciju):
```
$ python3 -m venv env
```

Aktivirati venv (mora se aktivirati svaki put pre pokretanja servera):
```
$ source env/bin/activate
```

Instalirati potrebne pakete preko pip-a unutar napravljenog venv:
```
$ pip install django djangorestframework mysqlclient pygments pillow pylint djoser django-filter drf-extensions django-rest-swagger
```


### Pokretanje backend servera ###

Pokrenuti XAMPP:
```
$ /opt/lampp/xampp start
```
ponekad je potrebno pre ovoga stopirati service mysql:
```
$ service mysql stop
```
(za windows pogledati kako se pokrece)

Aktivirati venv u kome su instalirani paketi:
```
$ source env/bin/activate
```

U folderu si.20.30.errand/Aplikacija/WebAPI/ napraviti migraciju i pokrenuti server:
```
$ python manage.py makemigrations
$ python manage.py migrate
$ python manage.py runserver
```
