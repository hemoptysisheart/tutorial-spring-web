# Ch.06 - 서비스 레이어

비지니스 로직을 가진 서비스 레이어의 컴포넌트를 추가한다.

## STEP 1 - 컴포넌트 추가

어쨌든 입력한 계정 정보를 저장하는 서비스 컴포넌트를 추가한다.

```java
package hemoptysisheart.github.com.tutorial.spring.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public AccountEntity create(String email, String nickname, String password) {
        AccountEntity account = new AccountEntity(email, nickname, password);
        account = this.accountRepository.save(account);
        return account;
    }
}
```
[AccountService.java](../../src/main/java/hemoptysisheart/github/com/tutorial/spring/web/AccountService.java)

컨트롤러에는 서비스 컴포넌트를 호출하는 코드를 추가한다.

```java
package hemoptysisheart.github.com.tutorial.spring.web;

// ... 생략 ...

@Controller
public class RootController {
    @Autowired
    private AccountService accountService;

    // ... 생략 ...

    @PostMapping("/signup")
    public String signUp(@ModelAttribute("signUpReq") @Valid final SignUpReq signUpReq, final BindingResult binding, final Model model) {
        // ... 생략 ...

        if (binding.hasErrors()) {
            return "_/signup";
        } else {
            AccountEntity account = this.accountService.create(signUpReq.getEmail(), signUpReq.getNickname(), signUpReq.getPassword());
            return "redirect:/";
        }
    }
}
```
[RootController.java](../../src/main/java/hemoptysisheart/github/com/tutorial/spring/web/RootController.java)

새 계정 정보.

![새 계정 정보](step_1_new_account_form.png)

저장된 계정 정보.

![저장된 계정](step_1_inserted_account.png)

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
│                           ├── AccountEntity.java
│                           ├── AccountRepository.java
│                           ├── AccountService.java
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
[전체 구조](step_1_tree.txt)

## STEP 2 - 속성의 변화에 대응하기

유저가 입력하는 계정 정보는 언제라도 바뀔 수 있다.
설혹 계정 정보는 바뀌지 않는다 하더라도, 다른 데이터는 선택하거나 입력할 수 있는 항목은 얼마든지 바뀔 수 있다.
메서드 인자가 늘거나 줄어든다면 메서드 시그니쳐가 바뀌고, 메서드를 사용하는 모든 코드를 수정해야 한다.
늘어난 항목은 기본값이 있어서 사용하지 않으면 자동으로 설정할 수 있음에도 불구하고.

이 문제는 로직에 필요한 데이터를 직접 인자로 넘기지 않고,
별도의 클래스로 묶어서 넘길 수 있다.

```java
package hemoptysisheart.github.com.tutorial.spring.web;

public class CreateAccountParams {
    private String email;
    private String nickname;
    private String password;

    // ... 생성자 메서드 ...

    // ... getters & setters ...
}
```
[CreateAccountParams.java](../../src/main/java/hemoptysisheart/github/com/tutorial/spring/web/CreateAccountParams.java)

인자를 바로 `new AccountEntity(...)`에 인자로 넘기지 않고, `params`에서 필요한 필드를 가져온다.

```java
public AccountEntity create(CreateAccountParams params) {
    AccountEntity account = new AccountEntity(params.getEmail(), params.getNickname(), params.getPassword());
    account = this.accountRepository.save(account);
    return account;
}
```
[AccountService.java](../../src/main/java/hemoptysisheart/github/com/tutorial/spring/web/AccountService.java)

바인딩된 데이터를 직접 메서드 인자로 넘기지 않고 `CreateAccountParams` 인스턴스를 만들어 넘긴다.

```java
@PostMapping("/signup")
public String signUp(@ModelAttribute("signUpReq") @Valid final SignUpReq signUpReq, final BindingResult binding, final Model model) {
    // ...
    if (binding.hasErrors()) {
        return "_/signup";
    } else {
        CreateAccountParams params = new CreateAccountParams(signUpReq.getEmail(), signUpReq.getNickname(), signUpReq.getPassword());
        AccountEntity account = this.accountService.create(params);
        return "redirect:/";
    }
}
```
[RootController.java](../../src/main/java/hemoptysisheart/github/com/tutorial/spring/web/RootController.java)

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
│                           ├── AccountEntity.java
│                           ├── AccountRepository.java
│                           ├── AccountService.java
│                           ├── ApplicationRunner.java
│                           ├── CreateAccountParams.java
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
[전체 구조](step_2_tree.text)