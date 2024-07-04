package com.zaoan.zaoan_news.zaoan_news_server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 全局跨域配置
 * @author Jingzhen
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 为所有路径（/**表示任意多层的任意路径）添加CORS配置
        registry.addMapping("/**")
                // 设置允许客户端携带认证信息（如Cookies）。
                // 注意：当允许发送认证信息（credentials）时，不能将allowedOrigins设置为'*'。
                // 这是因为CORS规范中如果设置了allowCredentials为true，allowedOrigins不能为'*'。
                .allowCredentials(true)
                // 设置允许的源使用通配符*，这是一个宽松的设置，适用于不限制特定域的开发环境。
                // 使用allowedOriginPatterns代替deprecated的allowedOrigins方法，以支持更灵活的源匹配模式。
                .allowedOriginPatterns("*")
                // 设置允许通过的HTTP请求方法，包括GET, POST, PUT, DELETE和OPTIONS。
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // 设置允许的HTTP请求头部，设置为'*'允许所有头部。
                .allowedHeaders("*")
                // 设置响应的头部可以暴露给客户端JavaScript脚本访问，设置为'*'表示暴露所有头部。
                .exposedHeaders("*");
    }
}

