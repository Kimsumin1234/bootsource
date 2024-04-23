package com.example.club.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices.RememberMeTokenAlgorithm;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.club.handler.ClubLoginSuccessHandler;

@EnableMethodSecurity // @PreAuthorize 활성화
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, RememberMeServices rememberMeServices) throws Exception {
        // 이방법은 경로가 많아지면 코드가 길어진다
        // /static/**,"/img/*" : static 에 들어있는것도 security 에 걸리기 때문에 전부 허가해준다
        // successHandler() : 로그인 성공후 이동하는 페이지 경로
        // http.authorizeHttpRequests(authorize -> {
        // authorize.requestMatchers("/", "/auth", "/static/**", "/img/*").permitAll()
        // .requestMatchers("/club/member").hasAnyRole("USER", "MANAGER", "ADMIN")
        // .requestMatchers("/club/manager").hasAnyRole("MANAGER")
        // .requestMatchers("/club/admin").hasAnyRole("ADMIN");
        // })

        // Controller 에서 hasAnyRole 조건을 줄수있다
        // .anyRequest().permitAll() : 일단은 전부 연다 왜냐하면 Controller 에서 걸러주니깐
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/static/**", "/img/*").permitAll()
                .anyRequest().permitAll())
                .formLogin(login -> login
                        .loginPage("/club/member/login").permitAll()
                        .successHandler(clubLoginSuccessHandler()))
                .oauth2Login(login -> login.successHandler(clubLoginSuccessHandler())) // 공통인증방식 추가
                .rememberMe(remember -> remember.rememberMeServices(rememberMeServices)) // 자동 로그인
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/club/member/logout"))
                        .logoutSuccessUrl("/"));
        return http.build();
    }

    // 자동 로그인 처리 - 1) 쿠키 이용 2) 데이터베이스 이용
    // 1) 쿠키 이용
    @Bean
    RememberMeServices rememberMeServices(UserDetailsService userDetailsService) {
        // RememberMeTokenAlgorithm.SHA256 : 비밀번호 알고리즘 (암호화 시켜서 저장)
        RememberMeTokenAlgorithm encodingAlgorithm = RememberMeTokenAlgorithm.SHA256;
        // TokenBased : Token 기반의 쿠키
        TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices("myKey", userDetailsService,
                encodingAlgorithm);
        rememberMeServices.setTokenValiditySeconds(60 * 60 * 24 * 7); // 7일, 쿠키 만료 시간 (필수로설정)
        return rememberMeServices;
    }

    // 암호화(encode), 비밀번호 입력값 검증(matches) : PasswordEncoder
    // 단방향 암호화 : 암호화만 가능하고 암호해석은 불가능
    // 그래서 비밀번호 찾기할때 아예 새 비밀번호를 만드는 이유가 암호해석이 불가능해서
    @Bean // 객체 생성 어노테이션
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // spring 컨테이너가 관리해야 해서 어노테이션 붙여줌
    // 객체 호출
    @Bean
    ClubLoginSuccessHandler clubLoginSuccessHandler() {
        return new ClubLoginSuccessHandler();
    }
}
