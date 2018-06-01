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