# <img src="https://user-images.githubusercontent.com/77663506/202992692-9b0fab8c-64e0-464b-84ce-6e15a52877df.png" style="display: inline-block; overflow: hidden; border-radius: 13px; width: 300px; height: 83px;">
카페인 - 카페 공부 필수 앱

# 📌 Introduction
보다 나은 공부/작업/회의를 위해 카공 정보를 제공하는 지도 기반 어플리케이션 ‘카페인’ 입니다.

# Member
- 여정화 (frontend - app)
- 장요엘 (frontend - app)
- 안소영 (frontend - web)
- 이의섭 (frontend - web)
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
![20221121cafein_erd](https://user-images.githubusercontent.com/77663506/202992552-e6fc81f8-e8d8-470a-b5b9-1a831b7509b0.png)
