package hemoptysisheart.github.com.tutorial.spring.web.configuration;

import hemoptysisheart.github.com.tutorial.spring.web.controller.ControllerConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author hemoptysisheart
 * @since 2018. 5. 29.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackageClasses = {ControllerConfiguration.class})
public class WebMvcConfiguration {
}