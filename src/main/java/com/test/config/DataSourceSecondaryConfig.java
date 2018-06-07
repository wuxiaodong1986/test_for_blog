package com.test.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by 吴晓冬 on 2018/6/6.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef="entityManagerFactorySecondary", transactionManagerRef="transactionManagerSecondary", basePackages= {"com.test.dao.secondary"})
public class DataSourceSecondaryConfig
{
    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Value("${spring.datasource.hikari.maximum-pool-size}")
    private Integer maximumPoolSize;

    @Value("${spring.datasource.secondary.url}")
    private String secondaryUrl;

    @Value("${spring.datasource.secondary.username}")
    private String secondaryUsername;

    @Value("${spring.datasource.secondary.password}")
    private String secondaryPassword;

    /**
     * 二库数据源配置
     * @return
     */
    @Bean(name = "dataSourceSecondary")
    public DataSource dataSourceSecondary()
    {
        HikariDataSource dataSourceSecondary = new HikariDataSource();
        dataSourceSecondary.setDriverClassName(driverClassName);
        dataSourceSecondary.setJdbcUrl(secondaryUrl);
        dataSourceSecondary.setUsername(secondaryUsername);
        dataSourceSecondary.setPassword(secondaryPassword);
        dataSourceSecondary.setMaximumPoolSize(maximumPoolSize);

        return dataSourceSecondary;
    }

    /**
     * 二库jpa 实例管理器工厂配置
     */
    @Bean(name = "entityManagerFactorySecondary")
    public LocalContainerEntityManagerFactoryBean entityManagerFactorySecondary(EntityManagerFactoryBuilder builder)
    {
        LocalContainerEntityManagerFactoryBean em = builder
                .dataSource(dataSourceSecondary())
                .packages("com.test.model")
                .build();
        Properties properties = new Properties();
        properties.setProperty("hibernate.physical_naming_strategy", "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy");
        em.setJpaProperties(properties);
        return em;
    }

    /**
     * 二库事务管理器配置
     */
    @Bean(name = "transactionManagerSecondary")
    public PlatformTransactionManager transactionManagerSecondary(EntityManagerFactoryBuilder builder)
    {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactorySecondary(builder).getObject());
        return txManager;
    }
}
