package com.example.book.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@Controller
@RequestMapping("/book")
public class BookController {
    @GetMapping("/list")
    public void getList() {
        log.info("/book/list 요청");
    }

}
