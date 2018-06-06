# CH.09 - 안정성 개선

프로그램의 개발, 운영, 테스트에서 안정성을 높일 수 있는 조치를 취한다.
단위 테스트는 별도로 다룬다.

## STEP 1 - 코드 공통화 : 폼 페이지 출력

중복 코드를 공유하도록 변경해 버그 발생 가능성을 낮춘다.

`doSignUpForm(Model)` 메서드로 계정 등록 폼을 출력할 때 필요한 모델 설정과 뷰 선택을 공통화한다.

```java
package hemoptysisheart.github.com.tutorial.spring.web.controller;

// ... 생략 ...

@Controller
class RootControllerImpl implements RootController {
    // ... 생략 ...

    private String doSignUpForm(Model model) {
        if (!model.containsAttribute("signUpReq")) {
            model.addAttribute("signUpReq", new SignUpReq());
        }
        return "_/signup";
    }

    @Override
    public String signUpForm(final Model model) {
        return doSignUpForm(model);
    }

    @Override
    public String signUp(@ModelAttribute("signUpReq") @Valid final SignUpReq signUpReq, final BindingResult binding, final Model model) {
        if (!signUpReq.getPassword().equals(signUpReq.getConfirm())) {
            binding.addError(new FieldError("signUpReq", "confirm", "password does not match."));
        }

        if (binding.hasErrors()) {
            return doSignUpForm(model);
        } else {
            // ... 생략 ...
        }
    }
}
```
[RootControllerImpl.java](../../src/main/java/hemoptysisheart/github/com/tutorial/spring/web/controller/RootControllerImpl.java)

## STEP 2 - 디버깅 로그

디버깅을 위해 각 메서드의 입출력 로그를 남긴다.
모든 메서드에 대해 모든 입출력 로그를 남긴다면 성능에 문제가 생긴다.

로그 레벨과 실행환경 설정으로 개발 환경(특히 로컬)에서만 메서드 입출력 로그를 남긴다.

### 애플리케이션 기본 설정

1. 기본 설정에서는 경고(`warn`) 이상의 로그만 남기도록 지정한다.

```yaml
# ... 생략 ...
logging:
  level:
    root: warn
```
[src/main/resources/application.yml](../../src/main/resources/application.yml)

### 실행환경별 설정

1. 로그를 남긴 코드 위치를 정확히 파악할 수 있도록 정보(전체 로거 이름, 메서드 이름, 라인번호)를 추가한다.
1. 이름이 `hemoptysisheart.github.com.tutorial.spring.web`로 시작하면 트레이스(`trace`) 이상의 로그를 남기도록 지정한다.

```yaml

logging:
  pattern:
    console: '%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(-%5p) %clr(---){faint} %clr([%15.15t]){faint} %clr(%logger#%M [L.%L]){cyan} %clr(:){faint} %m%n%wEx'
  level:
    hemoptysisheart.github.com.tutorial.spring.web: trace
```
[config/application.yml](../../config/application.yml)

### 로깅 코드

메서드 입출력 로그를 남기는 코드를 출력한다.
로그를 출력하기 전에 해당 레벨의 로그를 출력할 수 있는지 확인해서(`log.isTraceEnabled()`)
불필요한 문자열을 생성하지 않도록 한다.

```java
package hemoptysisheart.github.com.tutorial.spring.web.controller;

// ... 생략 ...

@Controller
class RootControllerImpl implements RootController {
    private static final Logger log = getLogger(RootControllerImpl.class);

    // ... 생략 ...

    @Override
    public String index(final Model model) {
        if (log.isTraceEnabled()) {
            log.trace(format("index args : model=%s", model));
        }

        model.addAttribute("timestamp", ZonedDateTime.now());

        String template = "_/index";
        if (log.isTraceEnabled()) {
            log.trace(format("index result : template='%s', model=%s", template, model));
        }
        return template;
    }

    // ... 생략 ...
}
```
[RootControllerImpl.java](../../src/main/java/hemoptysisheart/github/com/tutorial/spring/web/controller/RootControllerImpl.java)

## STEP 3 - 모니터링 로그

모니터링 로그는 로직 오류를 찾기위한 디버깅 로그와는 달리,
로그 데이터를 영구적으로 저장한 후 해당 데이터를 분석하는 과정을 전제로 한다.

데이터 분석의 목적은 사용자 행동 분석과 개선이 필요한 부분 찾기 등으로,
이후의 개발 작업 및 방향을 정하는 의사결정에 필요한 기초 자료를 확보하는 활동이다.

비지니스 로직은 JVM 내부의 일이고, 이건 밖에선 보이지 않는 부분이다.
모니터링 로그는 이 블랙박스에서 필요한 정보를 밖으로 내보내주는 역할을 한다.
예를 들면 특정 로직 실행에 걸리는 시간이라던지, 예외가 발생하는 인자의 정보라던지.

