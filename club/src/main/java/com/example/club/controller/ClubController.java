package com.example.club.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.club.dto.ClubMemberDto;
import com.example.club.service.ClubMemberService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Log4j2
@Controller
@RequestMapping("/club")
public class ClubController {

    private final ClubMemberService service;

    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    @GetMapping("/member")
    public void getMember() {
        log.info("member 요청");
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/manager")
    public void getManager() {
        log.info("manager 요청");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public void getAdmin() {
        log.info("admin 요청");
    }

    @GetMapping("/member/login")
    public void getLogin() {
        log.info("로그인 페이지 요청");
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/member/register")
    public void getRegister(ClubMemberDto member) {
        log.info("회원가입 페이지 요청");
    }

    @PostMapping("/member/register")
    public String postRegister(ClubMemberDto member, RedirectAttributes rttr) {
        log.info("회원가입 요청 {}", member);
        // 회원가입 서비스 호출
        String email = service.register(member);
        return "redirect:/club/member/login";
    }

}
