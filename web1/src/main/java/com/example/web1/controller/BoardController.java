package com.example.web1.controller;

import org.springframework.stereotype.Controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Log4j2
@Controller
public class BoardController {

    // http://localhost:8080/board/create 에 응답하는 요청
    @GetMapping("/board/create")
    public void create() {
        log.info("/board/create 요청");
    }

}
