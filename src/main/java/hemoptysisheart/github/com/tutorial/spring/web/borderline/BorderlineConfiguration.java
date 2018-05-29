package hemoptysisheart.github.com.tutorial.spring.web.borderline;

import hemoptysisheart.github.com.tutorial.spring.web.service.ServiceConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author hemoptysisheart
 * @since 2018. 5. 29.
 */
@Configuration
@ComponentScan(basePackageClasses = {ServiceConfiguration.class})
public class BorderlineConfiguration {
    public static final Package PACKAGE = BorderlineConfiguration.class.getPackage();
    public static final String PACKAGE_NAME = PACKAGE.getName();
}