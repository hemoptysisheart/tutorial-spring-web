package hemoptysisheart.github.com.tutorial.spring.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 비지니스 로직의 코드 스타일을 레포지토리 코드 스타일로 변환하거나, DB 최적화 로직을 담당하는 DAO 레이어의 컴포넌트.
 *
 * @author hemoptysisheart
 * @since 2018. 5. 25.
 */
@Transactional
@Service
public class AccountDao {
    @Autowired
    private AccountRepository accountRepository;

    public AccountEntity insert(AccountEntity account) {
        return this.accountRepository.save(account);
    }
}