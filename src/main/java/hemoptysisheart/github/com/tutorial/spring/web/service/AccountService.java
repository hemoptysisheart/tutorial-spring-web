package hemoptysisheart.github.com.tutorial.spring.web.service;

import hemoptysisheart.github.com.tutorial.spring.web.domain.Account;
import hemoptysisheart.github.com.tutorial.spring.web.service.params.CreateAccountParams;

/**
 * @author hemoptysisheart
 * @since 2018. 5. 31.
 */
public interface AccountService {
    /**
     * 유저가 입력한 값을 사용해 계정 엔티티 인스턴스를 만들어 저장한다.
     *
     * @param params 새 계정 등록에 필요한 정보.
     * @return 새로 등록된 계정 엔티티.
     */
    Account create(CreateAccountParams params);
}