다음의 변경사항으로 전체 응답시간 내에서 계정 등록 비지니스 로직(`AccountService.create(CreateAccountParams)`)이 차지하는 부분을 알려준다.
이 정보와 서블릿 컨테이너 혹은 로드밸러서의 로그와 함께 분석하면 어느 부분의 성능 개선이 필요한지 파악할 수 있다.

### 로그 설정

분석 기초 자료는 영구 저장이 기본이고, 영구 저장의 기본은 __파일로 저장하기__ 이다.
파일로 저장할 수 있도록 실행환경에 맞춰 저장 경로를 지정한다.

* 워킹 디렉토리의 `log` 디렉토리에 로그 파일을 저장한다(`logging.path`).
* 개발 환경에서 테스트 용도로 로그 파일의 크기(`logging.file.max-size`)를 작게 설정한다.
* 성능 부하를 줄이도록 메서드 이름, 행 번호, 색상을 제거한 패턴을 지정한다(`logging.pattern.file`).
* 로그 항목의 구분자를 ` --- `로 변경.

> `logging.pattern.console`의 패턴은 Spring Boot의 기본 로그 패턴에서 로거 이름 제한 삭제, 메서드 이름 및 행 번호를 추가한 패턴이다,

```yaml
logging:
  path: ./log
  file:
    max-size: 10KB
  pattern:
    console: '%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} - %clr(%5p) %clr(---){faint} %clr([%15.15t]){faint} %clr(%logger#%M [L.%L]){cyan} %clr(:){faint} %m%n%wEx'
    file: '%d{yyyy-MM-dd HH:mm:ss.SSS} --- %p --- [%t] %logger --- %m%n%wEx'
```
[config/application.yml](../../config/application.yml)

### 로그 생성

내부 로직의 계정 등록에 필요한 성능을 알 수 있는 시간 정보를 만들어 로그를 저장한다(로직 시작 시각, 로직 종료 시각, 로직 경과 시간, 생성 데이터).

```java
package hemoptysisheart.github.com.tutorial.spring.web.service;

// ... 생략 ...

@Service
class AccountServiceImpl implements AccountService {
    private static final Logger log = getLogger(AccountServiceImpl.class);

    // ... 생략 ...

    @Override
    public Account create(CreateAccountParams params) {
        // ... 생략 ...

        Instant start = Instant.now();
        // ... 생략 ...
        Instant end = Instant.now();

        if (log.isInfoEnabled()) {
            log.info(format("account creation : start=%s, end=%s, elapsed=%s, account=%s", start, end, Duration.between(start, end), account));
        }

        // ... 생략 ...
        return account;
    }
}
```
[AccountServiceImpl.java](../../src/main/java/hemoptysisheart/github/com/tutorial/spring/web/service/AccountServiceImpl.java)

14행에서 다음 로그를 확인할 수 있다.

```
2018-06-06 14:15:18.013 --- INFO --- [http-nio-8080-exec-3] hemoptysisheart.github.com.tutorial.spring.web.service.AccountServiceImpl --- account creation : start=2018-06-06T05:15:17.972846Z, end=2018-06-06T05:15:18.013078Z, elapsed=PT0.040232S, account=AccountEntity{id=1, email='a@a', nickname='AA', password=[ PROTECTED ]}
```
[전체 로그](step_3_file_log.log)

> 정확한 시각 정보를 사용하려면 실행환경의 시각 정보를 일치시킬 수 있는 타임서버 설정이 필요하다.
> 실행환경이 자체 IDC에서 폐쇄망을 사용하는 경우라면 신경써야 한다.

### 프로젝트 구조

```
.
├── config
│   └── application.yml
├── db
│   ├── tutorial_spring_web.mwb
│   └── tutorial_spring_web.sql
├── log
│   └── .gitlock
└── src/main
    ├── java
    │   └── hemoptysisheart.github.com.tutorial.spring.web
    │       ├── borderline
    │       │   ├── AccountBorderline.java
    │       │   ├── AccountBorderlineImpl.java
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
    │       │   ├── RootControllerImpl.java
    │       │   └── req
    │       │       └── SignUpReq.java
    │       ├── dao
    │       │   ├── AccountDao.java
    │       │   ├── AccountDaoImpl.java
    │       │   └── DaoConfiguration.java
    │       ├── domain
    │       │   ├── Account.java
    │       │   └── DomainConfiguration.java
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
    │           ├── AccountServiceImpl.java
    │           ├── ServiceConfiguration.java
    │           └── params
    │               └── CreateAccountParams.java
    └── resources
        └── application.yml
```
[전체 구조](step_3_tree.txt)