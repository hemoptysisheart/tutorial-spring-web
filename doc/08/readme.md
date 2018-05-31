# CH.08 - 프로젝트 구조 개선하기

## STEP 1 - 컴포넌트 레이어에 따라 패키지 분리하기

소스코드의 기능에 따라 패키지를 나누는 것은 각 타입의 의존성 관리를 기능 분리 등이 가능하게 만드는 기본 조치이다.
우선 각 컴포넌트 레이어에 맞춰 패키지를 나눈다.

```
./src/main
├── java
│   └── hemoptysisheart.github.com.tutorial.spring.web
│       ├── borderline
│       │   ├── AccountBorderline.java
│       │   ├── AccountPo.java
│       │   └── CreateAccountCmd.java
│       ├── configuration
│       │   └── JpaConfiguration.java
│       ├── controller
│       │   ├── RootController.java
│       │   └── SignUpReq.java
│       ├── dao
│       │   └── AccountDao.java
│       ├── jpa
│       │   ├── entity
│       │   │   └── AccountEntity.java
│       │   └── repository
│       │       └── AccountRepository.java
│       ├── runner
│       │   └── ApplicationRunner.java
│       └── service
│           ├── AccountService.java
│           └── CreateAccountParams.java
└── resources
    ├── application.yml
    └── templates
        └── _
            ├── index.html
            ├── newbie.html
            └── signup.html
```
[전체 구조](step_1_tree.txt)

* `hemoptysisheart.github.com.tutorial.spring.web.jpa`
: JPA Entity와 JpaRepository는 JPA에 종속적인 타입이기 때문에 `jpa.entity`와 `jpa.repository` 패키지로 분리한다.
* `hemoptysisheart.github.com.tutorial.spring.web.runner`
: 패키지 중복을 피하기 위해 클래스가 하나뿐이지만 별도의 패키지로 분리.

## STEP 2 - 분리된 패키지에 맞춰 설정 수정

패키지 변경으로 인해 기존 설정으로는 컴포넌트 설정을 활용할 수 없어서 애플리케이션을 실행할 수 없게 됐다.
개발도구의 지원을 최대한 이끌어내기 위해 JavaConfig 설정을 추가한다.

1. 웹MVC 설정을 위해 `@EnableWebMvc` 어노테이션은 `WebMvcConfiguration`으로 분리한다.
1. `hemoptysisheart.github.com.tutorial.spring.web.configuration`의 설정을 설정을 사용할 수 있도록 `scanBasePackageClasses` 설정을 추가한다.

애플리케이션의 메인 설정이 설정 패키지를 검색하도록 해서 메인 설정을 단순하게 만든다.

> 메인 설정은 JavaConfig와 `application.yml`의 2종류가 있다.
> JavaConfig는 컴파일타임에 결정하는 로직에 대한 설정이고,
> `application.yml`은 런타임에 결정하는 실행 환경에 대한 설정이다.

```java
package hemoptysisheart.github.com.tutorial.spring.web.runner;

// ... 생략 ...

@SpringBootApplication(scanBasePackageClasses = {ApplicationConfiguration.class})
public class ApplicationRunner {
    // ... 생략 ...
}
```
[ApplicationRunner.java](../../src/main/java/hemoptysisheart/github/com/tutorial/spring/web/runner/ApplicationRunner.java)

`ApplicationRunner`에서 웹MVC 설정 책임을 넘겨받은 `WebMvcConfiguration`이 넘겨받는다.

1. `@EnableWebMvc`설정을 제공한다.
1. 웹 페이지를 서비스할 컨트롤러를 설정할 수 있도록 `@ComponentScan(basePackageClasses = {ControllerConfiguration.class})` 설정을 제공한다.

```java
package hemoptysisheart.github.com.tutorial.spring.web.configuration;

// ... 생략 ...

@Configuration
@EnableWebMvc
@ComponentScan(basePackageClasses = {ControllerConfiguration.class})
public class WebMvcConfiguration {
}
```
[WebMvcConfiguration.java](../../src/main/java/hemoptysisheart/github/com/tutorial/spring/web/configuration/WebMvcConfiguration.java)

