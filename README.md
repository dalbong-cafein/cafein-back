# <img src="https://s3.us-west-2.amazonaws.com/secure.notion-static.com/19bb7e70-533f-4a65-bd9a-774c2a1c883e/%EC%B9%B4%ED%8E%98%EC%9D%B8_%EB%A1%9C%EA%B3%A0_Main_Color.svg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20220908%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20220908T093626Z&X-Amz-Expires=86400&X-Amz-Signature=ec0a858c9daf521260f3cd055a923fddcb881704cfc1bc72ef36f29be32d1fda&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22%25EC%25B9%25B4%25ED%258E%2598%25EC%259D%25B8_%25EB%25A1%259C%25EA%25B3%25A0_Main%2520Color.svg%22&x-id=GetObject" style="display: inline-block; overflow: hidden; border-radius: 13px; width: 250px; height: 83px;">
카페인 - 국민대학교 알파프로젝트

# 📌 Introduction
보다 나은 공부/작업/회의를 위해 카공 정보를 제공하는 지도 기반 어플리케이션 ‘카페인’ 입니다.

# Member
- 여정화 (frontend - app)
- 안소영 (frontend - web)
- 이형우 (backend)

# Branch 전략
 - master : 최상위 브랜치로 product로 release하는 소스가 저장됩니다.
 - release : QA를 위해 develop 브랜치에서 release 브랜치를 생성합니다. 완료되면 master브랜치로 merge합니다.
 - develop : release할 준비가 된 준비가 된 브랜치입니다. 개발된 모든 feature는 develop에 merge됩니다.
 - feature : 개별 기능의 구현과 버그를 해결할때 사용하는 브랜치입니다. master branch에는 직접 접근이 불가합니다.
 
 <br/>
 
 ![image](https://user-images.githubusercontent.com/69441691/152711253-348c902b-2058-4b5b-93cf-59eb8d92823f.png)

# 👨‍💻 Tech Stack
back 
- Springboot
- SpringData JPA
- Spring Security + JWT 토큰
- Mysql
- Querydsl
- AOP
- Gradle
- coolsms 문자발송

server 
- AWS Elastic beanstalk
- AWS RDS
- AWS S3
- AWS ElastiCache
- Git Actions

# API 명세서(진행중)
https://documenter.getpostman.com/view/15013144/UVkjuccy

# ERD(진행중)
![cafein_erd](https://user-images.githubusercontent.com/77663506/189089305-b7233671-ec8c-4724-869a-8b953a1d01a1.png)
