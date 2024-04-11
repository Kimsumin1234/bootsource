package com.example.book.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.book.dto.BookDto;
import com.example.book.service.BookService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Log4j2
@Controller
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService service;

    @GetMapping("/list")
    public void getList(Model model) {
        log.info("/book/list 요청");

        List<BookDto> list = service.getList();
        model.addAttribute("list", list);

    }

    @GetMapping("/create")
    public void getCreate(BookDto bookDto, Model model) {
        log.info("/book/create 요청");

        // 테이블에 있는 카테고리 명 보여주기
        model.addAttribute("categories", service.categoryNameList());
    }

    @PostMapping("/create")
    public String postCreate(@Valid BookDto bookDto, BindingResult result, RedirectAttributes rttr, Model model) {
        log.info("book post 요청 {}", bookDto);

        if (result.hasErrors()) {
            // 사용자가 입력을 잘못했을때 테이블에 있는 카테고리 명이 화면에 남게하기 위한 코드
            model.addAttribute("categories", service.categoryNameList());
            return "/book/create";
        }
        Long id = service.bookCreate(bookDto);
        rttr.addFlashAttribute("result", id);
        return "redirect:/book/list";

    }

    @GetMapping(value = { "/read", "/modify" })
    public void getRead(Long id, Model model) {
        log.info("/book/read get 요청 {}", id);
        BookDto dto = service.getRow(id);
        model.addAttribute("dto", dto);
    }

    @PostMapping("/modify")
    public String postModify(BookDto updateDto, RedirectAttributes rttr) {
        log.info("업데이트 요청 {}", updateDto);

        Long id = service.bookUpdate(updateDto);

        // 조회화면 이동
        // 주소줄에 보내는법 : addAttribute
        // 세션에 담아 보내는법 : addFlashAttribute
        rttr.addAttribute("id", id);
        return "redirect:/book/read";
    }

    @PostMapping("/delete")
    public String postDelete(Long id) {
        log.info("도서 삭제 요청 {}", id);
        service.bookDelete(id);
        return "redirect:/book/list";
    }

}
