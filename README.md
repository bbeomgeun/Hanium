<h3> 한이음 프로젝트 - 치매환자 위치추적 서비스 </h3>

 - 기간 : 2020-05-13 ~ 2020-11-30
 - 개요 : 치매환자 수 추이를 보면, 2024년에는 100만명이 넘을 것으로 조사된다. 치매환자가 있는 경우 가족들은 외출도 불가하며, 금전적인 치료비 또한 만만치 않다. 
 부득이하게 환자들을 두고 외출하는 경우, 환자의 옷에 달아놓은 뱃지에는 아두이노를 바탕으로한 센서가 부착된다. 환자 외출시 매핑되어있는 휴대폰으로 ‘환자외출’을 푸시해주며 위치추적을 가능하게 한다.
 또한, 구글 맵과 단말기의 GPS 등 연동하여 환자의 설정된 거주지 이외의 곳을 이탈하면 등록되어있는 비상연락망 모두에 푸시알림을 준다. 해당 관할구역의 경찰서와 연계하여 치매환자의 실종을 예방한다.
 치매환자 뿐 아니라, 정신지체 등을 앓고 있는 장애인까지 대상에 포함할 수 있다.
 
 ---
<h3> 프로젝트 구성 </h3> 

| 구성 | 역할 | 사용 언어 & 툴 |
|:------------:|:-----------------:|:-----------------:|
| Web | 관리자용 페이지 | java - eclipse |
| App | 사용자 위치확인 & 관리 | android studio |
| 백엔드 | 데이터베이스를 통한 정보 갱신, 웹과 앱 연결, cognito를<br> 이용한 사용자서비스인증 | aws DynamoDB + aws AppSync + awsCognito ->graphQL을 통해<br> 앱에서 데이터 수신 |
| 아두이노 | GPS 정보 송신 | 아두이노 + LORA모듈을 이용한 원거리 송신 |

---

 <h3> 나의 역할 </h3>
 
  - 어플 파트 팀원과 함께 전반적인 어플리케이션 개발<br>
  - 데이터 송수신을 위해 DynamoDB + AppSync 이용, 앱과 연동<br>
  - Cognito를 통해 로그인/회원가입/사용자 관리<br>
