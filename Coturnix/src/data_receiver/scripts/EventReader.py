#!/usr/bin/env python3
#임포트 안되면 인터프립터 설정 다시해보기
import unicodedata
import firebase_admin
import time
import json
import rospy
from std_msgs.msg import String
from firebase_admin import credentials
from firebase_admin import db

#절대 경로 사용 환경에 맞추어 수정 바람
directroy = '/home/lawsuit3310/Documents/Pheasant/Coturnix/src/data_receiver/scripts/'

with open (directroy + 'nodes.json') as f:
    nodes = json.load(f).values()
    #nodes는 List<List<Dictionary<string, int>>>형임

#잘 불러와짐
#print(nodes)

#초기설정
cred = credentials.Certificate(directroy + 'la-prova-firebase-adminsdk-9huj0-3cf67c4d09.json')
firebase_admin.initialize_app(cred,{'databaseURL':'https://la-prova-default-rtdb.firebaseio.com/'})

rospy.init_node('data_publisher')
pub = rospy.Publisher('data_publisher', String)
rate = rospy.Rate(2)


dir = db.reference()
#확인 해보니까 dir.get()은 딕셔너리형으로 반환됨.
#print(type(dir.get()))

#기기의 SSAID를 받아와 저장할 리스트
users = list(dir.get().keys())
#각 기기들의 요청을 가져와 저장할 리스트
requests = list(dir.get().values())

print(requests)

try:
    while not rospy.is_shutdown():
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
                exit()

        elif requests != currentR:
            #기존 유저가 다른 요청을 보냄
            for i in range (0,len(requests)):
                if requests[i] != currentR[i]:
                    result = requests[i]
                    break

            print(result)

            cnt = 0
            #로스는 한글을 지원하지 않으므로 데이터 베이스의 인덱스로 값을 변경
            for row in nodes: #row - List<Dictionary<string, int>>
                for col in row: #col - Dictionary<string, int>
                    for c in col.keys(): 
                        c = unicodedata.normalize('NFC',c) #List<string>v#내용이 같아도 서로 다르다고 인식하는 경우가 있어 자음모음 합쳐줌 출처 : https://jonsyou.tistory.com/26
                        for p in result.values():
                            p = unicodedata.normalize('NFC',p) 
                            if c == p :
                                print(c,p)
                                for key, value in result.items():
                                    if value == p:
                                        result[key] = col[c]
                                        cnt = cnt + 1
                                
            ##dict형을 string으로 명시적 형변환
            rs = json.dumps(result)
            pub.publish(rs)
            rate.sleep()

        time.sleep(0.1)#0.1초 마다 호출

except KeyboardInterrupt:
    exit()
    
