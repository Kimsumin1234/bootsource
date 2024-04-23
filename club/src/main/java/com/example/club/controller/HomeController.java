package com.example.club.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Log4j2
@Controller
public class HomeController {
    @PreAuthorize("permitAll()") // @EnableMethodSecurity 와 짝꿍
    @GetMapping("/")
    public String getHome() {
        log.info("home 요청");
        return "home";
    }

    // 회원에 담기는 Security 정보들 확인 (Authentication 객체) json 형태로 담김
    // 로그인을 성공하면 Authentication 객체를 리턴해준다
    @PreAuthorize("permitAll()")
    @ResponseBody
    @GetMapping("/auth")
    public Authentication getAuthenticationInfo() {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        return authentication;
    }

}
