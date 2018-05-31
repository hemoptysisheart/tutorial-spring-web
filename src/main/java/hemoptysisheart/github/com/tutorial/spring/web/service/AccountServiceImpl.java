package hemoptysisheart.github.com.tutorial.spring.web.service;

import hemoptysisheart.github.com.tutorial.spring.web.dao.AccountDao;
import hemoptysisheart.github.com.tutorial.spring.web.domain.Account;
import hemoptysisheart.github.com.tutorial.spring.web.jpa.entity.AccountEntity;
import hemoptysisheart.github.com.tutorial.spring.web.service.params.CreateAccountParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 계정 관리에 관련된 로직.
 *
 * @author hemoptysisheart
 * @since 2018. 5. 24.
 */
@Service
class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // hemoptysisheart.github.com.tutorial.spring.web.service.AccountService
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public Account create(CreateAccountParams params) {
        Account account = new AccountEntity(params.getEmail(), params.getNickname(), params.getPassword());
        account = this.accountDao.insert(account);
        return account;
    }
}