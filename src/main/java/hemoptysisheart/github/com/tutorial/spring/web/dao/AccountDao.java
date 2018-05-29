package hemoptysisheart.github.com.tutorial.spring.web.dao;

import hemoptysisheart.github.com.tutorial.spring.web.jpa.entity.AccountEntity;
import hemoptysisheart.github.com.tutorial.spring.web.jpa.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 비지니스 로직의 코드 스타일을 레포지토리 코드 스타일로 변환하거나, DB 최적화 로직을 담당하는 DAO 레이어의 컴포넌트.
 *
 * @author hemoptysisheart
 * @since 2018. 5. 25.
 */
@Service
public class AccountDao {
    @Autowired
    private AccountRepository accountRepository;

    public AccountEntity insert(AccountEntity account) {
        return this.accountRepository.save(account);
    }
}