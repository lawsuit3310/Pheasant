#임포트 안되면 인터프립터 설정 다시해보기
from urllib import request
import firebase_admin
import time
from firebase_admin import credentials
from firebase_admin import db
from nbformat import current_nbformat

cred = credentials.Certificate('la-prova-firebase-adminsdk-9huj0-3cf67c4d09.json')
firebase_admin.initialize_app(cred,{'databaseURL':'https://la-prova-default-rtdb.firebaseio.com/'})

dir = db.reference()
#확인 해보니까 dir.get()은 딕셔너리형으로 반환됨.
#print(type(dir.get()))

#기기의 SSAID를 받아와 저장할 리스트
users = list(dir.get().keys())
#각 기기들의 요청을 가져와 저장할 리스트
requests = list(dir.get().values())

print(requests)

try:
    while 1:
        dir = db.reference()
        currentU = users
        currentR = requests
        result = '' #결과를 담을 변수

        users = list(dir.get().keys())
        requests = list(dir.get().values())

        if users != currentU:
            if users > currentU:
                #새로운 유저에게서 요청이 들어온 상황이므로 구현 바람.
                for userC in currentU:
                    if userC in users:
                        users.remove(userC)
                result = dir.get()["".join(users)] #새로운 유저가 보낸 요청을 받아 저장
            else: 
                #기존 데이터가 삭제 되었을 경우
                print("값이 잘못된 경로를 통하여 변경되었습니다.\n프로그램을 종료합니다.")
                #exit()

        elif requests != currentR:
            #기존 유저가 다른 요청을 보냄
            for req in requests:
                print(True)
                for requestC in currentR:
                    if req != requestC: #다른 부분 찾아내서 반복문
                        result = req
                        break
                if result != '':
                    break

            print(result)

        time.sleep(0.1)#0.1초 마다 호출

except KeyboardInterrupt:
    exit()
    
