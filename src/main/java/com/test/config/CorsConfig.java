package com.test.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域配置
 * Created by 吴晓冬 on 2017/12/6.
 */
@Configuration
public class CorsConfig
{
    @Bean
    public CorsFilter corsFilter() {

        String allowedOrigin = "*";

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin(allowedOrigin); // 允许特定域名使用
        corsConfiguration.addAllowedHeader("*"); // 允许任何头
        corsConfiguration.addAllowedMethod("*"); // 允许任何方法（post、get等）
        corsConfiguration.setMaxAge(0l);
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addExposedHeader("x-auth-token");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(source);
    }
}
