package hemoptysisheart.github.com.tutorial.spring.web;

import static java.lang.String.format;

/**
 * 새 계정 등록 로직의 인자.
 *
 * @author hemoptysisheart
 * @see AccountService#create(CreateAccountParams)
 * @since 2018. 5. 24.
 */
public class CreateAccountParams {
    private String email;
    private String nickname;
    private String password;

    public CreateAccountParams() {
    }

    public CreateAccountParams(String email) {
        setEmail(email);
    }

    public CreateAccountParams(String email, String nickname) {
        this(email);
        setNickname(nickname);
    }

    public CreateAccountParams(String email, String nickname, String password) {
        this(email, nickname);
        setPassword(password);
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

    @Override
    public String toString() {
        return format("%s{email=%s, nickname=%s, password=%s}",
                getClass().getSimpleName(), this.email, this.nickname, this.password);
    }
}