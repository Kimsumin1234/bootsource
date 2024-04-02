package com.example.web1.controller;

import org.springframework.stereotype.Controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@Controller
public class HomeController {

    // Exception 메세지가 vscode 로 출력
    // @RequestMapping(value = "경로", method = RequestMethod.GET(get방식으로 요청, 기본적으로
    // 링크타고 들어가는건 get방식이다))
    // @RequestMapping(value = "/", method = RequestMethod.GET)

    // @GetMapping("경로") 이 간단해서 요즘에는 이걸 많이 쓴다
    @GetMapping("/")
    public String home() {
        // log = sout
        // c.e.web1.controller.HomeController : home 요청 콘솔창에 이렇게 표시가 뜬다
        log.info("home 요청");

        // 경로 : templates 아래 경로부터 시작해서 확장자 빼고 파일명만 적는다
        return "index";
    }

    // RedirectAttributes : redirect 시 데이터를 전달해 주는 객체

    // http://localhost:8080/ex3 요청
    // http://localhost:8080/ 이 화면이 나온다 주소도 이걸로 바뀜
    // 스프링 부트에서는 redirect 는 경로지정 (다른 컨트롤러에 있는 경로 포함해서)
    @GetMapping("/ex3")
    public String ex3(RedirectAttributes rttr) {
        log.info("/ex3 요청");
        // response.sendRedirect("/qList.do")
        // return "redirect:/";
        // return "redirect:/sample/basic";

        // path += "?bno="+bno
        // rttr.addAttribute("이름", 보내고싶은 값); : 파라메터로 전달이 된다
        // rttr.addFlashAttribute("이름", 보내고싶은 값); : Session 을 이용해서 값을 저장

        // rttr.addAttribute("bno", 15);
        // http://localhost:8080/sample/basic?bno=15 주소창에 이렇게 딸려보내짐
        // return "redirect:/sample/basic";

        // Session 을 이용해서 값을 저장한다
        // Flash : Session 을 사용은 하나 임시로 저장한다
        rttr.addFlashAttribute("bno", 15);
        // http://localhost:8080/sample/basic 주소창에 값이 안보인다
        return "redirect:/sample/basic";
    }

    // IllegalStateException: Ambiguous mapping.
    // - @GetMapping("/ex3") 경로가 중복이면 에러 메세지가 뜬다
    // - @PostMapping("/ex3") 일 경우는 가능하다
    // @GetMapping("/ex3")
    // public void ex4() {
    // log.info("/ex3 요청");
    // }

}
