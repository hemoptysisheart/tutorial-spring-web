package hemoptysisheart.github.com.tutorial.spring.web.service;

import hemoptysisheart.github.com.tutorial.spring.web.dao.AccountDao;
import hemoptysisheart.github.com.tutorial.spring.web.domain.Account;
import hemoptysisheart.github.com.tutorial.spring.web.jpa.entity.AccountEntity;
import hemoptysisheart.github.com.tutorial.spring.web.service.params.CreateAccountParams;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * 계정 관리에 관련된 로직.
 *
 * @author hemoptysisheart
 * @since 2018. 5. 24.
 */
@Service
class AccountServiceImpl implements AccountService {
    private static final Logger log = getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // hemoptysisheart.github.com.tutorial.spring.web.service.AccountService
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public Account create(CreateAccountParams params) {
        if (log.isTraceEnabled()) {
            log.trace(format("create args : params=%s", params));
        }

        Instant start = Instant.now();

        Account account = new AccountEntity(params.getEmail(), params.getNickname(),
                passwordEncoder.encode(params.getPassword()));
        account = this.accountDao.insert(account);

        Instant end = Instant.now();

        if (log.isInfoEnabled()) {
            log.info(format("account creation : start=%s, end=%s, elapsed=%s, account=%s", start, end, Duration.between(start, end), account));
        }

        if (log.isTraceEnabled()) {
            log.trace(format("create return : %s", account));
        }
        return account;
    }
}