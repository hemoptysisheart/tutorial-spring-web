# Ch.04 - 계정을 만들자

## STEP 1 - 회원가입 폼 출력하기

컨트롤러에 회원가입 폼을 출력하고 폼 서브밋을 처리하는 메서드를 추가한다`

* `@GetMapping("/signup") public String signUpForm(final Model model)` : `http://localhost:8080/signup` URL에 폼 페이지를 출력한다.
* `@PostMapping("/signup") public String signUp(@ModelAttribute @Valid final SignUpReq signUpReq, final Model model)` : 회원가입 폼 서브밋을 받아 처리한다.

```java
package hemoptysisheart.github.com.tutorial.spring.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.time.ZonedDateTime;

@Controller
public class RootController {
    @GetMapping
    public String index(final Model model) {
        model.addAttribute("timestamp", ZonedDateTime.now());
        return "_/index";
    }

    @GetMapping("/signup")
    public String signUpForm(final Model model) {
        model.addAttribute("signUpReq", new SignUpReq());
        return "_/signup";
    }

    @PostMapping("/signup")
    public String signUp(@ModelAttribute @Valid final SignUpReq signUpReq, final Model model) {
        return "redirect:/";
    }
}
```

폼 정보를 바인딩할 인스턴스를 추가한다. 폼 데이터의 형식 기반 검증을 할 수 있는 어노테이션을 추가한다.

```java
package hemoptysisheart.github.com.tutorial.spring.web;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SignUpReq {
    @NotNull
    @Email
    private String email;
    @NotNull
    @Size(min = 1)
    private String nickname;
    @NotNull
    @Size(min = 4)
    private String password;
    @NotNull
    @Size(min = 4)
    private String confirm;

    // Getter, Setter and toString()
}
```

회원가입 폼 출력에 사용한 템플릿(`_/signup`)을 추가한다.

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8"/>
    <title>Sign-Up</title>
</head>
<body>
<header>
    <nav>
        <a href="index.html" th:href="@{/}">TOP</a>
        <a href="signup.html" th:href="@{/signup}">sign-up</a>
        <a href="login.html" th:href="@{/login}">log-in</a>
    </nav>
</header>
<main>
    <form id="form-signup" action="index.html" method="post"
          th:action="@{/signup}"></form>
    <!--/*@thymesVar id="signUpReq" type="hemoptysisheart.github.com.tutorial.spring.web.SignUpReq"*/-->
    <div th:object="${signUpReq}">
        <div>
            <label for="email">E-MAIL</label>
            <input type="email" id="email" form="form-signup" required="required"
                   th:field="*{email}"/>
        </div>
        <div>
            <label for="nickname">NICKNAME</label>
            <input id="nickname" form="form-signup" required="required"
                   th:field="*{nickname}"/>
        </div>
        <div>
            <label for="password">PASSWORD</label>
            <input type="password" id="password" form="form-signup" required="required" pattern=".{4,}"
                   th:field="*{password}"/>
        </div>
        <div>
            <label for="confirm">PW CONFIRM</label>
            <input type="password" id="confirm" form="form-signup" required="required" pattern=".{4,}"
                   th:field="*{confirm}"/>
        </div>
        <div>
            <button form="form-signup">SUBMIT</button>
            <button type="reset" form="form-signup">RESET</button>
        </div>
    </div>
</main>
</body>
</html>
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