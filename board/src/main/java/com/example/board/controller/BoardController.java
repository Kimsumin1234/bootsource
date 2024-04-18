package com.example.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.board.dto.BoardDto;
import com.example.board.dto.PageRequestDto;
import com.example.board.service.BoardService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Log4j2
@RequiredArgsConstructor
@Controller
@RequestMapping("/board")
public class BoardController {

    private final BoardService service;

    @GetMapping("/list")
    public void getList(@ModelAttribute("requestDto") PageRequestDto requestDto, Model model) {
        log.info("list get 요청");

        model.addAttribute("result", service.getList(requestDto));
    }

    @GetMapping(value = { "/read", "modify" })
    public void getRead(Long bno, @ModelAttribute("requestDto") PageRequestDto requestDto, Model model) {
        log.info("read or modify get 요청");

        model.addAttribute("dto", service.getRow(bno));
    }

    @PostMapping("/modify")
    public String postModify(BoardDto updateDto, @ModelAttribute("requestDto") PageRequestDto requestDto,
            RedirectAttributes rttr) {

        service.update(updateDto);

        rttr.addAttribute("bno", updateDto.getBno());

        // 페이지 나누기 정보
        rttr.addAttribute("page", requestDto.getPage());
        rttr.addAttribute("type", requestDto.getType());
        rttr.addAttribute("keyword", requestDto.getKeyword());

        return "redirect:/board/read";
    }

    @PostMapping("/remove")
    public String postRemove(Long bno, @ModelAttribute("requestDto") PageRequestDto requestDto,
            RedirectAttributes rttr) {

        service.removeWithReplies(bno);

        // 페이지 나누기 정보
        rttr.addAttribute("page", requestDto.getPage());
        rttr.addAttribute("type", requestDto.getType());
        rttr.addAttribute("keyword", requestDto.getKeyword());

        return "redirect:/board/list";
    }

    @GetMapping("/create")
    public void getCreate(BoardDto boardDto, @ModelAttribute("requestDto") PageRequestDto requestDto) {
        log.info("create get 요청");

    }

    @PostMapping("/create")
    public String postCreate(@Valid BoardDto boardDto, BindingResult result,
            @ModelAttribute("requestDto") PageRequestDto requestDto,
            RedirectAttributes rttr) {
        log.info("create post 요청 {}", boardDto);

        if (result.hasErrors()) {
            return "/board/create";
        }

        Long bno = service.create(boardDto);

        rttr.addFlashAttribute("msg", bno);

        // 페이지 나누기 정보 보내기
        rttr.addAttribute("page", requestDto.getPage());
        rttr.addAttribute("type", requestDto.getType());
        rttr.addAttribute("keyword", requestDto.getKeyword());

        return "redirect:/board/list";
    }

}
