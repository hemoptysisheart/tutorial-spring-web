package hemoptysisheart.github.com.tutorial.spring.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.time.ZonedDateTime;

/**
 * 루트 페이지를 모아 제공하는 컨트롤러.
 * 컨트롤러 인터페이스/클래스는 어떤 URL 그룹에 해당한다.
 *
 * @author hemoptysisheart
 * @since 2018. 5. 22.
 */
@Controller
public class RootController {
    /**
     * 브라우저에 {@code /} 도메인만 입력했을 경우의 리퀘스트를 처리하는 컨트롤러 메서드.
     *
     * @param model HTTP 리퀘스트가 컨트롤러 전달하는 정보. 컨트롤러 로직 실행을 끝내고 뷰 템플릿 엔진에 데이터를 전달할 때도 사용한다.
     * @return 루트 페이지의 템플릿 이름.
     */
    @GetMapping
    public String index(final Model model) {
        // 별도의 로직이 필요없는 현재 시각 정보를 뷰로 넘긴다.
        model.addAttribute("timestamp", ZonedDateTime.now());

        // 뷰 템플릿 출력을 시험하기 위해 다른 로직 없이 템플릿 이름을 리턴한다.
        return "_/index";
    }

    /**
     * 회원 가입 폼을 출력한다.
     *
     * @param model
     * @return 회원가입 폼 템플릿 이름.
     */
    @GetMapping("/signup")
    public String signUpForm(final Model model) {
        model.addAttribute("signUpReq", new SignUpReq());
        return "_/signup";
    }

    /**
     * 회원 가입 요청을 처리한 후, 루트 페이지로 리다이렉트한다.
     *
     * @param signUpReq
     * @param model
     * @return 루트 페이지 리다이렉트 정보.
     */
    @PostMapping("/signup")
    public String signUp(@ModelAttribute @Valid final SignUpReq signUpReq, final Model model) {
        return "redirect:/";
    }
}