<h2> 한이음 프로젝트 - 치매환자 위치추적 서비스 </h2>

 - 기간 : 2020-05-13 ~ 2020-11-30
 - 개요 : 치매환자 수 추이를 보면, 2024년에는 100만명이 넘을 것으로 조사된다. 치매환자가 있는 경우 가족들은 외출도 불가하며, 금전적인 치료비 또한 만만치 않다. 
 부득이하게 환자들을 두고 외출하는 경우, 환자의 옷에 달아놓은 뱃지에는 아두이노를 바탕으로한 센서가 부착된다. 환자 외출시 매핑되어있는 휴대폰으로 ‘환자외출’을 푸시해주며 위치추적을 가능하게 한다.
 또한, 구글 맵과 단말기의 GPS 등 연동하여 환자의 설정된 거주지 이외의 곳을 이탈하면 등록되어있는 비상연락망 모두에 푸시알림을 준다. 해당 관할구역의 경찰서와 연계하여 치매환자의 실종을 예방한다.
 치매환자 뿐 아니라, 정신지체 등을 앓고 있는 장애인까지 대상에 포함할 수 있다.
 
 ---
<h3> 프로젝트 구성 </h3> 

| 구성 | 역할 | 사용 언어 & 툴 |
|:------------:|:-----------------:|:-----------------:|
| Web | 관리자용 페이지 | html, css, javascript, java - eclipse |
| App | 사용자 위치확인 & 관리 | java - android studio |
| 백엔드 | 데이터베이스를 통한 정보 갱신, 웹과 앱 연결, cognito를<br> 이용한 사용자서비스인증 | aws DynamoDB + aws AppSync + awsCognito ->graphQL을 통해 앱에서 데이터 수신 |
| 아두이노 | GPS 정보 송신 | 아두이노 + LORA모듈을 이용한 원거리 송신 |

---

 <h3> 나의 역할 </h3>
 
  - 어플 파트 팀원과 함께 전반적인 어플리케이션 개발<br>
  - 데이터 송수신을 위해 DynamoDB + AppSync 이용, 앱과 연동<br>
  - Cognito를 통해 로그인/회원가입/사용자 관리<br>

---
 <h3> AWS 흐름 </h3>
 
 <img src="https://cdn-ssl-devio-img.classmethod.jp/wp-content/uploads/2020/06/appsync-api-768x308.png">
 
 출처 : https://dev.classmethod.jp/articles/amplify-android-graphql-api/
 
 - 모바일 앱을 사용할 시 AWS Amplify를 거쳐, GraphQL을 사용해 AWS AppSync가 데이터와 관련된 작업을 수행
 - AppSync에서 생성된 API가 DynamoDB 데이터베이스에 맞춰 테이블을 생성해주고, 읽어들일 수 있도록 도와준다. 
 
 <h2> AWS Amplify란? </h2>
 
  - AWS Amplify는 안전하고 확장 가능한 모바일 및 웹 애플리케이션을 구축하기 위한 개발 플랫폼이다.
  - 즉, Amplify를 이용하면 앱 개발 시에 필수적인 백엔드 구현, 배포, 테스트와 같은 단계를 손쉽게 할 수 있도록 도와주는 역할을 한다.

<img src="https://cdn-ssl-devio-img.classmethod.jp/wp-content/uploads/2020/05/amplify-using-service-640x386.png">

 - Amplify 와 통합되어 사용하는 서비스들
 
 1. AWS AppSync : GraphQL을 이용하여 애플리케이션에서 필요로 하는 데이터를 가져올 수 있도록 하는 관리형 서비스. 오프라인 상태이더라도 로컬로 데이터 액세스가 가능하도록 하고, 온라인 상태가 되면 데이터를 다시 동기화해줄 수 있는 기능을 갖추고 있다.
 
 2. Amazone Cognito : 가입, 로그인, 액세스 제어 기능을 갖춘 인증 관리 서비스. Identity Pool을 통해 Facebook, Google, Amazone과 같은 소셜 로그인과도 연동이 가능하다.
 
