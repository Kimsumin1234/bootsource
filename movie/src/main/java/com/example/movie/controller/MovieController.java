package com.example.movie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.movie.dto.MovieDto;
import com.example.movie.dto.PageRequestDto;
import com.example.movie.dto.PageResultDto;
import com.example.movie.service.MovieService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/movie")
public class MovieController {

    private final MovieService service;

    @GetMapping("/list")
    public void getList(@ModelAttribute("requestDto") PageRequestDto pageRequestDto, Model model) {
        log.info("list 페이지 요청");

        PageResultDto<MovieDto, Object[]> result = service.getList(pageRequestDto);

        model.addAttribute("result", result);
    }

    @GetMapping({ "/read", "modify" })
    public void getRead(@ModelAttribute("requestDto") PageRequestDto pageRequestDto, @RequestParam Long mno,
            Model model) {
        log.info("영화 상세 페이지 요청 {}", mno);

        MovieDto movieDto = service.getRow(mno);

        model.addAttribute("dto", movieDto);
    }

    @PostMapping("/remove")
    public String postRemove(@ModelAttribute("requestDto") PageRequestDto pageRequestDto, Long mno,
            RedirectAttributes rttr) {
        log.info("영화 삭제 요청 {}", mno);

        service.movieRemove(mno);

        // 주소줄에 딸려보내는 개념
        rttr.addAttribute("page", pageRequestDto.getPage());

        return "redirect:/movie/list";
    }

}
