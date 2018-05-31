# CH.02 - 일단 Spring Boot Web Application으로 태세 변환

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

## STEP 2 - 패키지를 지정

`@SpringBootApplication`으로 존재하지 않는(메이븐 빌드 스크립트에 의존성이 없는) 패키지, 클래스를 읽지 않도록 패키지를 지정한다.

```java
package hemoptysisheart.github.com.tutorial.spring.web;

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

[깔끔하게 실행](step_2_ok.log)되는 것을 확인할 수 있다.

```
➜  ~ lsof -nP -i4TCP | grep 8080
java      1675 hemoptysisheart   52u  IPv6 0x361e34fce6328eb3      0t0  TCP *:8080 (LISTEN)
```

하지만 여전히 별다른 로직은 없으며, HTTP 리퀘스트를 매핑할 컨트롤러도, 뷰도 없기 때문에 브라우저로 볼 수 있는 페이지 또한 없다.

### 프로젝트 구조

`STEP 2`가 끝난 후의 프로젝트 디렉토리 구조.
`mvn clean`으로 소스코드가 아닌 파일을 삭제했다.

```
.
├── LICENSE
├── README.md
├── doc
│   ├── 01
│   │   └── readme.md
│   ├── 02
│   │   ├── readme.md
│   │   ├── step_1_error.log
│   │   ├── step_2_ok.log
│   │   └── step_2_tree.txt
│   └── readme.md
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── hemoptysisheart
│   │   │       └── github
│   │   │           └── com
│   │   │               └── tutorial
│   │   │                   └── spring
│   │   │                       └── web
│   │   │                           └── ApplicationRunner.java
│   │   └── resources
│   └── test
│       └── java
└── tutorial-spring-web.iml

15 directories, 11 files
```
[전체 구조](step_2_tree.txt)