DB 연동용 JPA 설정에 바뀐 패키지 정보를 제공한다.

1. 레포지토리 컨포넌트를 설정하도록 패키지 정보를 추가한다. `@EnableJpaRepositories(basePackageClasses = {RepositoryConfiguration.class})`
1. JPA 엔티티를 관리할수 있도록 엔티티 매니저에 패키지 정보를 추가한다. `factory.setPackagesToScan(EntityConfiguration.PACKAGE_NAME)`

```java
package hemoptysisheart.github.com.tutorial.spring.web.configuration;

// ... 생략 ...

@Configuration
@EnableJpaRepositories(basePackageClasses = {RepositoryConfiguration.class})
@EnableTransactionManagement
public class JpaConfiguration {
    // ... 생략 ...

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        // ... 생략 ...

        factory.setPackagesToScan(EntityConfiguration.PACKAGE_NAME);

        // ... 생략 ...
        return factory;
    }
}
```
[JpaConfiguration.java](../../src/main/java/hemoptysisheart/github/com/tutorial/spring/web/configuration/JpaConfiguration.java)

웹MVC 설정에서 뷰와 모델에 대한 설정은 컨트롤러가 코드를 통해 제공한다.
즉, 컨트롤러만 설정하면 된다.

1. `ControllerConfiguration` 클래스에 `@Configuration` 어노테이션으로 컴포넌트 스캔이 작동할 수 있도록 한다.
1. `@ComponentScan(basePackageClasses = {BorderlineConfiguration.class})`으로 모델 인스턴스를 가져올 때 필요한 보더라인 컴포넌트에 필요한 설정을 제공한다.

```java
package hemoptysisheart.github.com.tutorial.spring.web.controller;

// ... 생략 ...

@Configuration
@ComponentScan(basePackageClasses = {BorderlineConfiguration.class})
public class ControllerConfiguration {
    // ... 생략 ...
}
```
[ControllerConfiguration.java](../../src/main/java/hemoptysisheart/github/com/tutorial/spring/web/controller/ControllerConfiguration.java)

1. `@Configuration` : `BorderlineConfiguration` 클래스를 스프링 프레임워크가 설정 정보로 인식하도록 한다.
1. `@ComponentScan(basePackageClasses = {ServiceConfiguration.class})` : 내부 로직인 보더라인 레이어에 필요한 서비스 레이어 설정 정보를 제공한다.

```java
package hemoptysisheart.github.com.tutorial.spring.web.borderline;

// ... 생략 ...

@Configuration
@ComponentScan(basePackageClasses = {ServiceConfiguration.class})
public class BorderlineConfiguration {
    // ... 생략 ...
}
```
[BorderlineConfiguration.java](../../src/main/java/hemoptysisheart/github/com/tutorial/spring/web/borderline/BorderlineConfiguration.java)

`BorderlineConfiguration`와 동일. 필요한 레이어의 설정 정보를 스프링 프레임워크에 제공한다.

```java
package hemoptysisheart.github.com.tutorial.spring.web.service;

// ... 생략 ...

@Configuration
@ComponentScan(basePackageClasses = {DaoConfiguration.class})
public class ServiceConfiguration {
    // ... 생략 ...
}
```
[ServiceConfiguration.java](../../src/main/java/hemoptysisheart/github/com/tutorial/spring/web/service/ServiceConfiguration.java)

```java
package hemoptysisheart.github.com.tutorial.spring.web.dao;

// ... 생략 ...

@Configuration
@ComponentScan(basePackageClasses = {RepositoryConfiguration.class})
public class DaoConfiguration {
    // ... 생략 ...
}
```
[DaoConfiguration.java](../../src/main/java/hemoptysisheart/github/com/tutorial/spring/web/dao/DaoConfiguration.java)

