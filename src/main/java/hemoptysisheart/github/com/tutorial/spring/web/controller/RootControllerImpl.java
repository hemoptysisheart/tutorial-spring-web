package hemoptysisheart.github.com.tutorial.spring.web.controller;

import hemoptysisheart.github.com.tutorial.spring.web.borderline.AccountBorderline;
import hemoptysisheart.github.com.tutorial.spring.web.borderline.cmd.CreateAccountCmd;
import hemoptysisheart.github.com.tutorial.spring.web.borderline.po.AccountPo;
import hemoptysisheart.github.com.tutorial.spring.web.controller.req.SignUpReq;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.validation.Valid;
import java.time.ZonedDateTime;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author hemoptysisheart
 * @since 2018. 5. 22.
 */
@Controller
class RootControllerImpl implements RootController {
    private static final Logger log = getLogger(RootControllerImpl.class);

    @Autowired
    private AccountBorderline accountBorderline;

    /**
     * 계정 등록 폼 페이지 출력에 필요한 {@link Model} 설정과 템플릿 선택을 담당한다.
     *
     * @param model
     * @return
     */
    private String doSignUpForm(Model model) {
        if (!model.containsAttribute("signUpReq")) {
            // 바인딩 에러로 등록 폼을 다시 출력해야 할 경우,
            // 기존 입력값 유지와 BindingResult 보호를 위해
            // 기존 입력값이 없을 때만 어트리뷰트를 추가한다.
            model.addAttribute("signUpReq", new SignUpReq());
        }
        return "_/signup";
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // hemoptysisheart.github.com.tutorial.spring.web.controller.RootController
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public String index(final Model model) {
        if (log.isTraceEnabled()) {
            // 메서드 입력 로그.
            log.trace(format("index args : model=%s", model));
        }

        // 별도의 로직이 필요없는 현재 시각 정보를 뷰로 넘긴다.
        model.addAttribute("timestamp", ZonedDateTime.now());

        String template = "_/index";
        if (log.isTraceEnabled()) {
            // 메서드 출력(혹은 결과) 로그.
            log.trace(format("index result : template='%s', model=%s", template, model));
        }
        return template;
    }

    @Override
    public String signUpForm(final Model model) {
        if (log.isTraceEnabled()) {
            log.trace(format("signUpForm args : model=%s", model));
        }

        String template = doSignUpForm(model);

        if (log.isTraceEnabled()) {
            log.trace(format("signUpForm result : template='%s', model=%s", template, model));
        }
        return template;
    }

    @Override
    public String signUp(@ModelAttribute("signUpReq") @Valid final SignUpReq signUpReq, final BindingResult binding, final Model model) {
        if (log.isTraceEnabled()) {
            log.trace(format("signUp args : signUpReq=%s, binding=%s, model=%s", signUpReq, binding, model));
        }

        if (!signUpReq.getPassword().equals(signUpReq.getConfirm())) {
            binding.addError(new FieldError("signUpReq", "confirm", "password does not match."));
        }

        String template;
        if (binding.hasErrors()) {
            template = doSignUpForm(model);
        } else {
            CreateAccountCmd cmd = new CreateAccountCmd(signUpReq.getEmail(), signUpReq.getNickname(), signUpReq.getPassword());
            AccountPo account = this.accountBorderline.create(cmd);
            model.addAttribute("account", account);
            template = "_/newbie";
        }

        if (log.isTraceEnabled()) {
            log.trace(format("signUp result : template='%s', model=%s", template, model));
        }
        return template;
    }
}