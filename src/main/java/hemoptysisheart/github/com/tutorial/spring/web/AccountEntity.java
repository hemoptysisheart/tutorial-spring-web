package hemoptysisheart.github.com.tutorial.spring.web;

import javax.persistence.*;

import static java.lang.String.format;

/**
 * 임시로 만든 테이블을 사용해 계정 엔티티(JPA Entity)를 만들었다.
 * <p>
 * {@link Entity} : AccountEntity 클래스의 인스턴스는 JPA 엔티티이며 Account라는 이름으로 관리한다.
 * {@link Table} : Account 엔티티의 데이터는 user_account 테이블과 동기화한다. 동기화할 테이블, 테이블에 있는 인덱스 등의 테이블 정보를 제공한다.
 *
 * @author hemoptysisheart
 * @since 2018. 5. 24.
 */
@Entity(name = "Account")
@Table(name = "user_account",
        uniqueConstraints = {@UniqueConstraint(name = "UQ_ACCOUNT_EMAIL", columnNames = {"email"}),
                @UniqueConstraint(name = "UQ_ACCOUNT_NICKNAME", columnNames = {"nickname"})})
public class AccountEntity {
    /**
     * 계정 ID.
     *
     * @Id : `Account` 엔티티의 ID가 되는 속성. primary key 컬럼.
     * @GeneratedValue : Java 로직이 아닌 MySQL의 AUTO_INCREMENT로 값이 정해지는 속성.
     * @Column : 없어도 되지만, 필드와 DB 컬럼값의 동기화 규치를 명시적으로 표시.
     * <p>
     * 필드 이름과 컬럼 이름은 다를 수 있다.
     * 필드 이름과 컬럼 이름이 같지만, 컬럼 이름을 명시적으로 지정한다.
     * 이름을 상수로 지정할 경우, 엔티티 코드의 변경 없이 컬럼 이름을 바꿀 수 있다.
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = false, updatable = false)
    private int id;
    /**
     * 계정의 이메일 주소.
     * 로그인에 사용할 수 있도록 {@code NOT NULL UNIQUE} 조건을 가진다.
     * <p>
     * 필드 이름과 컬럼 이름은 다를 수 있다.
     * 필드 이름과 컬럼 이름이 같지만, 컬럼 이름을 명시적으로 지정한다.
     * 이름을 상수로 지정할 경우, 엔티티 코드의 변경 없이 컬럼 이름을 바꿀 수 있다.
     * </p>
     */
    @Column(name = "email", unique = true, nullable = false, length = 255)
    private String email;
    /**
     * 계정 이름. 화면 표시용 등으로 사용한다.
     * 로그인에 사용할 수 있도록 {@code NOT NULL UNIQUE} 조건을 가진다.
     * <p>
     * 필드 이름과 컬럼 이름은 다를 수 있다.
     * 필드 이름과 컬럼 이름이 같지만, 컬럼 이름을 명시적으로 지정한다.
     * 이름을 상수로 지정할 경우, 엔티티 코드의 변경 없이 컬럼 이름을 바꿀 수 있다.
     * </p>
     */
    @Column(name = "nickname", unique = true, nullable = false, length = 128)
    private String nickname;
    /**
     * 로그인 비밀번호.
     * DB에 저장하기 전에라도, 이 필드에 값이 설정되는 시점에는 원본 비밀번호를 알 수 없도록 해싱(단방향 암호화)이 끝나야 한다.
     * Spring Security는 기본적으로 bcrypt 해시를 사용한다.
     * <p>
     * 필드 이름과 컬럼 이름은 다를 수 있다.
     * 다른 경우에는 반드시 {@link Column} 어노테이션에 {@code name} 속성으로 컬럼 이름을 지정해야 한다.
     * </p>
     */
    @Column(name = "passwd", nullable = false, length = 60)
    private String password;

    /**
     * JPA 엔티티는 인자 없는 생성자 메서드가 필수이다.
     * 보통은 사용하지 않고 JPA만 사용하기 때문에 {@code private} 접근 제한자를 사용.
     * <p>
     * 리플렉션으로 인자없는 생성자 메서드를 사용해 엔티티 인스턴스를 만들고,
     * 리플렉션으로 JPA 필드로 표시된({@link javax.persistence} 패키지의 어노테이션이 붙어 있는) 필드의 값을 지정한다.
     * </p>
     */
    private AccountEntity() {
    }

    public AccountEntity(String email, String nickname, String password) {
        setEmail(email);
        setNickname(nickname);
        setPassword(password);
    }

    public int getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        if (null == email || email.isEmpty()) {
            throw new IllegalArgumentException(format("email is null or empty : %s", email));
        }
        this.email = email;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        if (null == nickname || nickname.isEmpty()) {
            throw new IllegalArgumentException(format("nickname is null or empty : %s", nickname));
        }
        this.nickname = nickname;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        if (null == password || password.isEmpty()) {
            throw new IllegalArgumentException("password is null or empty.");
        }
        this.password = password;
    }

    /**
     * JPA 구현체의 엔티티 매니저가 엔티티 인스턴스를 관리하기 위해선 오버라이드가 필요하다.
     * 자바 컬렉션 프레임워크에서 엘리먼트를 찾기 위해 오버라이드 해야 하는 것과 같은 이유.
     *
     * @return
     */
    @Override
    public int hashCode() {
        return this.id;
    }

    /**
     * JPA 구현체의 엔티티 매니저가 엔티티 인스턴스를 관리하기 위해선 오버라이드가 필요하다.
     * 자바 컬렉션 프레임워크에서 엘리먼트를 찾기 위해 오버라이드 해야 하는 것과 같은 이유.
     * <p>
     * {@code AUTO_INCREMENT}는 {@code 1}부터 할당되기 때문에,
     * {@link #id}가 1 미만인 인스턴스는 JPA 엔티티로 등록되지 않은 것으로 간주해 {@code false}를 반환한다.
     *
     * @param that 다른 엔티티
     * @return
     */
    @Override
    public boolean equals(Object that) {
        if (0 < this.id && this == that) {
            return true;
        } else if (that instanceof AccountEntity && 0 < this.id) {
            return this.id == ((AccountEntity) that).id;
        } else {
            return false;
        }
    }

    /**
     * 디버깅 참조용.
     *
     * @return
     */
    @Override
    public String toString() {
        return format("%s{id=%d, email='%s', nickname='%s', password=[ PROTECTED ]}",
                getClass().getSimpleName(), this.id, this.email, this.nickname);
    }
}