레포지토리 컴포넌트는 JPA 엔티티에 의존성을 가지고 있지만, 컴포넌트 스캔으로 엔티티 매니저에 JPA 엔티티 패키지 정보를 제공할 수 없다.
그래서 `@Configuration`과 `@ComponentScan`으로 스프링 프레임워크에 설정 정보를 제공하지 못한다.

`RepositoryConfiguration`는 `JpaConfiguration`의 `@EnableJpaRepositories(basePackageClasses = {RepositoryConfiguration.class})` 설정에 사용한다.

```java
package hemoptysisheart.github.com.tutorial.spring.web.jpa.repository;

// ... 생략 ...

public class RepositoryConfiguration {
    // ... 생략 ...
}
```
[RepositoryConfiguration.java](../../src/main/java/hemoptysisheart/github/com/tutorial/spring/web/jpa/repository/RepositoryConfiguration.java)

`EntityConfiguration`도 스프링 프레임워크의 컴포넌트 스캔이 아닌 `JpaConfiguration`에 직접 정보를 제공하는 설정 클래스이다.
`JpaConfiguration.entityManagerFactory()` 메서드에서
`LocalContainerEntityManagerFactoryBean.setPackagesToScan(String...)` 메서드의 인자로 사용할
`EntityConfiguration.PACKAGE_NAME` 문자열을 제공하는 용도이다.

```java
package hemoptysisheart.github.com.tutorial.spring.web.jpa.entity;

// ... 생략 ...

public class EntityConfiguration {
    public static final Package PACKAGE = EntityConfiguration.class.getPackage();
    public static final String PACKAGE_NAME = PACKAGE.getName();
}
```
[EntityConfiguration.java](../../src/main/java/hemoptysisheart/github/com/tutorial/spring/web/jpa/entity/EntityConfiguration.java)

### 프로젝트 구조

```
./src/main
├── java
│   └── hemoptysisheart.github.com.tutorial.spring.web
│       ├── borderline
│       │   ├── AccountBorderline.java
│       │   ├── AccountPo.java
│       │   ├── BorderlineConfiguration.java
│       │   └── CreateAccountCmd.java
│       ├── configuration
│       │   ├── ApplicationConfiguration.java
│       │   ├── JpaConfiguration.java
│       │   └── WebMvcConfiguration.java
│       ├── controller
│       │   ├── ControllerConfiguration.java
│       │   ├── RootController.java
│       │   └── SignUpReq.java
│       ├── dao
│       │   ├── AccountDao.java
│       │   └── DaoConfiguration.java
│       ├── jpa
│       │   ├── entity
│       │   │   ├── AccountEntity.java
│       │   │   └── EntityConfiguration.java
│       │   └── repository
│       │       ├── AccountRepository.java
│       │       └── RepositoryConfiguration.java
│       ├── runner
│       │   └── ApplicationRunner.java
│       └── service
│           ├── AccountService.java
│           ├── CreateAccountParams.java
│           └── ServiceConfiguration.java
└── resources
    ├── application.yml
    └── templates
        └── _
            ├── index.html
            ├── newbie.html
            └── signup.html
```
[전체 구조](step_2_tree.txt)

## STEP 3 - (선택)비지니스 로직 오브젝트와 DTO 분리

(상대적으로) 복잡한 비지니스 로직을 가진 컴포넌트 클래스와 컴포넌트 레이어 사이를 오가는 (상대적으로) 단순한 DTO 클래스를
서로 다른 패키지로 분리해서 각 패키지와 클래스의 목적과 내용, 책임을 명확히 한다.

