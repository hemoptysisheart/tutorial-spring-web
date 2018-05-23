package hemoptysisheart.github.com.tutorial.spring.web;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 새로운 계정 등록을 요청할 때 필요한 정보.
 *
 * @author hemoptysisheart
 * @since 2018. 5. 23.
 */
public class SignUpReq {
    @NotNull
    @Email
    private String email;
    @NotNull
    @Size(min = 1)
    private String nickname;
    @NotNull
    @Size(min = 4)
    private String password;
    @NotNull
    @Size(min = 4)
    private String confirm;

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

    public String getConfirm() {
        return this.confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    @Override
    public String toString() {
        return new StringBuilder(SignUpReq.class.getSimpleName())
                .append("{email='").append(this.email).append('\'')
                .append(", nickname='").append(this.nickname).append('\'')
                .append(", password='").append(this.password).append('\'')
                .append(", confirm='").append(this.confirm).append('\'')
                .append('}').toString();
    }
}