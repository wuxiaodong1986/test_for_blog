package com.test.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 吴晓冬 on 2018/6/7.
 */
@Configuration
public class DataSourceConfig
{
    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Value("${spring.datasource.hikari.maximum-pool-size}")
    private Integer maximumPoolSize;

    @Value("${spring.datasource.primary.url}")
    private String primaryUrl;

    @Value("${spring.datasource.primary.username}")
    private String primaryUsername;

    @Value("${spring.datasource.primary.password}")
    private String primaryPassword;

    @Value("${spring.datasource.secondary.url}")
    private String secondaryUrl;

    @Value("${spring.datasource.secondary.username}")
    private String secondaryUsername;

    @Value("${spring.datasource.secondary.password}")
    private String secondaryPassword;

    @Bean(name = "dataSource")
    public DataSource dynamicDataSource()
    {
        //配置主库数据源
        HikariDataSource dataSourcePrimary = new HikariDataSource();
        dataSourcePrimary.setDriverClassName(driverClassName);
        dataSourcePrimary.setJdbcUrl(primaryUrl);
        dataSourcePrimary.setUsername(primaryUsername);
        dataSourcePrimary.setPassword(primaryPassword);
        dataSourcePrimary.setMaximumPoolSize(maximumPoolSize);

        //配置二库数据源
        HikariDataSource dataSourceSecondary = new HikariDataSource();
        dataSourceSecondary.setDriverClassName(driverClassName);
        dataSourceSecondary.setJdbcUrl(secondaryUrl);
        dataSourceSecondary.setUsername(secondaryUsername);
        dataSourceSecondary.setPassword(secondaryPassword);
        dataSourceSecondary.setMaximumPoolSize(maximumPoolSize);

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("dataSourceKeyPrimary", dataSourcePrimary);
        targetDataSources.put("dataSourceKeySecondary", dataSourceSecondary);

        //配置动态数据源
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(targetDataSources);
        return dynamicDataSource;
    }
}
