package com.example.movie.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.movie.dto.AuthMemberDto;
import com.example.movie.dto.MemberDto;
import com.example.movie.dto.PageRequestDto;
import com.example.movie.dto.PasswordChangeDto;
import com.example.movie.service.MovieUserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Log4j2
@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {

    private final MovieUserService movieUserService;

    @GetMapping("/login")
    public void getLogin(@ModelAttribute("requestDto") PageRequestDto pageRequestDto) {
        log.info("로그인 페이지 요청");
    }

    @PreAuthorize("isAuthenticated()") // 로그인 했으면 가능함
    @GetMapping("/profile")
    public void getProfile(@ModelAttribute("requestDto") PageRequestDto pageRequestDto) {
        log.info("프로필 페이지 요청");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/edit")
    public String getEdit(@ModelAttribute("requestDto") PageRequestDto pageRequestDto) {
        log.info("프로필 수정 페이지 요청");
        return "/member/edit-profile";
    }

    // /edit/nickname
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/edit/nickname")
    public String postEditNickName(@ModelAttribute("requestDto") PageRequestDto pageRequestDto, MemberDto upMemberDto,
            RedirectAttributes rttr, HttpSession session) {
        log.info("닉네임 수정 controller 요청");

        String msg = movieUserService.nickNameUpdate(upMemberDto);
        // SecurityContext 안에 저장된 Authentication 는 변경되지 않음
        // 해경방안
        // 1) 현재 세션 제거 => 재로그인 : session.invalidate();

        // 2) Authentication 값을 업데이트
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        // /auth 로 확인할수있는 "principal" 를 가져온다
        AuthMemberDto authMemberDto = (AuthMemberDto) authentication.getPrincipal();
        // 변경할 Nickname 을 넣어준다
        authMemberDto.getMemberDto().setNickname(upMemberDto.getNickname());
        // 다시 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        rttr.addFlashAttribute("msg", msg);

        return "redirect:/member/profile";
    }

    // /edit/password
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/edit/password")
    public String postEditPassword(@ModelAttribute("requestDto") PageRequestDto pageRequestDto, PasswordChangeDto pDto,
            HttpSession session, RedirectAttributes rttr) {
        log.info("비밀번호 수정 controller 요청");
        // 현재 비밀번호가 틀리면 /member/edit
        try {
            movieUserService.passwordUpdate(pDto);
        } catch (Exception e) {
            rttr.addFlashAttribute("error", e.getMessage());
            return "redirect:/member/edit";
        }

        // 비밀번호가 맞으면 세션 날리고 로그인 페이지로
        session.invalidate();

        return "redirect:/member/login";
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/register")
    public void getRegister(@ModelAttribute("requestDto") PageRequestDto pageRequestDto, MemberDto memberDto) {
        log.info("회원가입 페이지 요청");
    }

    @PostMapping("/register")
    public String postRegister(@Valid MemberDto insertDto, BindingResult result,
            @ModelAttribute("requestDto") PageRequestDto pageRequestDto, RedirectAttributes rttr, Model model) {
        log.info("회원가입 요청 {}", insertDto);
        // 유효성 검사
        if (result.hasErrors()) {
            // forward 방식으로 보냈기 때문에 MemberDto insertDto 이거를 살려서
            // th:value="${memberDto.email}" 이거를 보여줄수있다
            // "redirect:/member/register" 이방식으로 보내면 MemberDto insertDto 이거를 살릴수없음
            return "/member/register";
        }

        String newEmail = "";
        // 중복 이메일 검사
        try {
            newEmail = movieUserService.register(insertDto);
        } catch (Exception e) {
            // 이방식은 MemberDto insertDto 이거를 살릴수없음
            // rttr.addFlashAttribute("Exception", e.getMessage());
            // return "redirect:/member/register";

            // model 에 담으면 Exception 메세지도 띄우고 MemberDto insertDto 도 살릴수있다
            model.addAttribute("Exception", e.getMessage());
            return "/member/register";
        }

        rttr.addFlashAttribute("newEmail", newEmail);
        return "redirect:/member/login";
    }

    @GetMapping("/leave")
    public void getLeave(@ModelAttribute("requestDto") PageRequestDto pageRequestDto) {
        log.info("회원탈퇴 페이지 요청");
    }

    @PostMapping("/leave")
    public String postLeave(@ModelAttribute("requestDto") PageRequestDto pageRequestDto, RedirectAttributes rttr,
            HttpSession session, MemberDto leaveMemberDto) {
        log.info("회원탈퇴 요청 {}", leaveMemberDto);

        try {
            movieUserService.leave(leaveMemberDto);
        } catch (Exception e) {
            rttr.addFlashAttribute("error", "이메일이나 비밀번호를 확인해 주세요");
            return "redirect:/member/leave";
        }

        // 회원탈퇴 성공하면 세션 날리기
        session.invalidate();

        return "redirect:/";
    }

}