* `hemoptysisheart.github.com.tutorial.spring.web.controller.req` : Request. HTTP 리퀘스트를 바인딩할 클래스용 패키지. 밸리데이션과 재입력 필드 등 웹UI 의존적인 코드를 가진다.
* `hemoptysisheart.github.com.tutorial.spring.web.borderline.cmd` : Command. 내부 로직을 실행할 때 필요한 정보. 웹MVC 레이어의 정보에서 웹 의존적인 코드가 빠지고 필요한 경우 현재 유저의 ID 등을 추가한다.
* `hemoptysisheart.github.com.tutorial.spring.web.service.params` : Parameters. 실재 데이터 관리 단위이자 최소 로직 단위인 JPA 엔티티를 정보를 필드로 가진다.
* `hemoptysisheart.github.com.tutorial.spring.web.borderline.po` : Plain Object. JPA 엔티티에서 JPA 요소를 제거해 템플릿 등의 외부 로직에서 데이터 변경 등 권한 외 조작을 못하도록 막는다.

### 프로젝트 구조

```
./src/main
├── java
│   └── hemoptysisheart.github.com.tutorial.spring.web
│       ├── borderline
│       │   ├── AccountBorderline.java
│       │   ├── BorderlineConfiguration.java
│       │   ├── cmd
│       │   │   └── CreateAccountCmd.java
│       │   └── po
│       │       └── AccountPo.java
│       ├── configuration
│       │   ├── ApplicationConfiguration.java
│       │   ├── JpaConfiguration.java
│       │   └── WebMvcConfiguration.java
│       ├── controller
│       │   ├── ControllerConfiguration.java
│       │   ├── RootController.java
│       │   └── req
│       │       └── SignUpReq.java
│       ├── dao
│       │   ├── AccountDao.java
│       │   └── DaoConfiguration.java
│       ├── jpa
│       │   ├── entity
│       │   │   ├── AccountEntity.java
│       │   │   └── EntityConfiguration.java
│       │   └── repository
│       │       ├── AccountRepository.java
│       │       └── RepositoryConfiguration.java
│       ├── runner
│       │   └── ApplicationRunner.java
│       └── service
│           ├── AccountService.java
│           ├── ServiceConfiguration.java
│           └── params
│               └── CreateAccountParams.java
└── resources
    ├── application.yml
    └── templates
        └── _
            ├── index.html
            ├── newbie.html
            └── signup.html
```
[전체 구조](step_3_tree.txt)

## STEP 4 - 실행환경용 설정을 패키지 밖으로 빼내기

실행환경용 설정은 `application.yml` 파일에 작성한다.
그런데 애플리케이션을 패키징할 경우, `*.jar`파일은 `application.yml` 파일까지 포함하게 된다.

이 경우 첫째, 잦은 패키징이 필요한 문제가 있다.
지금 설정에서는 실행환경이 변할 경우 새로 패키징을 해야 한다.
예를 들어 DB가 로컬호스트에서 별도의 서버에서 실행하는 경우에 새로 패키징을 해야 하는 문제가 있다.

둘째, 보안 문제가 있다.
빌드할 때 `application.yml` 파일도 포함해야 한다.
대표적으로 DB 접속 비밀번호 관리 문제가 있다.

이런 문제는 실행환경용 설정을 `*.jar` 파일 외부에서 제공하는 방법으로 간단하게 해결할 수 있다.

잦은 패키징 문제는 단순 재실행으로 대신할 수 있고,
보안 문제는 각 환경 담당자만 접근 권한을 가지는 것으로 해결할 수 있다.

방법은 간단해서, 워킹 디렉토리를 기준으로 `[WORKING DIRECTORY]/config/application.yml`로 설정 파일을 옮기면 된다.

```
.
├── config
│   └── application.yml
└── src/main
    ├── ... 생략 ...
    └── resources
        └── templates
            └── _
                ├── index.html
                ├── newbie.html
                └── signup.html
```
[전체 구조](step_4_rev_1_tree.txt)

그런데 `spring.jpa.open-in-view` 같은 실행환경에 의존성이 없는 설정은 공통으로 사용하고,
환경에 의존적인 설정만 추가하거나 덮어써서 하는 것이 문제가 생길 위험이 낮아진다.

