package com.example.xiergc.config;

import com.example.xiergc.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).excludePathPatterns("/user/login", "/user/register");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8081")  // 允许前端发起的请求的源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 确保包括 OPTIONS 请求方法
                .allowedHeaders("Authorization", "Content-Type", "*") // 允许所有头部或者特定头部
                .allowCredentials(true) // 允许携带凭证（如 Cookies 或 Authorization Token）
                .maxAge(3600);  // 预检请求的缓存时间（3600秒即1小时）
    }
}
