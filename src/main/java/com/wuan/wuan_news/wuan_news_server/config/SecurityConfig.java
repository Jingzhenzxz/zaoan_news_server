package com.wuan.wuan_news.wuan_news_server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 16:13
 * @description
 */
@Configuration
// 启用 Spring Security
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /*
     * 如果把 WebSecurityConfigurerAdapter 换成最新的
     * SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>，
     * 那么会返回401错误，因为 WebSecurityConfigurerAdapter 通常会覆盖一些默认的配置，而 SecurityConfigurerAdapter 则不会。
     */
    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                // 禁用 CSRF（跨站请求伪造）保护
                .csrf().disable()
                // 禁用基本身份验证
                .httpBasic().disable()
                // 定义 URL 的访问权限：
                .authorizeRequests()
                // 路径 /api/authentication/** 的所有请求都被允许（不需要认证），其他所有请求都需要被认证
                .antMatchers("/api/authentication/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                // Allow Swagger UI
                .antMatchers("/swagger-ui/**").permitAll()
                // Allow API docs
                .antMatchers("/v3/api-docs/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .anyRequest().authenticated()
                .and()
                // 在 UsernamePasswordAuthenticationFilter 过滤器之前添加 JwtFilter 过滤器
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
