package com.takeout.xianda.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors ->{})
                // 禁用 CSRF(前后端分离项目通常不需要)
                .csrf(csrf -> csrf.disable())

                // 设置会话管理为无状态
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 配置授权规则
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                        // 放行 API 文档相关接口
                        .requestMatchers("/doc.html").permitAll()
                        .requestMatchers("/doc.html/**").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/webjars/**").permitAll()
                        .requestMatchers("/api-docs/**").permitAll()

                        // 放行认证接口(登录、注册等)
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/orders/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("api/products/**").permitAll()
                        .requestMatchers("/employee/**").permitAll()
                        .requestMatchers("/employee/**").hasRole("ADMIN")

                        // 其他所有请求需要认证
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
