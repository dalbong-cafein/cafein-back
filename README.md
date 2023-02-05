# <img src="https://user-images.githubusercontent.com/77663506/202992692-9b0fab8c-64e0-464b-84ce-6e15a52877df.png" style="display: inline-block; overflow: hidden; border-radius: 13px; width: 300px; height: 83px;">
카페인 - 카페 공부 필수 앱 </br></br>
<a href='https://play.google.com/store/apps/details?id=com.dalbong.cafein&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1' style="display: inline-block; overflow: hidden; border-radius: 13px; width: 250px; height: 83px;"><img alt='다운로드하기 Google Play' src='https://play.google.com/intl/ko/badges/static/images/badges/ko_badge_web_generic.png' style="border-radius: 13px; width: 250px; height: 105px;"/></a></br>
<a href="https://apps.apple.com/kr/app/%EC%B9%B4%ED%8E%98%EC%9D%B8-%EC%B9%B4%ED%8E%98-%EA%B3%B5%EB%B6%80-%ED%95%84%EC%88%98-%EC%95%B1/id6444003237" style="display: inline-block; overflow: hidden; border-radius: 13px; width: 250px; height: 83px;"><img src="https://tools.applemediaservices.com/api/badges/download-on-the-app-store/black/en-us?size=250x83&amp;releaseDate=1617667200&h=0f093bf7a3190b584434352a2f9980f1" alt="Download on the App Store" style="border-radius: 13px; width: 250px; height: 83px;"></a>

# 📌 Introduction
보다 나은 공부/작업/회의를 위해 카공 정보를 제공하는 지도 기반 어플리케이션 ‘카페인’ 입니다.

<p float="left">
  <img src="https://user-images.githubusercontent.com/77663506/204604602-cf7a7d31-d045-445a-bcdf-1218edf78a2a.png"  width="826" height="420" />
</p>
<p float="left">
  <img src="https://user-images.githubusercontent.com/77663506/204605235-01829519-0bdc-4fc1-aaab-b5325fc3b2f4.png" width="272" height="500" />
  <img src="https://user-images.githubusercontent.com/77663506/204605299-e969ef74-e701-4779-82e9-eab0441c7a0f.png" width="272" height="500" />
  <img src="https://user-images.githubusercontent.com/77663506/204605344-bfcf7ead-ae41-43cb-ae22-65c2ce0f0c74.png" width="272" height="500" /> 
</p>

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
- Java11
- Springboot
- SpringData JPA
- Spring Security + JWT 토큰
- Mysql
- Querydsl
- Redis
- Gradle

server 
- AWS Elastic beanstalk
- AWS RDS
- AWS S3
- AWS ElastiCache
- Git Actions

# Dependencies
```
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-security'
	implementation 'org.springframework.boot:spring-boot-starter-validation'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	compileOnly 'org.projectlombok:lombok'
	developmentOnly 'org.springframework.boot:spring-boot-devtools'
	runtimeOnly 'com.h2database:h2'
	runtimeOnly 'mysql:mysql-connector-java'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	testImplementation 'org.springframework.security:spring-security-test'
	implementation 'io.jsonwebtoken:jjwt:0.9.1'
	implementation 'org.springframework.boot:spring-boot-starter-data-redis'
	implementation group: 'net.nurigo', name: 'javaSDK', version: '2.2'
	implementation group: 'org.springframework.cloud', name: 'spring-cloud-starter-aws', version: '2.2.6.RELEASE'
	implementation 'org.springframework.cloud:spring-cloud-starter-bootstrap:3.0.3'
	implementation 'org.springframework.cloud:spring-cloud-starter-aws-secrets-manager-config:2.2.6.RELEASE'
	implementation 'com.querydsl:querydsl-jpa'
	implementation group: 'org.apache.poi', name: 'poi', version: '5.1.0'
	implementation group: 'org.apache.poi', name: 'poi-ooxml', version: '5.1.0'
	implementation group: 'com.googlecode.json-simple', name: 'json-simple', version: '1.1.1'
}
```

# API 명세서(진행중)
https://documenter.getpostman.com/view/15013144/UVkjuccy

# ERD(2023.02.06)
![20230206_cafein_erd](https://user-images.githubusercontent.com/77663506/216834032-341dca68-3e2d-4191-8dd2-938e08fe8d0c.png)

