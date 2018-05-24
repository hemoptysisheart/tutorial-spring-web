# Ch.05 - DB 연동

현실적으로는 DB에 연동해야만 데이터를 잃어버리지 않고 저장하고 사용할 수 있다.
MySQL을 사용한 데이터 저장을 시도한다.

## STEP 1 - 연결할 DB 만들기

회원 가입한 계정 정보를 저장해야 한다.
해당 DB를 만든다. DB 스키마 작성은 MySQL Workbench를 사용한다.
MySQL Workbench에서 모델을 만들고 DDL을 생성하면,
DDL의 테이블 순서가 유지된다.
이것은 DB 변경을 추적하기 위해 매우 중요한 특징이 된다.
(`mysqldump`는 순서가 유지되지 않는다.)

모델을 추가한다.
스키마 이름을 변경하고, `Default Collation`을 `utf8mb4 - utf8mb4_bin`으로 변경한다.
DDL에서 스키마의 `character set`을 `utf8mb4`로, `collate`를 `utf8mb4_bin`으로 설정된다.

![1. 스키마 작성](step_1_create_schema.png)

MySQL Workbench가 DDL 생성시 스키마를 생성하려면 테이블이 하나는 필요하다.
임시로 PK만 있는 테이블을 추가한다. 이후 회원 가입한 계정 정보 저장에 사용한다.

![2. 테이블 추가](step_1_create_table.png)

`Menu > File > Export > Forward Engineer SQL CREATE Script...`(<kbd><kbd>shift</kbd> + <kbd>command</kbd> + <kbd>G</kbd></kbd>)로 DDL파일(`*.sql`)을 생성한다.

![3. DDL 생성 옵션](step_1_export_option.png)

DDL을 실행해 스키마를 생성한다.

![4. DDL 실행](step_1_run_ddl.png)

### 프로젝트 구조

```
.
├── db
│   ├── tutorial_spring_web.mwb
│   └── tutorial_spring_web.sql
├── pom.xml
└── src
    └── main
        ├── java
        │   └── hemoptysisheart
        │       └── github
        │           └── com
        │               └── tutorial
        │                   └── spring
        │                       └── web
        │                           ├── ApplicationRunner.java
        │                           ├── RootController.java
        │                           └── SignUpReq.java
        └── resources
            ├── application.yml
            └── templates
                └── _
                    ├── index.html
                    └── signup.html
```

[전체 구조](step_1_tree.txt)

## STEP 2 - 의존성 추가하기

JPA의존성을 추가하고, DB 연결을 위해 커넥터를 추가한다.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
```

## STEP 3 - DB 연결하기

JPA도 기본적으로는 JDBC를 기반으로 작동한다.

* `spring.datasource.hikari` : Spring Boot 2.0 부터 기본 DB 커넥션 풀 관리자가 된 HikariCP 설정.
* `spring.jpa` : JPA(Spring Data JPA) 설정.

```yaml
spring:
  datasource:
    hikari:
      driver-class-name: com.mysql.jdbc.Driver
      jdbc-url: jdbc:mysql://localhost/tutorial_spring_web?connectionCollation=utf8mb4_bin
      username: root
      password: ''
      idle-timeout: 12000
      connection-test-query: SELECT 1
  jpa:
    database: mysql
    open-in-view: false
```

[전체 설정](../../src/main/resources/application.yml)

JPA JavaConfig 설정 추가.

```java
package hemoptysisheart.github.com.tutorial.spring.web;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories
@EnableTransactionManagement
public class JpaConfiguration {
    @Bean
    @ConfigurationProperties("spring.datasource.hikari")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.MYSQL);
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource());
        factory.setPackagesToScan(JpaConfiguration.class.getPackageName());
        factory.setJpaVendorAdapter(adapter);
        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager(entityManagerFactory().getObject());
    }

    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }
}
```

### 프로젝트 구조

```
./src/main
├── java
│   └── hemoptysisheart
│       └── github
│           └── com
│               └── tutorial
│                   └── spring
│                       └── web
│                           ├── ApplicationRunner.java
│                           ├── JpaConfiguration.java
│                           ├── RootController.java
│                           └── SignUpReq.java
└── resources
    ├── application.yml
    └── templates
        └── _
            ├── index.html
            └── signup.html
```

[전체 구조](step_3_tree.txt)

### 웹 애플리케이션 실행 로그

JPA, Hibernate, HikariCP 관련 로그를 볼 수 있다.

```
2018-05-24 13:38:26.615  INFO 4195 --- [           main] j.LocalContainerEntityManagerFactoryBean : Building JPA container EntityManagerFactory for persistence unit 'default'
2018-05-24 13:38:26.624  INFO 4195 --- [           main] o.hibernate.jpa.internal.util.LogHelper  : HHH000204: Processing PersistenceUnitInfo [
	name: default
	...]
2018-05-24 13:38:26.681  INFO 4195 --- [           main] org.hibernate.Version                    : HHH000412: Hibernate Core {5.2.17.Final}
2018-05-24 13:38:26.683  INFO 4195 --- [           main] org.hibernate.cfg.Environment            : HHH000206: hibernate.properties not found
2018-05-24 13:38:26.714  INFO 4195 --- [           main] o.hibernate.annotations.common.Version   : HCANN000001: Hibernate Commons Annotations {5.0.1.Final}
2018-05-24 13:38:26.793  INFO 4195 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2018-05-24 13:38:26.950  INFO 4195 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2018-05-24 13:38:26.961  INFO 4195 --- [           main] org.hibernate.dialect.Dialect            : HHH000400: Using dialect: org.hibernate.dialect.MySQL5Dialect
2018-05-24 13:38:27.142  INFO 4195 --- [           main] j.LocalContainerEntityManagerFactoryBean : Initialized JPA EntityManagerFactory for persistence unit 'default'
```

[전체 로그](step_3_bootup.log)