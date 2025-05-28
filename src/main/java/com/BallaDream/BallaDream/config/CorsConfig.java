package com.BallaDream.BallaDream.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .exposedHeaders("access", "refresh", "Authorization")
                .allowedOrigins("http://localhost:5173"); //Todo 프론트엔드 서버 기입
    }
}
