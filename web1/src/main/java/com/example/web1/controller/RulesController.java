package com.example.web1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.web1.dto.AddDto;
import com.example.web1.dto.RulesDto;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Log4j2
@Controller
@RequestMapping("/calc")
public class RulesController {
    @GetMapping("/rules")
    public void rulesGet() {
        log.info("/calc/rules 요청");
    }

    // <select name="rules" /> 일 경우
    // RulesDto 활용해서 혼자 연습해 본것
    // @PostMapping("/rules")
    // public String postMethodName(RulesDto dto, Model model) {
    // log.info("/calc/rules post 요청");

    // switch (dto.getRules()) {
    // case "+":
    // model.addAttribute("result", dto.getNum1() + dto.getNum2());
    // break;
    // case "-":
    // model.addAttribute("result", dto.getNum1() - dto.getNum2());
    // break;
    // case "*":
    // model.addAttribute("result", dto.getNum1() * dto.getNum2());
    // break;
    // case "/":
    // model.addAttribute("result", dto.getNum1() / dto.getNum2());
    // break;

    // default:
    // break;
    // }
    // return "/calc/result";
    // }

    // <select name="op" /> 일 경우
    @PostMapping("/rules")
    public String postMethodName(AddDto addDto, @ModelAttribute("op") String op, Model model) {
        log.info("/calc/rules post 요청");

        int result = 0;

        switch (op) {
            case "+":
                result = addDto.getNum1() + addDto.getNum2();
                break;
            case "-":
                result = addDto.getNum1() - addDto.getNum2();
                break;
            case "*":
                result = addDto.getNum1() * addDto.getNum2();
                break;
            case "/":
                result = addDto.getNum1() / addDto.getNum2();
                break;

            default:
                break;
        }

        // Model 객체 활용
        // model.addAttribute("result", result);

        // AddDto 에 result 변수 활용
        addDto.setResult(result);
        return "/calc/result";
    }

}
