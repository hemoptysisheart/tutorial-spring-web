package hemoptysisheart.github.com.tutorial.spring.web.security;

import static java.lang.String.format;

/**
 * 기본 구현.
 *
 * @author hemoptysisheart
 * @since 2018. 6. 8.
 */
public class BasicAccountDetails implements AccountDetails {
    private int id;
    private String nickname;
    private String email;
    private String password;

    public BasicAccountDetails(int id, String nickname, String email, String password) {
        if (0 >= id) {
            throw new IllegalArgumentException(format("illegal account id : %d", id));
        }
        if (null == nickname || nickname.isEmpty()) {
            throw new IllegalArgumentException(format("illegal account nickname : %s", nickname));
        }
        if (null == email || email.isEmpty()) {
            throw new IllegalArgumentException(format("illegal account email : %s", email));
        }
        if (null == password || password.isEmpty()) {
            throw new IllegalArgumentException("illegal account password hash : [ PROTECTED ]");
        }

        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // hemoptysisheart.github.com.tutorial.spring.web.security.AccountDetails
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public String getNickname() {
        return this.nickname;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String toString() {
        return format("{id=%d, nickname=%s, email=%s}", this.id, this.nickname, this.email);
    }
}