package hemoptysisheart.github.com.tutorial.spring.web.dao;

import hemoptysisheart.github.com.tutorial.spring.web.domain.Account;
import hemoptysisheart.github.com.tutorial.spring.web.jpa.entity.AccountEntity;
import hemoptysisheart.github.com.tutorial.spring.web.jpa.repository.AccountRepository;
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
class AccountDaoImpl implements AccountDao {
    private static final Logger log = getLogger(AccountDaoImpl.class);

    @Autowired
    private AccountRepository accountRepository;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // hemoptysisheart.github.com.tutorial.spring.web.dao.AccountDao
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public Account insert(Account account) {
        if (log.isTraceEnabled()) {
            log.trace(format("insert args : account=%s", account));
        }

        account = this.accountRepository.save((AccountEntity) account);

        if (log.isTraceEnabled()) {
            log.trace(format("insert return : %s", account));
        }
        return account;
    }

    @Override
    public Account select(String nickname) {
        if (log.isTraceEnabled()) {
            log.trace(format("select args : nickname=%s", nickname));
        }

        Account account = this.accountRepository.findOneByNickname(nickname);

        if (log.isTraceEnabled()) {
            log.trace(format("select return : %s", account));
        }
        return account;
    }

    @Override
    public Account selectWhereEmail(String email) {
        if (log.isTraceEnabled()) {
            log.trace(format("selectWhereEmail args : email=%s", email));
        }

        Account account = this.accountRepository.findOneByEmail(email);

        if (log.isTraceEnabled()) {
            log.trace(format("selectWhereEmail return : %s", account));
        }
        return account;
    }
}