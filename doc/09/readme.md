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