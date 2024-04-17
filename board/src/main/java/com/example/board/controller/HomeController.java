package com.example.board.controller;

import org.springframework.stereotype.Controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Log4j2
@Controller
public class HomeController {

    @GetMapping("/")
    public String getHome(RedirectAttributes rttr) {
        log.info("home get 요청");
        return "redirect:/board/list";
    }

}
