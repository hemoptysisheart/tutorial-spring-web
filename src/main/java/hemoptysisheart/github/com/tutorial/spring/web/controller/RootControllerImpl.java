package hemoptysisheart.github.com.tutorial.spring.web.controller;

import hemoptysisheart.github.com.tutorial.spring.web.borderline.AccountBorderline;
import hemoptysisheart.github.com.tutorial.spring.web.borderline.cmd.CreateAccountCmd;
import hemoptysisheart.github.com.tutorial.spring.web.borderline.po.AccountPo;
import hemoptysisheart.github.com.tutorial.spring.web.controller.req.SignUpReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.validation.Valid;
import java.time.ZonedDateTime;

/**
 * @author hemoptysisheart
 * @since 2018. 5. 22.
 */
@Controller
class RootControllerImpl implements RootController {
    @Autowired
    private AccountBorderline accountBorderline;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // hemoptysisheart.github.com.tutorial.spring.web.controller.RootController
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public String index(final Model model) {
        // 별도의 로직이 필요없는 현재 시각 정보를 뷰로 넘긴다.
        model.addAttribute("timestamp", ZonedDateTime.now());

        // 뷰 템플릿 출력을 시험하기 위해 다른 로직 없이 템플릿 이름을 리턴한다.
        return "_/index";
    }

    @Override
    public String signUpForm(final Model model) {
        model.addAttribute("signUpReq", new SignUpReq());
        return "_/signup";
    }

    @Override
    public String signUp(@ModelAttribute("signUpReq") @Valid final SignUpReq signUpReq, final BindingResult binding, final Model model) {
        if (!signUpReq.getPassword().equals(signUpReq.getConfirm())) {
            binding.addError(new FieldError("signUpReq", "confirm", "password does not match."));
        }

        if (binding.hasErrors()) {
            return "_/signup";
        } else {
            CreateAccountCmd cmd = new CreateAccountCmd(signUpReq.getEmail(), signUpReq.getNickname(), signUpReq.getPassword());
            AccountPo account = this.accountBorderline.create(cmd);
            model.addAttribute("account", account);
            return "_/newbie";
        }
    }
}