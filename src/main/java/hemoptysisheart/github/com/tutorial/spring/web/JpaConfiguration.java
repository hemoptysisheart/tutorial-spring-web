package hemoptysisheart.github.com.tutorial.spring.web;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * JPA 기반으로 DB 연동 설정.
 *
 * @author hemoptysisheart
 * @since 2018. 5. 24.
 */
@Configuration  // 스프링 JavaConfig 클래스임을 표시해준다. 이게 있어야 스프링 프레임워크가 설정으로 인식한다.
@EnableJpaRepositories  // SQL 쿼리를 쉽게 생성할 수 있는 JPA 레포지토리를 사용하도록 설정한다.
@EnableTransactionManagement    // 트랜잭션 관리자를 설정하도록 설정한다.
public class JpaConfiguration {
    /**
     * JDBC 설정({@code spring.datasource})이 적용된 {@link DataSource} 컴포넌트를 설정한다.
     * 의존성 설정 때문에 기본 커넥션 풀 관리자인 HikariCP를 반환한다.
     *
     * @return JDBC 커넥션 컴포넌트.
     */
    @Bean
    @ConfigurationProperties("spring.datasource.hikari")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * JPA 엔티티 관리자 팩토리.
     * DB의 데이터와 동기화되는 단위인 JPA 엔티티를 관리하는 엔티티 관리자를 만든다.
     * 엔티티 관리자는 데이터 동기화 시점 등을 결정한다.
     *
     * @return
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.MYSQL);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource());
        factory.setPackagesToScan(JpaConfiguration.class.getPackageName());
        factory.setJpaVendorAdapter(adapter);

        return factory;
    }

    /**
     * JPA 이벤트를 기반으로 트랜잭션을 관리하는 트랜잭션 관리자 컴포넌트를 만든다.
     *
     * @return
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager(entityManagerFactory().getObject());
    }

    /**
     * JPA 구현체로 사용하는 Hibernate에서 발생한 예외를 스프링의 예외로 변환한다.
     *
     * @return
     */
    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }
}