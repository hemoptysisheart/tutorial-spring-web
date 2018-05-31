package hemoptysisheart.github.com.tutorial.spring.web.dao;

import hemoptysisheart.github.com.tutorial.spring.web.domain.Account;

/**
 * 비지니스 로직의 코드 스타일을 레포지토리 코드 스타일로 변환하거나, DB 최적화 로직을 담당하는 DAO 레이어의 컴포넌트.
 *
 * @author hemoptysisheart
 * @since 2018. 5. 31.
 */
public interface AccountDao {
    Account insert(Account account);
}