package com.example.movie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.movie.handler.CustomAccessDeniedHandler;

@EnableMethodSecurity
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/assets/**", "/css/**", "/js/**", "/auth").permitAll()
                .requestMatchers("/movie/list", "/movie/read").permitAll()
                .requestMatchers("/movie/modify").hasRole("ADMIN")
                .requestMatchers("/upload/display").permitAll() // 사진볼수있게 열어주기
                .requestMatchers("/reviews/**").permitAll() // 리뷰볼수있게 열어주기
                .requestMatchers("/member/register").permitAll()
                // .anyRequest().permitAll()); 모든요청 권한 열기
                .anyRequest().authenticated());
        // login 페이지는 /member/login 경로요청 해야한다고 알려줌 (기본 로그인창 안쓰고)
        http.formLogin(login -> login
                .loginPage("/member/login").permitAll()
                .defaultSuccessUrl("/movie/list", true));
        http.logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                .logoutSuccessUrl("/"));

        // 403 (접근제한) 페이지 띄우기 - 1. 정적 페이지와 연결
        // => static 폴더에 access-denied.html 를 사용
        // http.exceptionHandling(exception ->
        // exception.accessDeniedPage("/access-denied.html"));

        // 2. 핸들러 사용방법(좀더 다양한 형태로 처리하기 위해서 핸들러 클래스 작성)
        // => templates 폴더에 access-denied.html 를 사용 (templates에 있기때문에 레이아웃 적용가능)
        http.exceptionHandling(exception -> exception.accessDeniedHandler(customAccessDeniedHandler()));

        // get 을 제외한 모든방식은 csrf 토큰이 필요함
        // => thymeleaf 에서는 form:action 을 삽입하면 자동으로 만들어 준다
        // => 403 에러가 발생한다면 먼저 csrf 토큰이 포함되었는지 확인해 보는게 좋다

        // http.csrf(csrf -> csrf.disable()); // csrf.disable() : csrf 비활성화

        // 로그인페이지 디지안이 안나올 경우 : 세션에 문제가 있을수도 있다
        // => 세션을 항상 새로운거로 만들어 달라는 코드
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // 핸들러 사용 메소드(핸들러 클래스 호출)
    CustomAccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}
