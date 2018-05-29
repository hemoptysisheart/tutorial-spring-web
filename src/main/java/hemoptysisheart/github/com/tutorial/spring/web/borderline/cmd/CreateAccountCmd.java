package hemoptysisheart.github.com.tutorial.spring.web.borderline.cmd;

import static java.lang.String.format;

/**
 * @author hemoptysisheart
 * @since 2018. 5. 25.
 */
public class CreateAccountCmd {
    private String email;
    private String nickname;
    private String password;

    public CreateAccountCmd() {
    }

    public CreateAccountCmd(String email) {
        setEmail(email);
    }

    public CreateAccountCmd(String email, String nickname) {
        this(email);
        setNickname(nickname);
    }

    public CreateAccountCmd(String email, String nickname, String password) {
        this(email, nickname);
        setPassword(password);
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

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return format("%s{email=%s, nickname=%s, password=[ PROTECTED ]}",
                getClass().getSimpleName(), this.email, this.nickname);
    }
}