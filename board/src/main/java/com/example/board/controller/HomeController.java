package com.example.board.controller;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.board.dto.PageRequestDto;

@Log4j2
@Controller
public class HomeController {

    @PreAuthorize("permitAll()")
    @GetMapping("/")
    public String getHome(@ModelAttribute("requestDto") PageRequestDto requestDto, RedirectAttributes rttr) {
        log.info("home get 요청");

        // 페이지 나누기 정보 보내기
        rttr.addAttribute("page", requestDto.getPage());
        rttr.addAttribute("type", requestDto.getType());
        rttr.addAttribute("keyword", requestDto.getKeyword());

        return "redirect:/board/list";
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
