package hemoptysisheart.github.com.tutorial.spring.web.dao;

import hemoptysisheart.github.com.tutorial.spring.web.jpa.repository.RepositoryConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author hemoptysisheart
 * @since 2018. 5. 29.
 */
@Configuration
@ComponentScan(basePackageClasses = {RepositoryConfiguration.class})
public class DaoConfiguration {
    public static final Package PACKAGE = DaoConfiguration.class.getPackage();
    public static final String PACKAGE_NAME = PACKAGE.getName();
}