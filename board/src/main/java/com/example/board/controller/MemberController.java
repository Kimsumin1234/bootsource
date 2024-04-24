package com.example.board.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.board.dto.MemberDto;
import com.example.board.dto.PageRequestDto;
import com.example.board.service.MemberService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Log4j2
@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService service;

    @PreAuthorize("permitAll()")
    @GetMapping("/login")
    public void getLogin(@ModelAttribute("requestDto") PageRequestDto requestDto) {
        log.info("로그인 폼 요청");
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/register")
    public void getRegister(MemberDto memberDto, @ModelAttribute("requestDto") PageRequestDto requestDto) {
        log.info("회원가입 페이지 요청");
    }

    @PostMapping("/register")
    public String postRegister(@Valid MemberDto memberDto, BindingResult result,
            @ModelAttribute("requestDto") PageRequestDto requestDto, RedirectAttributes rttr) {
        log.info("회원가입 post 요청 {}", memberDto);

        // 유효성 검사
        if (result.hasErrors()) {
            return "/member/register";
        }

        // 아이디 중복 검사
        try {
            service.register(memberDto);
        } catch (Exception e) {
            String msg = e.getMessage();
            log.info(msg);
            rttr.addFlashAttribute("msg", msg);
            return "redirect:/member/register";
        }

        return "redirect:/member/login";
    }

}
