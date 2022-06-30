package com.yiyi_app.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * 配置解决跨域问题
 * 2022/6/30
 */
@Configuration
public class CORSConfiguration {

    @Bean
    public CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 配置跨域
        corsConfiguration.addAllowedHeader("*"); //允许携带任意请求头
        corsConfiguration.addAllowedMethod("*"); //允许所有方法
        corsConfiguration.addAllowedOrigin("*"); //允许所有源
        corsConfiguration.setAllowCredentials(true);  //允许携带cookie

        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsWebFilter(source);
    }
}
