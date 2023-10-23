package hu.bearmaster.springtutorial.boot;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Map;

@TestConfiguration
@EnableJpaRepositories(basePackages = {
        "hu.bearmaster.springtutorial.boot.repository",
        "hu.bearmaster.springtutorial.boot.model"
})
public class TestConfig {

    @Bean
    public DataSource hikariDataSource(Environment environment) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(environment.getRequiredProperty("database.url"));
        config.setUsername(environment.getRequiredProperty("database.username"));
        config.setPassword(environment.getRequiredProperty("database.password"));
        //config.setSchema(environment.getRequiredProperty("database.schema"));
        config.setMaximumPoolSize(environment.getProperty("database.maxpoolsize", Integer.class, 5));
        return new HikariDataSource(config);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.POSTGRESQL);
        vendorAdapter.setShowSql(true);
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setDataSource(dataSource);
        factory.setPackagesToScan("hu.bearmaster.springtutorial.boot.model");
        factory.setJpaPropertyMap(Map.of("hibernate.format_sql", true,
                "hibernate.hbm2ddl.auto", "create"));
        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory);
        return txManager;
    }

}