* `spring.datasource.hikari.*` : 공통(기본) 설정은 `src/main/resources/application.yml`에서, 환경별 설정은 `config/application.yml`에서 가져와서 전체 설정을 가져온다.
* `spring.jpa.*` : 공통 설정인 `src/main/resources/application.yml`에서 가져온다.
* `spring.thymeleaf.cache` : `src/main/resources/application.yml`의 설정을 `config/application.yml`에서 덮어쓴다.

```yaml
spring:
  datasource:
    hikari:
      jdbc-url: jdbc:mysql://localhost/tutorial_spring_web?connectionCollation=utf8mb4_bin
      username: root
      password: ''
  thymeleaf:
    cache: false
```
[`config/application.yml`](../../config/application.yml)

```yaml
spring:
  datasource:
    hikari:
      driver-class-name: com.mysql.jdbc.Driver
      idle-timeout: 12000
      connection-test-query: SELECT 1
  jpa:
    database: mysql
    open-in-view: false
  thymeleaf:
    cache: true
```
[`src/main/resources/application.yml`](../../src/main/resources/application.yml)

### 프로젝트 구조

```
.
├── config
│   └── application.yml
└── src/main
    ├── ... 생략 ...
    └── resources
        ├── application.yml
        └── templates
            └── _
                ├── index.html
                ├── newbie.html
                └── signup.html
```
[전체 구조](step_4_rev_2_tree.txt)

## STEP 5 - 프론트엔드와 백엔드 파일 분리하기

현재 구조는 백엔드인 Java 파일과 프론트엔드인 Thymeleaf 템플릿이 함께(`src/main/*`) 있고
패키징한 `*.jar` 파일이 Java 바이트코드와 Thymeleaf 템플릿을 모두 포함한다.

프론트엔드와 백엔드는 변경하는 이유도 시점도 주기도 필요한 전문성도 모두 다르다.
따라서 한꺼번에 배포하는 것 보다는 따로 배포하는 적합하다.

그러니까, 프론트엔드 소스도 설정 파일과 마찬가지로 외부로 뺄 필요가 있다.

`./src/main/resources/tepmplates` 디렉토리를 `./templates`로 옮기고 설정에 반영한다.

이렇게 패키징에서 제외해 외부로 빼면, `th:replace`, `th:include` 등의
UI 모듈화를 프론트엔드에서 독립적으로 수행할 수 있게 된다.

```yaml
spring:
  #  ... 생략 ...
  thymeleaf:
    cache: true
    check-template: true
    check-template-location: true
    enabled: true
    enable-spring-el-compiler: false
    encoding: UTF-8
    mode: HTML
    prefix: file:./templates/
    suffix: .html
    reactive:
      max-chunk-size: 0
    servlet:
      content-type: text/html
```
[src/main/resources/application.yml](../../src/main/resources/application.yml)

> 얼마간 Spring Boot에서 설정의 `spring.thymeleaf` 항목을 모두 적지 않고
> `config/application.yml`파일에 일부만 작성할 경우(`spring.thymeleaf.cache` 등),
> `src/main/resources/application.yml`의 설정을 무시하는 문제가 있었다.
> 지금은 수정되었는지는 아직 모르겠다.

### 프로젝트 구조

```
.
├── config
│   └── application.yml
├── src/main
│   ├── java
│   │   └── hemoptysisheart.github.com.tutorial.spring.web
│   │       ├── borderline
│   │       │   ├── AccountBorderline.java
│   │       │   ├── BorderlineConfiguration.java
│   │       │   ├── cmd
│   │       │   │   └── CreateAccountCmd.java
│   │       │   └── po
│   │       │       └── AccountPo.java
│   │       ├── configuration
│   │       │   ├── ApplicationConfiguration.java
│   │       │   ├── JpaConfiguration.java
│   │       │   └── WebMvcConfiguration.java
│   │       ├── controller
│   │       │   ├── ControllerConfiguration.java
│   │       │   ├── RootController.java
│   │       │   └── req
│   │       │       └── SignUpReq.java
│   │       ├── dao
│   │       │   ├── AccountDao.java
│   │       │   └── DaoConfiguration.java
│   │       ├── jpa
│   │       │   ├── entity
│   │       │   │   ├── AccountEntity.java
│   │       │   │   └── EntityConfiguration.java
│   │       │   └── repository
│   │       │       ├── AccountRepository.java
│   │       │       └── RepositoryConfiguration.java
│   │       ├── runner
│   │       │   └── ApplicationRunner.java
│   │       └── service
│   │           ├── AccountService.java
│   │           ├── ServiceConfiguration.java
│   │           └── params
│   │               └── CreateAccountParams.java
│   └── resources
│       └── application.yml
└── templates
    └── _
        ├── index.html
        ├── newbie.html
        └── signup.html
```
[전체 구조](step_5_tree.txt)

