/**
 * @author hemoptysisheart
 * @since 2018. 5. 22.
 */
package hemoptysisheart.github.com.tutorial.spring.web; // 패키지를 지정해서 모듈 의존성 정보가 없는 클래스를 읽으려 하는 문제를 해결한다.

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * 애플리케이션 실행 클래스.
 */
@SpringBootApplication  // 애플리케이션을 Spring Boot 애플리케이션으로 만들기 위한 설정.
@EnableWebMvc   // Spring Boot 애플리케이션을 웹 애플리케이션으로 만들기 위한 설정.
public class ApplicationRunner {
    /**
     * {@code java} 명령어로 애플리케이션을 실행하면 이 메서드를 먼저 실행한다.
     *
     * @param args 실행 명령의 인자.
     * @throws Exception 애플리케이션이 처리하지 못한 예외.
     */
    public static void main(String[] args) throws Exception {
        // Spring Boot 프레임워크에 이 프로그램을 Spring Boot Application으로 해석, 실행하도록 요청한다.
        SpringApplication.run(ApplicationRunner.class, args);
    }
}