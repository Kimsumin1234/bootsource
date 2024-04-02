package com.example.web1.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.web1.dto.SampleDto;

import lombok.extern.log4j.Log4j2;

// log. 을 호출가능
@Log4j2

// Controller 라고 어노테이션을 꼭 붙여준다
@Controller

// 공통인 주소를 뺄수있다
@RequestMapping("/sample")
public class SampleController {
    // 메소드는 거의 String, void 형태로 만든다

    // 404 : 경로 없음 (컨트롤러에 매핑 주소를 확인해 본다)

    // 500 : Error resolving template [sample/basic], template might not exist or
    // might not be accessible by any of the configured Template Resolvers
    // 이 에러인 경우 templates 폴더를 확인해 본다
    // 해결 : templates / sample / basic.html (폴더와 파일을 만들면 된다)

    // 400 : Bad Request, status = 400
    // Failed to convert value of type 'java.lang.String' to required type 'int';
    // For input string: ""
    // calc/add 에서 한쪽에 값을 안넣어서 이런 에러 메세지가 뜸
    // 해결 : addPost(@RequestParam(value = "op1", defaultValue = "0") int num1,
    // 값을 안넣었을때 default 값으로 0 을 줄수있다

    // 500 :
    // Optional int parameter 'num1' is present but cannot be translated into a null
    // AddController.java 에서 int num1 과 name="op1" 이 서로 다를경우 에러 메세지
    // 해결방법 : @RequestParam("op1") int num1

    // IllegalStateException: Ambiguous mapping.
    // - @GetMapping("/ex3") 경로가 중복이면 에러 메세지가 뜬다

    // 리턴타입 결정
    // 1) void : 경로와 일치하는 곳에 템플릿이 존재할 때
    // ex) http://localhost:8080/board/create 마지막은 html 파일로 해준다
    // ex) http://localhost:8080/board/user/create /board/user 는 폴더
    // ex) http://localhost:8080/board/user/member/create /board/user/member 는 폴더
    //
    // 2) String : 경로와 일치하는 곳에 템플릿이 없을 때
    // (템플릿의 위치와 관계없이 자유롭게 지정 가능)

    // get방식(링크타고 들어가는거) : http://localhost:8080/sample/basic 에 응답하는 요청
    @GetMapping("/basic")
    public void basic(Model model) {
        log.info("/sample/basic 요청");

        model.addAttribute("name", "홍길동");

        // 기존 방식
        // SampleDto sampleDto = new SampleDto();
        // sampleDto.setFirst("first");
        // sampleDto.setId(1L);
        // sampleDto.setLast("last");
        // sampleDto.setRegTime(LocalDateTime.now());

        // SampleDto.java 에 설정된 lombok @Builder 패턴 이용
        // 한개 담을때
        SampleDto sampleDto = SampleDto.builder()
                .first("first")
                .id(1L)
                .last("last")
                .regTime(LocalDateTime.now())
                .build();
        model.addAttribute("dto", sampleDto);

        // 여러개 담을때
        // 스트링 부트에서 id 는 주로 Long 타입을 쓴다
        List<SampleDto> list = new ArrayList<>();
        for (Long i = 1L; i < 21; i++) {
            SampleDto dto = SampleDto.builder()
                    .first("first" + i)
                    .id(i)
                    .last("last" + i)
                    .regTime(LocalDateTime.now())
                    .build();
            list.add(dto);
        }
        model.addAttribute("list", list);

        model.addAttribute("now", new Date());
        model.addAttribute("price", 123456789);
        model.addAttribute("title", "This is a just sample");
        model.addAttribute("options", Arrays.asList("AAAA", "BBBB", "CCCC", "DDDD"));
    }

    // http://localhost:8080/sample/ex1 에 응답하는 요청
    @GetMapping("/ex1")
    public void ex1(Model model) {
        log.info("/sample/ex1 요청");
        model.addAttribute("result", "SUCCESS");
    }

    // http://localhost:8080/board/create 에 응답하는 요청
    // 위에 @RequestMapping("/sample") 가 있어서 BoardController 를 새로 만든다

    // http://localhost:8080/sample/ex2 를 요청
    // /board/create 화면이 출력
    @GetMapping("/ex2")
    public String ex2() {
        log.info("/sample/ex2 요청");
        return "/board/create";
    }

    @GetMapping("/ex3")
    public void ex3() {
        log.info("/sample/ex3 요청");
    }

    @GetMapping("/ex4")
    public void ex4(String param1, String param2, Model model) {
        log.info("/sample/ex4 요청");
        log.info("param1 {}, param2 {}", param1, param2);

        model.addAttribute("param1", param1);
        model.addAttribute("param2", param2);
    }

    @GetMapping("/ex5")
    public void ex5() {
        log.info("/sample/ex5 요청");
    }

    @GetMapping("/ex6")
    public void ex6() {
        log.info("/sample/ex6 요청");
    }

}
