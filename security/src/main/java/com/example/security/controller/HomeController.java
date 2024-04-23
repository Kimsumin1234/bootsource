package com.example.security.controller;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Log4j2
@Controller
public class HomeController {

    @GetMapping("/")
    public String getHome() {
        log.info("home 요청");
        return "home";
    }

    // 회원에 담기는 Security 정보들 확인
    @ResponseBody
    @GetMapping("/auth")
    public Authentication getAuthenticationInfo() {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String username = authentication.getName();
        Object principal = authentication.getPrincipal();

        Collection<? extends GrantedAuthority> getAuthorities = authentication.getAuthorities();

        log.info("---------------------");
        log.info("username : {}", username);
        log.info("principal : {}", principal);
        log.info("getAuthorities : {}", getAuthorities);

        return authentication;
    }

}