---

 - Amplify를 통해 기존 Dynamodb Table과 연결하는 법.
 
 1. AWS App Sync를 통해 기존 DB Table과 연동하여 console에서 API를 생성한다.
 2. 프로젝트에서 Amplify init을 해줘서 Amplify를 추가해준다.
 3. Amplify amplify add codegen --apiID(appsync API)를 통해 프로젝트에 API의 graphql을 생성한다.
 4. 이후에 gradle 파일을 추가해주고, 앱을 실행시키면 auto generated로 데이터베이스에 접근할 수 있는 쿼리 클래스가 생성된다.

---
<h3> API란? </h3>

<img src = "http://blog.wishket.com/wp-content/uploads/2019/10/API-%EC%89%BD%EA%B2%8C-%EC%95%8C%EC%95%84%EB%B3%B4%EA%B8%B0.png">

출처 : http://blog.wishket.com/api%EB%9E%80-%EC%89%BD%EA%B2%8C-%EC%84%A4%EB%AA%85-%EA%B7%B8%EB%A6%B0%ED%81%B4%EB%9D%BC%EC%9D%B4%EC%96%B8%ED%8A%B8/

- API는 점원과 같은 역할을 한다.
- 즉, 프로그램이 명령할 수 있게 명령 목록을 정리하고, 명령을 받으면 응용프로그램과 상호작용하며 명령에 대한 결과값을 전달해준다.
- API는 프로그램들이 서로 상호작용하는 것을 도와주는 매개체라고 볼 수 있다.

<h4> API의 역할 </h4>

 1. API는 서버와 데이터베이스에 대한 출입구 역할을 한다.
  - 모든 사람들이 데이터베이스에 접근하는 것을 방지. 허용된 사람들에게만 접근성을 부여한다.
  
 2. API는 애플리케이션과 기기가 원활하게 통신할 수 있도록 한다.
  - 스마트폰 어플이나 프로그램이 기기와 데이터를 원활하게 주고받을 수 있도록 도와주는 역할
  
 3. API는 모든 접속을 표준화한다.
  - API는 모든 접속을 표준화하기 때문에 기계/운영체제 등과 상관없이 동일한 액세스를 얻을 수 있다.

---

<h3> What is GraphQL? </h3>
 - graphQL은 API를 위한 쿼리문이다.

---

 <h3> gradle파일이란 무엇인가? </h3>
 
 - AWS App Sync를 Android studio project에 연결하던 중, version이 맞지 않아 gradle sync 오류가 났고, 이런 오류가 날때마다 단순 검색을 통해 해결하는 것보단 직접 이해해서 고치는 편이 도움이 되겠다고 생각해서 공부를 해보았다.
 
 <h4> Gradle이란? </h4>
 
  - Gradle이란 빌드(소스코드와 의존성 라이브러리를 엮어서 컴파일하여 패키징하는 것) 배포 도구이다.
  - 프로젝트를 만들게 되면 Gradle이란 것도 같이 생성되는 것을 볼 수 있다.
  - Android Studio(IDE)와 빌드 시스템은 서로 독립적이기 때문. (코드의 편집 담당 <-> 빌드 담당)
  - 따라서 Android Studio project 설정과 Gradle Build 설정이 동기화되지 않아 에러가 나타날 수 있다.
  
  참고 : https://developer.android.com/studio/build?hl=ko
  
  ---
  
  <h3> Android NDK란? </h3>
  
  - 카카오맵을 사용시 관련 파일을 JNI에 압축을 풀었고, build할때 NDK version을 맞춰줘야하는 경우가 있었다.
  - NDK가 무엇인지 궁금해졌고, 이에 관해 간단하게 공부를 했다.
  
  <h4> NDK란? </h4>
  
  - 공식 developer 문서에서의 NDK (Native Development Kit)
 
  ~~~
  The Android NDK is a toolset that lets you implement parts of your app using native-code languages such as C and C++.
  For certain types of apps, this can help you reuse code libraries written in those languages.
  ~~~
   - java코드만 사용해서 필요한 기능과 성능을 모두 만족시키기는 힘들다. 따라서 C나 C++ 언어로 작성된 프로그램을 java에서 사용할 수 있도록 JDK에서 제공하는 것이 JNI(Java Native Interface)이다.
   - JNI를 통해 java코드와 NDK를 연결시켜준다.
