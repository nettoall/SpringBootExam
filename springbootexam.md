SpringBoot Library 변경
======================
## 버전 정보
* Spring Boot 2.4.1
* Gradle 6.7.1
* Junit5

## 변경방법
### gradle version 변경
    ./gradlew wrapper --gradle-version 6.7.1

### build.gradle 수정
* gradle 6부터 implementatoin, testImplementation 사용 

    plugins {
        id 'org.springframework.boot' version '2.4.1'
        id 'io.spring.dependency-management' version '1.0.10.RELEASE'
        id 'java'
    }

    group 'com.nettoall'
    version '1.0.0-SNAPSHOT-'+new Date().format("yyyyMMddHHmmss")
    sourceCompatibility = 1.8

    repositories {
        mavenCentral()
        jcenter()
    }

    // for Junit 5
    test {
        userJUnitPlatform()
    }

    dependencies {
        implementation('org.springframework.boot:spring-boot-starter-web')
        implementation('org.springframework.boot:spring-boot-starter-mustache')

        // lombok
        implementation('org.projectlombok:lombok')
        annotationProcessor('org.projectlombok:lombok')
        testImplementation('org.projectlombok:lombok')
        testAnnotationProcessor('org.projectlombok:lombok')

        implementation('org.springframework.boot:spring-boot-starter-data-jpa')
        implementation('org.mariadb.jdbc:mariadb-java-client')
        implementation('com.h2database:h2')

        testImplementation('org.springframework.boot:spring-boot-starter-test')

        // 권한 spring-security 수행시만 추가 
        implementation('org.springframework.boot:spring-boot-starter-oauth2-client')
        implementation('org.springframework.boot:spring-session-jdbc')
        testImplementation('org.springframework.security:spring-security-test')
    }
  
### JUnit 변경
* JUnit 4 -> JUnit 5 로 수정
* 맥 전체 교체 : Cmd + shift + R

#### @Test 변경
* import 패키지 변경
* org.junit.Test 에서 org.junit.jupiter.api.Test 로 변경

#### @RunWith
* @ExtendWith 로 변경
* 패키지 org.junit.jupiter.api.extension.ExtendWith

#### SpringRunner
* @RunWith(SpringRunner.class) 에서 @ExtendWith(Springextension) 으로 변경
* 패키지 org.springframework.test.context.junit.jupiter.SpringExtension

#### @After
* 테스트 메소드 끝날때마다 수행
* @AfterEach 로 변경
* 패키지 org.junit.jupiter.api.AfterEach

#### @Before
* @BeforeEach 로 변경
* 패키지 org.junit.jupiter.api.BeforeEach

#### 주의
* 테스트 수행시 'No tests found for given includes' 발생시 확인
    test {
        useJUnitPlatform()
    }
  
### JPA 수정
* Spring Boot 2.1.10 부터 MySql 문법 지정 방식 변경됨
* DB설정 변경
* local DB 설정 변경(main,test)
    * as-is(application.properties)  
  
        spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect
  
    * to-be(application.properties)
        spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL57Dialect
        spring.jpa.properties.hibernate.dialect.storage_engine=innodb
        spring.datasource.hikari.jdbc-url=jdbc:h2:mem:testdb;MODE=MYSQL
        spring.datasource.hikari.username=sa

* real DB  설정 변경
    * as-is
        spring.jpa.hibernate.ddl-auto=none
        spring.datasource.url=jdbc:mariadb://ip:port/datasource명
        spring.datasource.username=sa
        spring.datasource.password=password
        spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
      
    * to-be
        spring.jpa.hibernate.ddl-auto=none
        spring.jpa.show_sql=false
      
        spring.datasource.hikari.jdbc-url=jdbc:mariadb://ip:port/database명
        spring.datasource.hikari.username=sa
        spring.datasource.hikari.password=password
        spring.datasource.hikari.driver-class-name=org.mariadb.jdbc.Driver
      

### spring.profile.include
* Spring Boot 2.4가 되면서 spring.profile.include 옵션 변경
* 기존 spring.profiles.include=oauth 설정 동작 안함
* profile Group으로 관리
    spring.profiles.group.<group>=dev, auth
* 예
    -Dspring.profiles.active=local-group
* application.properties
    spring.profiles.group.local-real=local-real, oauth
    spring.profiles.group.real=real, real-db, oauth
    spring.profiles.group.real1=real1, real-db, oauth
    spring.profiles.group.real2=real2, real-db, oauth
