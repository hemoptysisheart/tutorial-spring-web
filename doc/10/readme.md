# CH.10 - 인증

회원가입(계정 등록)은 만들었다.
인증을 추가해 로그인 기반의 웹 애플리케이션으로 전환한다.

> * 인증(Authentication) : 로그인.
> * 인가(Authorize) : 현재 유저가 어떤 유저인가. 게스트/일반/관리자 등.

## STEP 1 - 시큐리티 모듈

### 라이브러리 추가

먼저 Spring Security 모듈을 추가한다.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```
[pom.xml](../../pom.xml)


### `AccountDetails` - 웹 세션용 타입 정의

서블릿 컨테이너의 웹 세션에서 `Account` 정보를 저장, 공유할 때 사용할 데이터 타입을 정의.

웹 세션 정보는 애플리케이션의 메모리에 저장하거나 별도의 세션 서버에 저장해서 다수의 애플리케이션 서버가 공유한다.
즉, JVM 메모리에서 트랜잭션 관리 등에 사용하는 JPA 엔티티(`AccountEntity`)는 사용할 수 없다.
따라서 JPA에서 자유로운 별도의 타입을 정의해 그 인스턴스를 사용해야 한다.

```java
package hemoptysisheart.github.com.tutorial.spring.web.security;

// ... 생략 ...

public interface AccountDetails extends UserDetails {
    int getId();

    String getEmail();

    String getNickname();

    // ... 생략 ...
}
```
[AccountDetails.java](../../src/main/java/hemoptysisheart/github/com/tutorial/spring/web/security/AccountDetails.java)

세션이 생성되고 인증로직이 실행되면 세션에 계정 정보가 저장된다.
데이터가 이상 없이 설정되도록 검증 로직을 넣어준다.

```java
package hemoptysisheart.github.com.tutorial.spring.web.security;

// ... 생략 ...

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

    // ... 생략 ...
}
```
[BasicAccountDetails.java](../../src/main/java/hemoptysisheart/github/com/tutorial/spring/web/security/BasicAccountDetails.java)

### `AccountDetailsService` - 인증용 계정 정보 로더

Spring Security는 로그인 할 때 유저 정보(`UserDetails`)를 읽기 위해 `UserDetialsService`를 사용한다.
애플리케이션에 특화된 유저 정보를 읽기 위해 `AccountDetailsService`를 정의하고 구현한다.

```java
package hemoptysisheart.github.com.tutorial.spring.web.security;

// ... 생략 ...

@Transactional
public interface AccountDetailsService extends UserDetailsService {
    @Override
    AccountDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
```
[AccountDetailsService.java](../../src/main/java/hemoptysisheart/github/com/tutorial/spring/web/security/AccountDetailsService.java)

계정마다 유일한 속성이 `nickname`과 `email`의 2가지가 있으므로 이 두가지 값을 로그인 키로 사용할 수 있다.
사용량 통계를 통해 `nickname`을 많이 쓰는지, `email`을 많이 쓰는지 확인해 많이 쓰는 속성을 먼저 확인하도록 하는 방식으로 DB 부하를 낮출 수 있다.
만약, 최근 통계를 실시간으로 획득할 수 있다면 최근 통계를 바탕으로 자동 선택하도록 만들 수도 있다.

```java
package hemoptysisheart.github.com.tutorial.spring.web.security;

// ... 생략 ...

@Service
class AccountDetailsServiceImpl implements AccountDetailsService {
    @Autowired
    private AccountDao accountDao;

    @Override
    public AccountDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = this.accountDao.select(username);
        if (null == account) {
            account = this.accountDao.selectWhereEmail(username);
        }

        if (null == account) {
            throw new UsernameNotFoundException(format("account does not exist : username=%s", username));
        }

        AccountDetails details = new BasicAccountDetails(
                account.getId(),
                account.getNickname(),
                account.getEmail(),
                account.getPassword()
        );

        return details;
    }
}
```
[AccountDetailsServiceImpl.java](../../src/main/java/hemoptysisheart/github/com/tutorial/spring/web/security/AccountDetailsServiceImpl.java)