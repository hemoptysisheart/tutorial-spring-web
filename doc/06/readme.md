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