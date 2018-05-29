package hemoptysisheart.github.com.tutorial.spring.web.controller;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 새로운 계정 등록을 요청할 때 필요한 정보.
 *
 * @author hemoptysisheart
 * @since 2018. 5. 23.
 */
public class SignUpReq {
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String nickname;
    @NotNull
    @Size(min = 4)  // TODO 최소 길이를 상수로 변경.
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