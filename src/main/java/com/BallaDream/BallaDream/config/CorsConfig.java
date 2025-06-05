package com.BallaDream.BallaDream.config;

import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.http.HttpMethod.*;

public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
//                .allowedMethods(GET.name(), POST.name(), DELETE.name(), PATCH.name(), PUT.name())
                .exposedHeaders("access", "refresh", "Authorization")
                .allowedOrigins("http://localhost:5173")
                .allowedOrigins("https://balladream.shop"); //프론트엔드 서버 기입

    }
}
