package com.imes.opcda.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class GlobalCorsConfig {
    @Bean
    public CorsFilter corsFilter(){
        //1.添加CORS配置信息
        CorsConfiguration configuration = new CorsConfiguration();
        //1）允许的域名，不要写*，否则cookie无法使用
        configuration.addAllowedOrigin("http://localhost:8000");
        //2）是否发送Cookie信息
        configuration.setAllowCredentials(true);
        //3）允许请求方式
        configuration.addAllowedMethod(HttpMethod.GET);
        configuration.addAllowedMethod(HttpMethod.PUT);
        configuration.addAllowedMethod(HttpMethod.OPTIONS);
        configuration.addAllowedMethod(HttpMethod.DELETE);
        configuration.addAllowedMethod(HttpMethod.POST);
        configuration.addAllowedMethod(HttpMethod.PATCH);
        configuration.addAllowedMethod(HttpMethod.HEAD);
        configuration.addAllowedMethod(HttpMethod.TRACE);
        //4）允许的头信息
        configuration.addAllowedHeader("*");
        //2. 添加映射路径，拦截一切请求
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**", configuration);
        //3. 返回新的CorsFilter
        return new CorsFilter(configurationSource);
    }
}
