package com.example.web1.controller;

import org.springframework.stereotype.Controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@Controller
public class HomeController {

    // Exception 메세지가 vscode 로 출력
    // @RequestMapping(value = "경로", method = RequestMethod.GET(get방식으로 요청, 기본적으로
    // 링크타고 들어가는건 get방식이다))
    // @RequestMapping(value = "/", method = RequestMethod.GET)

    // @GetMapping("경로") 이 간단해서 요즘에는 이걸 많이 쓴다
    @GetMapping("/")
    public String home() {
        // log = sout
        // c.e.web1.controller.HomeController : home 요청 콘솔창에 이렇게 표시가 뜬다
        log.info("home 요청");

        // 경로 : templates 아래 경로부터 시작해서 확장자 빼고 파일명만 적는다
        return "index";
    }

}
