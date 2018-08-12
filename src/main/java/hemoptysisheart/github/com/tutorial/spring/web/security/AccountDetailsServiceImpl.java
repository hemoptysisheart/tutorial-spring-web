package hemoptysisheart.github.com.tutorial.spring.web.security;

import hemoptysisheart.github.com.tutorial.spring.web.dao.AccountDao;
import hemoptysisheart.github.com.tutorial.spring.web.domain.Account;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author hemoptysisheart
 * @since 2018. 6. 8.
 */
@Service
class AccountDetailsServiceImpl implements AccountDetailsService {
    private static final Logger log = getLogger(AccountDetailsServiceImpl.class);

    @Autowired
    private AccountDao accountDao;

    /**
     * 사용량 통계를 통해 `nickname`을 많이 쓰는지, `email`을 많이 쓰는지 확인해 많이 쓰는 속성을 먼저 확인하도록 하는 방식으로 DB 부하를 낮출 수 있다.
     * 만약, 최근 통계를 실시간으로 획득할 수 있다면 최근 통계를 바탕으로 자동 선택하도록 만들 수도 있다.
     *
     * @param username 닉네임 혹은 메일 주소.
     */
    @Override
    public AccountDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (log.isTraceEnabled()) {
            log.trace(format("loadUserByUsername args : username=%s", username));
        }

        // username이 등록된 계정의 nickname인지 확인한다.
        Account account = this.accountDao.select(username);
        if (null == account) {
            // username이 등록된 계정의 nickname이 아닌 경우, 등록된 계정의 email인지 확인한다.
            account = this.accountDao.selectWhereEmail(username);
        }

        if (null == account) {
            // username이 등록된 계정의 nickname 혹은 email이 아닌 경우.
            if (log.isInfoEnabled()) {
                log.info(format("log-in load fail : username=%s", username));
            }
            throw new UsernameNotFoundException(format("account does not exist : username=%s", username));
        }

        // username이 등록된 계정의 nickname 혹은 email인 경우.
        // 도메인 오브젝트(`hemoptysisheart.github.com.tutorial.spring.web.domain`의 구현체)는 기본적으로 JPA 엔티티이다.
        // JPA 구현체가 내부적으로 추가하는 기능에 영향을 받지 않으면서
        // 세션 관리(공유 세션을 포함해)에 사용할 수 있는 독립적인 오브젝트로 변환한다.
        if (log.isInfoEnabled()) {
            log.info(format("log-in load success : username=%s", username));
        }
        AccountDetails details = new BasicAccountDetails(
                account.getId(),
                account.getNickname(),
                account.getEmail(),
                account.getPassword()
        );

        if (log.isTraceEnabled()) {
            log.trace(format("loadUserByUsername return : %s", details));
        }
        return details;
    }
}