## STEP 6 - 스프링 컴포넌트에 인터페이스 사용하기

단위 테스트 작성이나 같은 기능을 여러 방식으로 구현해야 할 필요가 있을 때 하는 것이 좋지만,
이 이상 클래스로 작성했다가 나중에 한번에 바꾸기엔 양이 많아 ~귀찮으니까~ 미리 인터페이스로 분리한다.

2부분을 변경한다.

1. 인터페이스를 분리한다.
1. 클래스의 접근 제한제를 `public`에서 `(default)`로 변경한다.

웹MVC 컨트롤러에 맞는 메서드만 남긴다.

```java
package hemoptysisheart.github.com.tutorial.spring.web.controller;

// ... 생략 ...

@RequestMapping
public interface RootController {
    @GetMapping
    String index(Model model);

    @GetMapping("/signup")
    String signUpForm(Model model);

    @PostMapping("/signup")
    String signUp(@ModelAttribute("signUpReq") @Valid SignUpReq signUpReq, BindingResult binding, Model model);
}
```
[RootController.java](../../src/main/java/hemoptysisheart/github/com/tutorial/spring/web/controller/RootController.java)

구현의 접근제한자를 디폴트로 변경해서 패키지 외부에서는 접근하지 못하고,
단위테스트에서는 접근할 수 있도록 열어둔다.
Java 9 이후의 모듈 시스템을 활용하면, 구현을 별도 패키지로 빼고 모듈 외부에 노출하지 않는 방법도 사용할 수 있다.

```java
package hemoptysisheart.github.com.tutorial.spring.web.controller;

// ... 생략 ...

@Controller
class RootControllerImpl implements RootController {
    // ... 생략 ...
}
```
[RootControllerImpl.java](../../src/main/java/hemoptysisheart/github/com/tutorial/spring/web/controller/RootControllerImpl.java)

### 프로젝트 구조

```
./src/main/java
└── hemoptysisheart.github.com.tutorial.spring.web
    ├── borderline
    │   ├── AccountBorderline.java
    │   ├── AccountBorderlineImpl.java
    │   ├── BorderlineConfiguration.java
    │   ├── cmd
    │   │   └── CreateAccountCmd.java
    │   └── po
    │       └── AccountPo.java
    ├── configuration
    │   ├── ApplicationConfiguration.java
    │   ├── JpaConfiguration.java
    │   └── WebMvcConfiguration.java
    ├── controller
    │   ├── ControllerConfiguration.java
    │   ├── RootController.java
    │   ├── RootControllerImpl.java
    │   └── req
    │       └── SignUpReq.java
    ├── dao
    │   ├── AccountDao.java
    │   ├── AccountDaoImpl.java
    │   └── DaoConfiguration.java
    ├── jpa
    │   ├── entity
    │   │   ├── AccountEntity.java
    │   │   └── EntityConfiguration.java
    │   └── repository
    │       ├── AccountRepository.java
    │       └── RepositoryConfiguration.java
    ├── runner
    │   └── ApplicationRunner.java
    └── service
        ├── AccountService.java
        ├── AccountServiceImpl.java
        ├── ServiceConfiguration.java
        └── params
            └── CreateAccountParams.java
```
[전체 구조](step_6_tree.txt)