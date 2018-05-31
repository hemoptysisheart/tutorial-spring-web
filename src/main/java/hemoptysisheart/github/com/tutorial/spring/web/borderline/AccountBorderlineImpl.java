package hemoptysisheart.github.com.tutorial.spring.web.borderline;

import hemoptysisheart.github.com.tutorial.spring.web.borderline.cmd.CreateAccountCmd;
import hemoptysisheart.github.com.tutorial.spring.web.borderline.po.AccountPo;
import hemoptysisheart.github.com.tutorial.spring.web.domain.Account;
import hemoptysisheart.github.com.tutorial.spring.web.service.AccountService;
import hemoptysisheart.github.com.tutorial.spring.web.service.params.CreateAccountParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hemoptysisheart
 * @since 2018. 5. 25.
 */
@Service
class AccountBorderlineImpl implements AccountBorderline {
    @Autowired
    private AccountService accountService;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // hemoptysisheart.github.com.tutorial.spring.web.borderline.AccountBorderline
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public AccountPo create(CreateAccountCmd cmd) {
        CreateAccountParams params = new CreateAccountParams(
                cmd.getEmail(),
                cmd.getNickname(),
                cmd.getPassword());
        Account account = this.accountService.create(params);
        AccountPo po = new AccountPo(
                account.getId(),
                account.getEmail(),
                account.getNickname());
        return po;
    }
}