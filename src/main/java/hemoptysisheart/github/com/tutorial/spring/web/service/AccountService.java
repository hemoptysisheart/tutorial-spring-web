package hemoptysisheart.github.com.tutorial.spring.web.service;

import hemoptysisheart.github.com.tutorial.spring.web.dao.AccountDao;
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
public class AccountService {
    @Autowired
    private AccountDao accountDao;

    /**
     * 유저가 입력한 값을 사용해 계정 엔티티 인스턴스를 만들어 저장한다.
     *
     * @param params 새 계정 등록에 필요한 정보.
     * @return 새로 등록된 계정 엔티티.
     */
    public AccountEntity create(CreateAccountParams params) {
        AccountEntity account = new AccountEntity(params.getEmail(), params.getNickname(), params.getPassword());
        account = this.accountDao.insert(account);
        return account;
    }
}