package hemoptysisheart.github.com.tutorial.spring.web.dao;

import hemoptysisheart.github.com.tutorial.spring.web.jpa.entity.AccountEntity;
import hemoptysisheart.github.com.tutorial.spring.web.jpa.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hemoptysisheart
 * @since 2018. 5. 25.
 */
@Service
class AccountDaoImpl implements AccountDao {
    @Autowired
    private AccountRepository accountRepository;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // hemoptysisheart.github.com.tutorial.spring.web.dao.AccountDao
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public AccountEntity insert(AccountEntity account) {
        return this.accountRepository.save(account);
    }
}