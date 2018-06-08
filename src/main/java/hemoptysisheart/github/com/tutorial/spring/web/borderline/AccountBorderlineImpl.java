package hemoptysisheart.github.com.tutorial.spring.web.borderline;

import hemoptysisheart.github.com.tutorial.spring.web.borderline.cmd.CreateAccountCmd;
import hemoptysisheart.github.com.tutorial.spring.web.borderline.po.AccountPo;
import hemoptysisheart.github.com.tutorial.spring.web.domain.Account;
import hemoptysisheart.github.com.tutorial.spring.web.service.AccountService;
import hemoptysisheart.github.com.tutorial.spring.web.service.params.CreateAccountParams;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author hemoptysisheart
 * @since 2018. 5. 25.
 */
@Service
class AccountBorderlineImpl implements AccountBorderline {
    private static final Logger log = getLogger(AccountBorderlineImpl.class);

    @Autowired
    private AccountService accountService;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // hemoptysisheart.github.com.tutorial.spring.web.borderline.AccountBorderline
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public AccountPo create(CreateAccountCmd cmd) {
        if (log.isTraceEnabled()) {
            log.trace(format("create args : cmd=%s", cmd));
        }

        CreateAccountParams params = new CreateAccountParams(
                cmd.getEmail(),
                cmd.getNickname(),
                cmd.getPassword());
        Account account = this.accountService.create(params);
        AccountPo po = new AccountPo(
                account.getId(),
                account.getEmail(),
                account.getNickname());

        if (log.isTraceEnabled()) {
            log.trace(format("create return : %s", po));
        }
        return po;
    }
}