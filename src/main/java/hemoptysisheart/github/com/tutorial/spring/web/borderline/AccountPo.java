package hemoptysisheart.github.com.tutorial.spring.web.borderline;

import static java.lang.String.format;

/**
 * 도메인 오브젝트에서 JPA 등의 영향을 제거한 변경 불가능한 스냅샷 인스턴스로 변환한다.
 * <p>
 * * {@code PO(Plain Object)}
 * </p>
 *
 * @author hemoptysisheart
 * @since 2018. 5. 25.
 */
public class AccountPo {
    private int id;
    private String email;
    private String nickname;

    public AccountPo(int id) {
        setId(id);
    }

    public AccountPo(int id, String email) {
        this(id);
        setEmail(email);
    }

    public AccountPo(int id, String email, String nickname) {
        this(id, email);
        setNickname(nickname);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return format("%s{id=%d, email='%s', nickname='%s'}",
                getClass().getSimpleName(), this.id, this.email, this.nickname);
    }
}