/**
 * @author hemoptysisheart
 * @since 2018. 5. 22.
 */

/**
 * 애플리케이션 실행 클래스.
 */
// 현재 이 애플리케이션은 웹 애플리케이션이 아니다.
// 웹 애플리케이션으로 만들기 위해서는 Spring Boot의 Web MVC 모듈의 의존성을 추가하고,
// 이 애플리케이션이 Spring Boot 애플리케이션임을 Springframework에 알려줘야 한다.
public class ApplicationRunner {
    /**
     * {@code java} 명령어로 애플리케이션을 실행하면 이 메서드를 먼저 실행한다.
     *
     * @param args 실행 명령의 인자.
     * @throws Exception 애플리케이션이 처리하지 못한 예외.
     */
    public static void main(String[] args) throws Exception {
        // 애플리케이션을 실행하기는 하지만, 애플리케이션이 처리할 일을 하나도 지정하지 않았다.
        // 애플리케이션을 실행하면, 바로 종료된다.
    }
}