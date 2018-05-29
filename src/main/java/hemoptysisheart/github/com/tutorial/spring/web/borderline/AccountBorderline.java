package hemoptysisheart.github.com.tutorial.spring.web.borderline;

import hemoptysisheart.github.com.tutorial.spring.web.jpa.entity.AccountEntity;
import hemoptysisheart.github.com.tutorial.spring.web.service.AccountService;
import hemoptysisheart.github.com.tutorial.spring.web.service.CreateAccountParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 트랜잭션의 시작과 종료, JPA 엔티티가 명시적인 {@link Transactional}영역 밖으로 나가지 않도록 관리한다.
 *
 * @author hemoptysisheart
 * @since 2018. 5. 25.
 */
@Transactional
@Service
public class AccountBorderline {
    @Autowired
    private AccountService accountService;

    public AccountPo create(CreateAccountCmd cmd) {
        CreateAccountParams params = new CreateAccountParams(
                cmd.getEmail(),
                cmd.getNickname(),
                cmd.getPassword());
        AccountEntity account = this.accountService.create(params);
        AccountPo po = new AccountPo(
                account.getId(),
                account.getEmail(),
                account.getNickname());
        return po;
    }
}