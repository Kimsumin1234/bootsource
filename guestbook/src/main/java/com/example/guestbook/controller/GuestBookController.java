package com.example.guestbook.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.guestbook.dto.GuestBookDto;
import com.example.guestbook.dto.PageRequestDto;
import com.example.guestbook.dto.PageResultDto;
import com.example.guestbook.entity.GuestBook;
import com.example.guestbook.service.GuestBookService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Log4j2
@Controller
@RequestMapping("/guestbook")
public class GuestBookController {

    private final GuestBookService service;

    // 페이지 나누기 전
    // @GetMapping("/list")
    // public void getList(Model model) {
    // log.info("list get 요청");
    // List<GuestBookDto> list = service.getList();
    // model.addAttribute("list", list);
    // }

    // 페이지 나누기 후
    @GetMapping("/list")
    public void getList(@ModelAttribute("requestDto") PageRequestDto requestDto, Model model) {
        log.info("list get 요청");
        PageResultDto<GuestBookDto, GuestBook> result = service.getList(requestDto);
        model.addAttribute("result", result);
    }

    @GetMapping(value = { "/read", "/modify" })
    public void getRead(Long gno, @ModelAttribute("requestDto") PageRequestDto requestDto, Model model) {
        log.info("read or modify get 요청");
        GuestBookDto dto = service.getRow(gno);
        model.addAttribute("dto", dto);
    }

    @PostMapping("/modify")
    public String postModify(GuestBookDto updateDto, RedirectAttributes rttr,
            @ModelAttribute("requestDto") PageRequestDto requestDto) {
        log.info("업데이트 요청 {}", updateDto);
        log.info("page 나누기 정보 {}", requestDto);

        Long gno = service.guestBookUpdate(updateDto);

        rttr.addAttribute("gno", gno);

        // 페이지 나누기 정보
        rttr.addAttribute("page", requestDto.getPage());
        rttr.addAttribute("type", requestDto.getType());
        rttr.addAttribute("keyword", requestDto.getKeyword());

        return "redirect:/guestbook/read";
    }

    @PostMapping("/delete")
    public String postDelete(Long gno, RedirectAttributes rttr,
            @ModelAttribute("requestDto") PageRequestDto requestDto) {

        service.guestBookDelete(gno);

        // 페이지 나누기 정보
        rttr.addAttribute("page", requestDto.getPage());
        rttr.addAttribute("type", requestDto.getType());
        rttr.addAttribute("keyword", requestDto.getKeyword());

        return "redirect:/guestbook/list";
    }

    @GetMapping("/create")
    public void getCreate(GuestBookDto guestBookDto, @ModelAttribute("requestDto") PageRequestDto requestDto) {
        log.info("create get 요청");
    }

    @PostMapping("/create")
    public String postCreate(@Valid GuestBookDto guestBookDto, BindingResult result, RedirectAttributes rttr,
            @ModelAttribute("requestDto") PageRequestDto requestDto) {
        log.info("create post 요청 {}", guestBookDto);

        if (result.hasErrors()) {
            return "/guestbook/create";
        }

        Long gno = service.GuestBookCreate(guestBookDto);

        rttr.addFlashAttribute("msg", gno);

        // 페이지 나누기 정보 보내기
        rttr.addAttribute("page", requestDto.getPage());
        rttr.addAttribute("type", requestDto.getType());
        rttr.addAttribute("keyword", requestDto.getKeyword());

        return "redirect:/guestbook/list";
    }

}
