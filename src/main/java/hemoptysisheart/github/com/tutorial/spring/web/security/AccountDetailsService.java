package hemoptysisheart.github.com.tutorial.spring.web.security;

import hemoptysisheart.github.com.tutorial.spring.web.domain.Account;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

/**
 * 인증할 계정 정보를 가저올 때 사용할 컴포넌트를 정의한다.
 *
 * {@link UserDetailsService}를 구현하기에 앞서, 세션으로 관리하는 유저 정보를 {@link AccountDetails}로 제한할 필요가 있다.
 * 그래서 {@link UserDetails}가 아닌 {@link AccountDetails}를 사용하는 {@link AccountDetailsService} 인터페이스를 정의한다.
 *
 * @author hemoptysisheart
 * @since 2018. 6. 8.
 */
@Transactional
public interface AccountDetailsService extends UserDetailsService {
    /**
     * 인자로 받은 유저 식별자에 해당하는 계정 정보를 찾는다.
     *
     * @param username 닉네임 혹은 메일 주소.
     *
     * @return 계정 정보. NOT NULL.
     *
     * @throws UsernameNotFoundException {@code username}에 해당하는 계정이 없을 때.
     * @see Account#getNickname() {@code username}은 {@link Account#getNickname() 닉네임} 일 수 있습니다.
     * @see Account#getEmail() {@code username}은 {@link Account#getEmail()} () 이메일} 일 수 있습니다.
     */
    @Override
    AccountDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}