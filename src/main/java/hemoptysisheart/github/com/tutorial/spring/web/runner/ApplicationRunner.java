/**
 * @author hemoptysisheart
 * @since 2018. 5. 22.
 */
package hemoptysisheart.github.com.tutorial.spring.web.runner; // 패키지를 지정해서 모듈 의존성 정보가 없는 클래스를 읽으려 하는 문제를 해결한다.

import hemoptysisheart.github.com.tutorial.spring.web.configuration.ApplicationConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 애플리케이션 실행 클래스.
 */
@SpringBootApplication(scanBasePackageClasses = {ApplicationConfiguration.class})
public class ApplicationRunner {
    /**
     * {@code java} 명령어로 애플리케이션을 실행하면 이 메서드를 먼저 실행한다.
     *
     * @param args 실행 명령의 인자.
     */
    public static void main(String[] args) {
        // Spring Boot 프레임워크에 이 프로그램을 Spring Boot Application으로 해석, 실행하도록 요청한다.
        SpringApplication.run(ApplicationRunner.class, args);
    }
}