package com.example.springstudy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // 비로그인도 접근 가능한 경로
                .requestMatchers("/", "/auth/**", "/css/**", "/js/**").permitAll()
                // GET 게시글 목록, 상세는 누구나 볼 수 있음
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/posts", "/posts/{id}").permitAll()
                // 나머지(게시글 작성/수정/삭제)는 로그인 필요
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/auth/login")           // 커스텀 로그인 페이지
                .loginProcessingUrl("/auth/login")  // form action URL (POST)
                .defaultSuccessUrl("/posts", true)  // 로그인 성공 후 이동
                .failureUrl("/auth/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/posts")
                .permitAll()
            );

        return http.build();
    }

    // 비밀번호 암호화 (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
