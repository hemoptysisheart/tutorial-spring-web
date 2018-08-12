package hemoptysisheart.github.com.tutorial.spring.web.dao;

import hemoptysisheart.github.com.tutorial.spring.web.domain.Account;

/**
 * 비지니스 로직의 코드 스타일을 레포지토리 코드 스타일로 변환하거나, DB 최적화 로직을 담당하는 DAO 레이어의 컴포넌트.
 *
 * @author hemoptysisheart
 * @since 2018. 5. 31.
 */
public interface AccountDao {
    /**
     * 인자로 받은 계정을 등록(DB에 저장)하고 DB에서 생성된 정보를 포함한 계정 정보를 반환한다.
     *
     * @param account 신규 계정 정보.
     *
     * @return 등록된 계정 정보.
     */
    Account insert(Account account);

    /**
     * @param nickname 계정의 닉네임.
     *
     * @return {@code nickname}을 가진 계정 혹은 {@code null}.
     */
    Account select(String nickname);

    /**
     * alias {@link #select(String)}
     *
     * @param nickname 계정의 닉네임.
     *
     * @return {@code nickname}을 가진 계정 혹은 {@code null}.
     */
    default Account selectWhereNickname(String nickname) {
        return select(nickname);
    }

    /**
     * @param email 계정의 이메일 주소.
     *
     * @return {@code email}을 가진 계정 혹은 {@code null}.
     */
    Account selectWhereEmail(String email);
}