package hemoptysisheart.github.com.tutorial.spring.web.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

/**
 * Spring Security가 인증 정보를 다루는 {@link UserDetails}에 정보를 추가하기 위해 인터페이스를 상속한다.
 *
 * @author hemoptysisheart
 * @since 2018. 6. 8.
 */
public interface AccountDetails extends UserDetails {
    int getId();

    String getEmail();

    String getNickname();

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // org.springframework.security.core.userdetails.UserDetails
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 유저의 개인정보가 아니면서 유일한 값인 {@link #getNickname() 닉네임}을 기본 식별자로 사용한다.
     *
     * @return 계정 이름.
     */
    @Override
    default String getUsername() {
        return getNickname();
    }

    /**
     * 이 계정의 권한 목록. 별도로 계정의 종류를 나누지 않으므로 {@code ROLE_USER}로 통일한다.
     * 유저의 종류가 나뉘고 권한이 달라질 때 구현 클래스에서 처리하도록 변경한다.
     *
     * @return 인가된 권한 목록.
     */
    @Override
    default Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    /**
     * 별도의 유효기간이 없으므로 {@code true}.
     *
     * @return 계정의 유효기간 종료 여부. {@code true}면 유효.
     */
    @Override
    default boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 규정 위반 등으로 사용 불가능 처분을 받았는지 표시.
     *
     * @return 사용 가능한 계정인지 여부. {@code true}면 유효.
     */
    @Override
    default boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 비밀번호가 유효한지 여부.
     * 보안 강화를 위해 비밀번호를 일정 기간마다 바꾸도록 강제할 때 사용.
     *
     * @return 비밀번호가 유효한지 여부. {@code true}면 유효.
     */
    @Override
    default boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정이 사용가능하게 됐는지 여부.
     * 이메일을 사용한 계정 활성화 등을 지원할 때 사용.
     *
     * @return {@code true}면 유효.
     */
    @Override
    default boolean isEnabled() {
        return true;
    }
}