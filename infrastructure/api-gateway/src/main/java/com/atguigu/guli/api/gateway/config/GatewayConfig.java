package com.atguigu.guli.api.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class GatewayConfig {
    //跨域配置的filter
    @Bean
    public CorsWebFilter corsWebFilter(){
        UrlBasedCorsConfigurationSource corsConfigurationSource
                = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");//* 允许所有的客户端跨域访问
        config.addAllowedHeader("*");// 允许携带所有请求头跨域访问
        config.addAllowedMethod("*");//允许所有的请求方式跨域访问
        config.setAllowCredentials(true);//允许携带cookie跨域访问
        //指定一个路径  并为他配置跨域的参数     /**通配所有路径
        corsConfigurationSource.registerCorsConfiguration("/**",config);
        return new CorsWebFilter(corsConfigurationSource);
    }
}
