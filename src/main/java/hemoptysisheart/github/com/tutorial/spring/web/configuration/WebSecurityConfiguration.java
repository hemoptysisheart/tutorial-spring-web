package hemoptysisheart.github.com.tutorial.spring.web.configuration;

import hemoptysisheart.github.com.tutorial.spring.web.security.AccountDetailsService;
import hemoptysisheart.github.com.tutorial.spring.web.security.SecurityConfiguration;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author hemoptysisheart
 * @since 2018. 6. 22.
 */
@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = {SecurityConfiguration.class})
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private static final Logger log = getLogger(WebSecurityConfiguration.class);

    @Autowired
    private AccountDetailsService accountDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.accountDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin();
        http.logout();
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/signup", "/login", "/login/**").anonymous();
    }
}