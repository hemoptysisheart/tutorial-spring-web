package hemoptysisheart.github.com.tutorial.spring.web.domain;

/**
 * @author hemoptysisheart
 * @since 2018. 5. 31.
 */
public interface Account {
    int getId();

    String getEmail();

    void setEmail(String email);

    String getNickname();

    void setNickname(String nickname);

    String getPassword();

    void setPassword(String password);
}