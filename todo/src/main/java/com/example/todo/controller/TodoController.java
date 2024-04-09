package com.example.todo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.todo.dto.TodoDto;
import com.example.todo.service.TodoService;
import com.example.todo.service.TodoServiceImpl;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Log4j2
@Controller
@RequestMapping("/todo")
public class TodoController {

    private final TodoService service;

    // 멤버변수 초기화 방법 - 1) 생성자 2) setter 스프링에선 이방법 말고
    // @RequiredArgsConstructor + final 방식을 사용한다 (DI 의존성주입)
    // @Service , @Entity , @Controller 등 어노테이션을 지정하면 객체를 생성하라고
    // 스프링프레임워크 한테 알려주는것이다

    @GetMapping("/list")
    public String getList(Model model) {
        log.info("list 요청");

        List<TodoDto> list = service.getList();
        model.addAttribute("list", list);

        return "/todo/list";
    }

    @GetMapping("/create")
    public void getCreate() {
        log.info("create 탬플릿 요청");
    }

    @PostMapping("/create")
    public String postCreate(TodoDto dto, RedirectAttributes rttr) {
        log.info("create 요청");

        TodoDto result = service.create(dto);

        rttr.addFlashAttribute("msg", result.getId());

        return "redirect:/todo/list";
    }

    // todo/read?id= 요청처리
    @GetMapping("/read")
    public void getRead(@RequestParam Long id, Model model) {
        log.info("read 요청 {}", id);
        TodoDto todo = service.getTodo(id);
        model.addAttribute("todo", todo);
    }

    // todo/done 완료 목록 처리
    @GetMapping("/done")
    public void getDone(Model model) {
        log.info("완료목록 요청");
        List<TodoDto> list = service.getCompletedList();
        model.addAttribute("list", list);
    }

    // todo/update
    // @RequestBody 를 썻는데 HttpMediaTypeNotSupportedException: Content-Type
    // 'application/x-www-form-urlencoded;charset=UTF-8' is not supported 가 떠서
    // @RequestParam 으로 바꿔서 해결
    @PostMapping("/update")
    public String postUpdate(@RequestParam Long id, RedirectAttributes rttr) {
        // id 받기
        Long id2 = service.todoUpdate(id);

        rttr.addAttribute("id", id2);

        return "redirect:/todo/read";
    }

    @PostMapping("/delete")
    public String postDelete(@RequestParam Long id) {
        service.todoDelete(id);

        return "redirect:/todo/list";
    }

}
