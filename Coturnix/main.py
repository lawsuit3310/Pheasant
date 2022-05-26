import firebase_admin
from firebase_admin import credentials
from firebase_admin import db

cred = credentials.Certificate('la-prova-firebase-adminsdk-9huj0-3cf67c4d09.json')
firebase_admin.initialize_app(cred,{'databaseURL':'https://console.firebase.google.com/u/0/project/la-prova/database'})

dir = db.reference()
print(dir.get())

try:
    while 1:
        pass

except KeyboardInterrupt:
    exit()
