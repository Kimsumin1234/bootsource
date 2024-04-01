package com.example.web1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.web1.dto.AddDto;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Log4j2
@Controller
@RequestMapping("/calc")
public class AddController {
    // get 요청 : http://localhost:8080/calc/add 주소창 링크
    @GetMapping("/add")
    public void addGet() {
        log.info("/calc/add 요청");
    }

    // 사용자 입력 값 가져오기 (post 방식)
    // 2,3 번 방식을 많이 사용
    // 1. HttpServletRequest req
    // 2. 파라메터 를 이용
    // 3. DTO 이용
    // - 장점 : post 컨트롤러 응답 후 보여지는 화면단에서 dto 에 들어있는 값을 사용 가능

    // 1. HttpServletRequest req
    // @PostMapping("/add")
    // public void addPost(HttpServletRequest req) {
    // log.info("/calc/add post 요청");
    // log.info("num1 {} ", req.getParameter("num1"));
    // log.info("num2 {} ", req.getParameter("num2"));
    // }

    // 2. 파라메터 를 이용
    // 원래는 입력값은 다 String 으로 넘어와서 타입변환을 해야했는데
    // 스프링에서는 알아서 변환을 해준다
    // addPost(int num1, int num2) , <input name="num1"> 변수명 맞추기
    // @PostMapping("/add")
    // public void addPost(@RequestParam(value = "op1", defaultValue = "0") int
    // num1,
    // @RequestParam(value = "op2", defaultValue = "0") int num2) {
    // log.info("/calc/add post 요청");
    // log.info("num1 {} ", num1);
    // log.info("num2 {} ", num2);
    // }

    // post 요청 : num1, num2 입력하고 계산 누르고
    // 3. DTO 이용
    @PostMapping("/add")
    public void addPost(AddDto dto, Model model) {
        log.info("/calc/add post 요청");
        log.info("num1 {} ", dto.getNum1());
        log.info("num2 {} ", dto.getNum2());

        // 덧셈결과 보여주는 방법 1
        // - AddDto.java 에 result 변수 추가해서 하는 방법
        // dto.setResult(dto.getNum1() + dto.getNum2());

        // 덧셈결과 보여주는 방법 1
        // - Model 객체 이용하는 방법
        model.addAttribute("result", dto.getNum1() + dto.getNum2());
    }

    // 데이터 보내기
    // reguest.setAttribute("이름",값) == Model (스프링에서 같은 역할을 해주는 객체를 만들어둠)

}
