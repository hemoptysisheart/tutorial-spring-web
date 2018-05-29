package hemoptysisheart.github.com.tutorial.spring.web.controller;

import hemoptysisheart.github.com.tutorial.spring.web.borderline.BorderlineConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author hemoptysisheart
 * @since 2018. 5. 29.
 */
@Configuration
@ComponentScan(basePackageClasses = {BorderlineConfiguration.class})
public class ControllerConfiguration {
    public static final Package PACKAGE = ControllerConfiguration.class.getPackage();
    public static final String PACKAGE_NAME = PACKAGE.getName();
}