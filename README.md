2023월 9월 이후, 하나의 기존 레포지토리에서 서버별 레포지토리와 모바일 앱 레포지토리를 분리하였습니다.
<div>
  <ul>
  <li><a href="https://github.com/SWM-IDLE/mybrary-application">Flutter 기반 앱 레포지토리</a></li>
  <li><a href="https://github.com/SWM-IDLE/mybrary-user-service">유저 서비스 레포지토리</a></li>
  <li><a href="https://github.com/SWM-IDLE/mybrary-book-service">도서 서비스 레포지토리</a></li>
  <li><a href="https://github.com/SWM-IDLE/mybrary-notification-service">푸시 알림 서버 레포지토리</a></li>
  <li><a href="https://github.com/SWM-IDLE/mybrary-multi-book-recognition-service">다중 도서 인식 서버 레포지토리</a></li>
  <li><a href="https://github.com/SWM-IDLE/mybrary-apigateway-server">API Gateway 서버 레포지토리</a></li>
  <li><a href="https://github.com/SWM-IDLE/mybrary-eureka-server">Eureka 서버 레포지토리</a></li>
  <li><a href="https://github.com/SWM-IDLE/mybrary-config-server">Config 서버 레포지토리</a></li>
  </ul>
</div>

<br/>

# Mybrary
> 소장 도서로 사람을 잇고, 도서의 가치를 발견하다.

<br/>
<br/>

## Mybrary가 추구하는 가치
- 사람과 사람을 도서로 잇고, 도서로 연결한다.
- 각자의 도서 가치를 나누고 공유하며, 그 속에서 더 큰 가치를 발견한다.

<br/>
<br/>

## 👨‍💻 IDLE 팀
  
| 역할 | 직무   | 이름   | GitHub                                         |
| ---- | ------ | ------ | ---------------------------------------------- |
| 팀장 | 백엔드, 아키텍처 및 CI/CD 구축 | 강민성 | [@minnseong](https://github.com/minnseong) |
| 팀원 | 백엔드, 아키텍처 및 CI/CD 구축 | 이세영 | [@0112leesy](https://github.com/0112leesy) |
| 팀원 | 모바일 앱, UI/UX | 진동규 | [@DonggyuJin](https://github.com/DonggyuJin) |  

</div>
 
<br/>
<br/>

## 기술 스택

### Backend Stack
<div align=left> 
<!--    <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> 
  <img src="https://img.shields.io/badge/python-3776AB?style=for-the-badge&logo=python&logoColor=white"> 
  <img src="https://img.shields.io/badge/flask-000000?style=for-the-badge&logo=flask&logoColor=white">
  <img src="https://img.shields.io/badge/spring jpa-6DB33F?style=for-the-badge&logo=springjpa&logoColor=white">
  <img src="https://img.shields.io/badge/spring boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/spring security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">
  <img src="https://img.shields.io/badge/dart-0175C2?style=for-the-badge&logo=dart&logoColor=white">
  <img src="https://img.shields.io/badge/flutter-02569B?style=for-the-badge&logo=flutter&logoColor=white">
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> 
  <img src="https://img.shields.io/badge/firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=white">
  <img src="https://img.shields.io/badge/redis-DC382D?style=for-the-badge&logo=redis&logoColor=white">
  <img src="https://img.shields.io/badge/aws-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white">
  <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
  <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/jira-0052CC?style=for-the-badge&logo=jira&logoColor=white">
  <img src="https://img.shields.io/badge/confluence-172B4D?style=for-the-badge&logo=confluence&logoColor=white">
  <img src="https://img.shields.io/badge/slack-4A154B?style=for-the-badge&logo=slack&logoColor=white">
  <img src="https://img.shields.io/badge/notion-000000?style=for-the-badge&logo=notion&logoColor=white"> -->
  Java17, Spring Boot 3, Spring Security, Spring Cloud, Spring Data JPA, Spring Rest Docs, Redis, Mysql, QueryDsl
</div>

### Infra Stack
Docker, AWS EC2, S3, ECS, ECR, Fargate, RDS, Cloud Watch

### Moblie App Stack
<div align=left> 
  Dart, Flutter
</div>

### Collaboration Stack
<div align=left> 
  Github, Jira & Confluence, Slack, Notion

<br/>
<br/>

## Infra Architecture
### 2023년 11월 11일
<img width="746" alt="image" src="https://github.com/SWM-IDLE/mybrary/assets/71378475/22d3b65f-da7e-4e1c-bdbf-e05cb51bf4ac">

### 2023년 9월 3일
![mybrary 아키텍처 0817-페이지-1 drawio](https://github.com/SWM-IDLE/mybrary/assets/71378475/f60ea648-c522-408b-9181-f640192c5b14)


<br/>
<br/>

## ERD
![image](https://github.com/SWM-IDLE/mybrary/assets/71378475/fbd8f09e-4c34-4ad6-bff1-13d0cf2f3f37)

<br/>
<br/>

## API DOCS
MSA 환경에서 Rest Docs + Swagger UI를 통해 각 마이크로 서비스의 API 문서를 통합 <br/>
<a href="http://docs.mybrary.kr/">API Docs</a> 

<br/>
<br/>

## UI
### 로딩 화면, 소셜 로그인, 홈, 마이관심사
![스크린샷 2023-09-04 오후 8 46 26](https://github.com/SWM-IDLE/mybrary/assets/71378475/f9e8bef2-1c3b-492e-bd7c-6aa0bc56ffd3)
### 도서 검색, 바코드 스캔, 도서 검색 결과, 도서 상세
![스크린샷 2023-09-04 오후 8 46 36](https://github.com/SWM-IDLE/mybrary/assets/71378475/076182d0-8542-494b-b7e3-65cff1c69e13)
### 마이북(메인 화면), 마이북 목록, 마이북 상세, 마이북 상세 기록
![스크린샷 2023-09-04 오후 8 46 47](https://github.com/SWM-IDLE/mybrary/assets/71378475/fa4e3542-4e39-4362-80d2-3bf025a54eb6)
### 프로필(메인 화면), 팔로잉, 사용자 검색, 다른 사용자 프로필
![스크린샷 2023-09-04 오후 8 46 58](https://github.com/SWM-IDLE/mybrary/assets/71378475/f99f1039-453a-4114-900e-999137106096)
### 다른 사용자 마이브러리, 마이북 상세, 마이 리뷰 등록, 마이 리뷰 목록
![스크린샷 2023-09-04 오후 8 47 09](https://github.com/SWM-IDLE/mybrary/assets/71378475/b42bf2de-4952-4b60-9f3a-ff2a0aa4c52b)
