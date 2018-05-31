package hemoptysisheart.github.com.tutorial.spring.web.borderline;

import hemoptysisheart.github.com.tutorial.spring.web.borderline.cmd.CreateAccountCmd;
import hemoptysisheart.github.com.tutorial.spring.web.borderline.po.AccountPo;
import org.springframework.transaction.annotation.Transactional;

/**
 * 트랜잭션의 시작과 종료, JPA 엔티티가 명시적인 {@link Transactional}영역 밖으로 나가지 않도록 관리한다.
 *
 * @author hemoptysisheart
 * @since 2018. 5. 31.
 */
@Transactional
public interface AccountBorderline {
    AccountPo create(CreateAccountCmd cmd);
}