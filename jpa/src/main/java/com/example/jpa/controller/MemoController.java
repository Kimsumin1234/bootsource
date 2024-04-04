package com.example.jpa.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.jpa.dto.MemoDto;
import com.example.jpa.service.MemoServiceImpl;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor // MemoServiceImpl 에서 @Service 를 사용해서 객체를 생성해놨다, 생성해 논걸 주입해줘 어노테이션
@Log4j2
@Controller
@RequestMapping("/memo")
public class MemoController {

    private final MemoServiceImpl service;

    // /memo/list + GET
    @GetMapping("/list")
    public void list(Model model) {
        log.info("/memo/list 요청");

        List<MemoDto> list = service.getMemoList();

        // list를 list.html 에서 보여주고 싶다면?
        model.addAttribute("list", list);
    }

    // /memo/read?mno=3 + GET
    // /memo/modify?mno=3 + GET : 수정을 위해 화면 보여주기 요청
    @GetMapping({ "/read", "/modify" }) // 하는일이 같다면 경로를 여러개 지정 가능
    public void read(@RequestParam Long mno, Model model) {
        log.info("/memo/read form 요청");

        MemoDto mDto = service.getMemo(mno);
        model.addAttribute("mDto", mDto);
    }

    // /memo/modify + POST : 실제 수정 요청
    @PostMapping("/modify")
    public String modifyPost(MemoDto mDto, RedirectAttributes rttr) {
        log.info("modify 요청" + mDto);
        MemoDto updateDto = service.updateMemo(mDto);

        // 수정 완료 시 /memo/read 이동
        rttr.addAttribute("mno", updateDto.getMno());
        return "redirect:/memo/read";
    }

    // /memo/delete?mno=3 + GET : 삭제요청
    @GetMapping("/delete")
    public String deleteGet(@RequestParam Long mno) {
        log.info("메모 삭제 요청 {}", mno);
        service.deleteMemo(mno);
        // 삭제 성공시 리스트 화면으로 가기
        return "redirect:/memo/list";
    }

    // /memo/create + GET : 입력을 위해 화면 보여주기 요청
    // @PostMapping 에 @Valid MemoDto mDto 가 있으면 @GetMapping 메소드 변수 안에도 같은걸 넣어준다
    // @ModelAttribute("mDto") html 에서 사용할 객체명 변경
    @GetMapping("/create")
    public void createGet(@ModelAttribute("mDto") MemoDto mDto) {
        log.info("메모 입력 폼 요청");
    }

    // /memo/create + POST : 실제 입력 요청
    @PostMapping("/create")
    public String postMethodName(@Valid @ModelAttribute("mDto") MemoDto mDto, BindingResult result,
            RedirectAttributes rttr) {
        log.info("create post 요청 {}", mDto);

        if (result.hasErrors()) {
            return "/memo/create";
        }

        service.insertMemo(mDto);

        // list.html script 에서 사용함 const msg = "[(${result})]";
        rttr.addFlashAttribute("result", "SUCCESS");
        return "redirect:/memo/list";
    }

}
