package com.example.movie.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.movie.dto.PageRequestDto;

@Log4j2
@Controller
public class HomeController {

    @GetMapping("/")
    public String getHome() {
        log.info("home 요청");
        return "redirect:/movie/list";
    }

    // 로그인을 안하면 모두 시큐리티에 걸려서 로그인 페이지로 가게되지만
    // 로그인 이후에 잘못된 경로로 접속을 시도할경우
    // 403 페이지나 404 페이지를 띄워줄수있다

    // 403 페이지 (접근권한 없음)
    @GetMapping("/access-denied")
    public void getDenied(@ModelAttribute("requestDto") PageRequestDto pageRequestDto) {
        log.info("접근제한 페이지");
    }

    // 404 페이지 (사용자가 존재하지않는 페이지 경로로 들어갈 경우)
    @GetMapping("/error")
    public String getError(@ModelAttribute("requestDto") PageRequestDto pageRequestDto) {
        log.info("404");
        return "/except/url404"; // 스프링에서 만들어 놓은 경로 형식을 맞추면 error.html 을 부를수있다
    }

    // 회원에 담기는 Security 정보들 확인 (Authentication 객체) json 형태로 담김
    // 로그인을 성공하면 Authentication 객체를 리턴해준다
    // 이거는 개발을 편하게 하기위한 확인용, 실제 프로젝트할떄는 작업이 끝난뒤 지워야함
    @PreAuthorize("permitAll()")
    @ResponseBody
    @GetMapping("/auth")
    public Authentication getAuthenticationInfo() {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        return authentication;
    }

}
