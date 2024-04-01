package com.example.web1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.web1.dto.MemberDto;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequestMapping("/member")
public class MemberController {
    // get 방식 : http://localhost:8080/member/login 여기서 사용자한테 입력받음
    @GetMapping("/login")
    public void login() {
        log.info("로그인 페이지 요청");
    }

    // @PostMapping("/login")
    // public void loginPost(String email, String name) {
    // log.info("로그인 정보 가져오기");
    // log.info("email {} ", email);
    // log.info("name {} ", name);
    // }

    // 데이터 보내기
    // reguest.setAttribute("이름",값) == Model (스프링에서 같은 역할을 해주는 객체를 만들어둠)
    // post 방식 : http://localhost:8080/member/login 여기서 입력 받은 내용을
    // http://localhost:8080/member/info 화면에 출력해줌
    // 근데 주소창은 /member/login 이 나온다 (forward 방식으로 움직임)
    // (MemberDto mDto, int page) 개별로 다르게 받을수 있다
    // input hidden 으로 담긴 값 넘기는 방법 (Model model) model.addAttribute("page", page);
    // @ModelAttribute ( "이름" ) : dto 객체 이름 지정
    @PostMapping("/login")
    public String loginPost(@ModelAttribute("mDto") MemberDto mDto, @ModelAttribute("page") int page, Model model) {
        log.info("dto로 로그인 정보 가져오기");
        log.info("email {} ", mDto.getEmail());
        log.info("name {} ", mDto.getName());
        log.info("page {} ", page);

        // req.setAttribute() 랑 동일한걸 스프링에서 Model 객체로 만들어둠
        // model.addAttribute("page", page); == @ModelAttribute("page") int page

        return "/member/info";
    }

}
