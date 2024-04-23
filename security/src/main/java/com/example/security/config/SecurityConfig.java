package com.example.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity // 웹에서 security 적용이 가능하도록 하는 어노테이션
@Configuration // == @Component(@Controller, @Service) : 객체 생성(목적-환경설정용)
public class SecurityConfig {

    // 접근제한 권한 개념

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // HTTP ERROR 403 : 권한이 없을경우 403 이 뜬다

        // requestMatchers("/", "/security/guest").permitAll() :
        // => "/", "/security/guest" 이 경로로 들어오면 권한 모두 열기

        // .formLogin(Customizer.withDefaults() : 403 페이지 대신 로그인화면 보여줌

        // 요청확인 (요청확인 작업후 controller 동작)
        http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/", "/security/guest", "/auth").permitAll()
                .requestMatchers("/security/member").hasRole("USER")
                .requestMatchers("/security/admin").hasRole("ADMIN"))

                // 인증처리 (웹에서는 대부분 form 로그인 작업)
                // .formLogin(Customizer.withDefaults()); : 디폴트 로그인 페이지
                .formLogin(login -> login
                        // .usernameParameter("userid") : username 속성 변경
                        // .passwordParameter("pwd") : password 속성 변경
                        // .successForwardUrl("") : 로그인 성공후 이동할 페이지 설정
                        .loginPage("/member/login").permitAll()) // 내가만든 로그인 페이지

                .logout(logout -> logout
                        .logoutSuccessUrl("/") // 로그아웃 후 이동 페이지 디폴트는 로그인 페이지
                        // 내가만든 로그아웃 페이지
                        .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout")));

        // 빨간줄 나면 퀵픽스 throws 하기
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        // 비밀번호 암호화
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // 임시 - 데이터베이스에 인증을 요청하는 객체
    // InMemoryUserDetailsManager - 메모리에 등록해 놓고 임시로 사용
    // SecurityTest 에서 비밀번호 1111 을 암호화 작업함
    @Bean
    UserDetailsService users() {
        UserDetails user = User.builder()
                // ID
                .username("user1")
                // 암호화된 비밀번호
                .password("{bcrypt}$2a$10$7orbyIs0T3a.6e7VV1yJMe7BVN1ejqiMHEgwFromxGPT.cYUqpoLu")
                // 요청확인
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
                .username("admin1")
                .password("{bcrypt}$2a$10$7orbyIs0T3a.6e7VV1yJMe7BVN1ejqiMHEgwFromxGPT.cYUqpoLu")
                .roles("USER", "ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }
}
