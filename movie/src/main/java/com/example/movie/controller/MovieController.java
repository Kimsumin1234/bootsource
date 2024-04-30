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

        // 검색 추가
        rttr.addAttribute("page", pageRequestDto.getPage());
        rttr.addAttribute("size", pageRequestDto.getSize());
        rttr.addAttribute("type", pageRequestDto.getType());
        rttr.addAttribute("keyword", pageRequestDto.getKeyword());

        return "redirect:/movie/list";
    }

    @GetMapping("/register")
    public void getRegister(@ModelAttribute("requestDto") PageRequestDto pageRequestDto, MovieDto movieDto) {
        log.info("영화 등록 폼 요청");
    }

    // 지금 상태는 파일선택만 하고 등록을 안하는 경우, DB에 저장은 안되지만 서버에 저장이 되는상황
    // 이걸 해결하기 위해서 DB에 저장된 파일과 서버에 저장된 파일을 비교하는 서비스를 만들어서
    // 일치하지 않는경우 서버를 DB와 일치하게 만드는 서비스를 또 만들어야한다
    @PostMapping("/register")
    public String postRegister(@ModelAttribute("requestDto") PageRequestDto pageRequestDto, MovieDto movieDto,
            RedirectAttributes rttr) {
        log.info("영화 등록 {}", movieDto);

        // 서비스 호출
        Long mno = service.movieInsert(movieDto);
        // mno 넘기기
        rttr.addFlashAttribute("msg", mno);

        // 검색 추가
        rttr.addAttribute("page", pageRequestDto.getPage());
        rttr.addAttribute("size", pageRequestDto.getSize());
        rttr.addAttribute("type", pageRequestDto.getType());
        rttr.addAttribute("keyword", pageRequestDto.getKeyword());

        return "redirect:/movie/list";
    }

    @PostMapping("/modify")
    public String postMethodName(@ModelAttribute("requestDto") PageRequestDto pageRequestDto, MovieDto movieDto,
            RedirectAttributes rttr) {
        log.info("movie 수정 요청 {}", movieDto);

        Long mno = service.movieUpdate(movieDto);

        rttr.addFlashAttribute("msg", mno);

        rttr.addAttribute("mno", movieDto.getMno());

        // 검색 추가
        rttr.addAttribute("page", pageRequestDto.getPage());
        rttr.addAttribute("size", pageRequestDto.getSize());
        rttr.addAttribute("type", pageRequestDto.getType());
        rttr.addAttribute("keyword", pageRequestDto.getKeyword());

        return "redirect:/movie/read";
    }

}
