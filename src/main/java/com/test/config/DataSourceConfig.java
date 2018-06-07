//package com.test.config;
//
//import com.zaxxer.hikari.HikariDataSource;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
//import javax.sql.DataSource;
//
///**
// * Created by 吴晓冬 on 2018/6/6.
// */
//@Configuration
//public class DataSourceConfig
//{
//    @Value("${spring.datasource.driverClassName}")
//    private String driverClassName;
//
//    @Value("${spring.datasource.hikari.maximum-pool-size}")
//    private Integer maximumPoolSize;
//
//    @Value("${spring.datasource.rw.url}")
//    private String rwUrl;
//
//    @Value("${spring.datasource.rw.username}")
//    private String rwUsername;
//
//    @Value("${spring.datasource.rw.password}")
//    private String rwPassword;
//
//    @Value("${spring.datasource.ro.url}")
//    private String roUrl;
//
//    @Value("${spring.datasource.ro.username}")
//    private String roUsername;
//
//    @Value("${spring.datasource.ro.password}")
//    private String roPassword;
//
//    @Primary
//    @Bean(name = "dataSourceRW")
//    public DataSource dataSourceRW()
//    {
//        HikariDataSource dataSourceRW = new HikariDataSource();
//        dataSourceRW.setDriverClassName(driverClassName);
//        dataSourceRW.setJdbcUrl(rwUrl);
//        dataSourceRW.setUsername(rwUsername);
//        dataSourceRW.setPassword(rwPassword);
//        dataSourceRW.setMaximumPoolSize(maximumPoolSize);
//
//        return dataSourceRW;
//    }
//
//    @Bean(name = "dataSourceRO")
//    public DataSource dataSourceRO()
//    {
//        HikariDataSource dataSourceRO = new HikariDataSource();
//        dataSourceRO.setDriverClassName(driverClassName);
//        dataSourceRO.setJdbcUrl(roUrl);
//        dataSourceRO.setUsername(roUsername);
//        dataSourceRO.setPassword(roPassword);
//        dataSourceRO.setMaximumPoolSize(maximumPoolSize);
//
//        return dataSourceRO;
//    }
//
//
//}
