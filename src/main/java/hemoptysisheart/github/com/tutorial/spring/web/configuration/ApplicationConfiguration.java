package hemoptysisheart.github.com.tutorial.spring.web.configuration;

import org.springframework.context.annotation.Configuration;

/**
 * @author hemoptysisheart
 * @since 2018. 5. 29.
 */
@Configuration
public class ApplicationConfiguration {
    public static final Package PACKAGE = ApplicationConfiguration.class.getPackage();
    public static final String PACKAGE_NAME = PACKAGE.getName();
}