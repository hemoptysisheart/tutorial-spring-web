# Ch.02 - 일단 Spring Boot Web Application으로 태세 변환

애플리케이션을 임베디드 서블릿 컨테이너를 사용하는 웹 애플리케이션으로 바꾼다.

## STEP 1 - 일단 변환

여전히 로직이 없기 때문에 하는 일은 없고,
HTTP 리퀘스트를 받아 처리할 컨트롤러도 없기 때문에 열어볼 수 있는 페이지도 없다.
다만 실행 후 바로 종료되지 않고 HTTP 리퀘스트를 기다린다.

그래야 한다.

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class ApplicationRunner {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ApplicationRunner.class, args);
    }
}
```

* `@SpringBootApplication` : Spring Boot 기반의 애플리케이션임을 표시.
* `@EnableWebMvc` : 그 Spring Boot 기반의 애플리케이션이 웹 애플리케이션임을 표시.

하지만 이렇게 하면 수 많은 [에러](step_1_error.log) 때문에 애플리케이션을 실행하지 못한다.
원인은 다음 에러 메시지에서 확인할 수 있다.

```
Caused by: java.lang.IllegalStateException: Could not evaluate condition on org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration$EmbeddedDatabaseConfiguration due to org/springframework/jdbc/datasource/embedded/EmbeddedDatabaseType not found. Make sure your own configuration does not rely on that class. This can also happen if you are @ComponentScanning a springframework package (e.g. if you put a @ComponentScan in the default package by mistake)
```

에러메시지의 끝부분을 보면 `(e.g. if you put a @ComponentScan in the default package by mistake)`라고 나온다.
즉, 디폴트 패키지를 대상으로 스프링 컨텍스트를 생성하려고 했기 때문인데,
이는 `@SpringBootApplication` 어노테이션이 디폴트 패키지에 있는 `ApplicationRunner` 클래스에 걸려있기 때문이다.
그 결과 디폴트 패키지의 하위 패키지에 있는 `org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType` 클래스를 읽으려 시도하고, 실패한다.
`org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType` 클래스 정보는 **아마도** 스프링 부트 내부에 문자열로 하드코딩 되어있을 것이다.