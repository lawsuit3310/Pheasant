#임포트 안되면 인터프립터 설정 다시해보기
import firebase_admin
import time
from firebase_admin import credentials
from firebase_admin import db

cred = credentials.Certificate('la-prova-firebase-adminsdk-9huj0-3cf67c4d09.json')
firebase_admin.initialize_app(cred,{'databaseURL':'https://la-prova-default-rtdb.firebaseio.com/'})

dir = db.reference()
#확인 해보니까 dir.get()은 딕셔너리형으로 반환됨.
#print(type(dir.get()))

#기기의 SSAID를 받아와 저장할 리스트
users = dir.get().keys()
#각 기기들의 요청을 가져와 저장할 리스트
requests = dir.get().values()

print(requests)



try:
    while 1:
        updateStatus()
        time.sleep(0.1)#0.1초 마다 호출

except KeyboardInterrupt:
    exit()

def updateStatus():
    dir = db.reference()

    currentU = users
    currentR = requests

    users = dir.get().keys()
    requests = dir.get().values()

    if users != currentU:
        pass
        #새로운 유저에게서 요청이 들어온 상황이므로 구현 바람.
    
    elif requests != currentR:
        pass
        #기존 유저가 다른 요청을 보냄
    
