package hemoptysisheart.github.com.tutorial.spring.web.jpa.repository;

import hemoptysisheart.github.com.tutorial.spring.web.configuration.JpaConfiguration;
import hemoptysisheart.github.com.tutorial.spring.web.jpa.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA가 메서드 이름을 기반으로 SQL로 변환해주는 레포지토리 인터페이스.
 * 구현하지 않더라도 프록시를 사용해 인스턴스를 생성한다.
 * 메서드 이름으로 SQL 쿼리를 생성하는 것이 기본이지만, 다른 방식도 많다.
 * {@link Repository}는 {@link javax.persistence.Entity} 어노테이션이 있는 JPA 엔티티가 필요하다.
 *
 * @author hemoptysisheart
 * @see JpaConfiguration {@link org.springframework.data.jpa.repository.config.EnableJpaRepositories} 설정으로 사용하도록 설정한다.
 * @since 2018. 5. 24.
 */
@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
    /**
     * {@link AccountEntity#email}과 인자의 {@code email}이 일치하는 계정 엔티티를 반환한다.
     *
     * @param email 이메일 주소.
     * @return 계정 인스턴스 혹은 {@code null}.
     */
    AccountEntity findOneByEmail(String email);

    /**
     * {@link AccountEntity#nickname}과 인자의 {@code nickname}이 일치하는 계정 엔티티를 반환한다.
     *
     * @param nickname 닉네임.
     * @return 계정 인스턴스 혹은 {@code null}.
     */
    AccountEntity findOneByNickname(String nickname);
}