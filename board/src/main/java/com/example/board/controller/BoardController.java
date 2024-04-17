package com.example.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.board.dto.BoardDto;
import com.example.board.dto.PageRequestDto;
import com.example.board.service.BoardService;

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
    public void getRead(Long bno, Model model) {
        log.info("read or modify get 요청");

        model.addAttribute("dto", service.getRow(bno));
    }

    @PostMapping("/modify")
    public String postModify(BoardDto updateDto, RedirectAttributes rttr) {

        service.update(updateDto);

        rttr.addAttribute("bno", updateDto.getBno());

        return "redirect:/board/read";
    }

    @PostMapping("/remove")
    public String postRemove(Long bno, RedirectAttributes rttr) {

        service.removeWithReplies(bno);

        return "redirect:/board/list";
    }

}
