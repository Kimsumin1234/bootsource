package com.example.club.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.build();
    }

    // 암호화(encode), 비밀번호 입력값 검증(matches) : PasswordEncoder
    // 단방향 암호화 : 암호화만 가능하고 암호해석은 불가능
    // 그래서 비밀번호 찾기할때 아예 새 비밀번호를 만드는 이유가 암호해석이 불가능해서
    @Bean // 객체 생성 어노테이션
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
