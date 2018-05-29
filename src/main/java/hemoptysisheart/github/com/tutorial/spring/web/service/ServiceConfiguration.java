package hemoptysisheart.github.com.tutorial.spring.web.service;

import hemoptysisheart.github.com.tutorial.spring.web.dao.DaoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author hemoptysisheart
 * @since 2018. 5. 29.
 */
@Configuration
@ComponentScan(basePackageClasses = {DaoConfiguration.class})
public class ServiceConfiguration {
    public static final Package PACKAGE = ServiceConfiguration.class.getPackage();
    public static final String PACKAGE_NAME = PACKAGE.getName();
}