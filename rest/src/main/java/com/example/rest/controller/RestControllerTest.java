package com.example.rest.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.rest.dto.SampleDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

// 컨트롤러 어노테이션
// @Controller - 메소드가 끝나고 찾는 대상은 템플릿임

// @RestController - 데이터 자체를 리턴 가능
//                 - 객체 자체를 리턴 가능
//                 - 객체 => json 으로 변환하는 컨버터가 자동으로 돌아간다

@RestController
public class RestControllerTest {

    @GetMapping("/hello")
    public String getHome() {
        // 템플릿을 만들지 않아도 Hello World 가 나온다
        return "Hello World";
    }

    // headers 가 필수요소로 한번 알려주지만
    // produces, consume 으로 한번더 알기쉽게끔 알려주는 용도
    // produces : 서버 -> 브라우저로 데이터를 보낼때 어떤 타입인지 알려주는 용도
    // 지금 프로젝트 에서는 json 컨버터가 기본으로 돌아가서 생략가능
    // json 말고 다른 타입일 경우에는 명시해줘야함
    // consume : 브라우저 -> 서버 일 경우

    @GetMapping(value = "/sample", produces = MediaType.APPLICATION_JSON_VALUE)
    public SampleDto getSample() {
        // Model 에 담지 않아도 json 형태로 객체가 리턴이 된다
        SampleDto dto = new SampleDto();
        dto.setMno(1L);
        dto.setFirstName("홍");
        dto.setLastName("길동");
        return dto;
    }

    @GetMapping(value = "/sample2", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SampleDto> getSample2() {
        // Model 에 담지 않아도 json 형태로 배열이 리턴이 된다
        List<SampleDto> list = new ArrayList<>();

        LongStream.rangeClosed(1, 10).forEach(i -> {
            SampleDto dto = new SampleDto();
            dto.setMno(i);
            dto.setFirstName("홍");
            dto.setLastName("길동");
            list.add(dto);
        });
        return list;
    }

    // 데이터 + 상태코드(Http 상태코드 - 200,500,404, ...)
    // 상태코드 F12 에 들어가서 Network 에 들어가면 Status 로 확인 가능
    // ResponseEntity
    // http://localhost:8080/check?height=150&weight=45
    @GetMapping(value = "/check", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SampleDto> getCheck(double height, double weight) {

        SampleDto dto = new SampleDto();
        dto.setMno(1L);
        dto.setFirstName(String.valueOf(height));
        dto.setLastName(String.valueOf(weight));

        if (height < 150) {
            // BAD_REQUEST : 400
            return new ResponseEntity<SampleDto>(dto, HttpStatus.BAD_REQUEST);
        } else {
            // OK : 200
            return new ResponseEntity<SampleDto>(dto, HttpStatus.OK);
        }
    }

    // 경로 : /product/5678/1234
    // 화면에 이런식으로 출력 : ["category5678","productId1234"]
    @GetMapping("/product/{cat}/{pid}")
    public String[] getMethodName(@PathVariable("cat") String cat, @PathVariable("pid") String pid) {
        return new String[] {
                "category" + cat,
                "productId" + pid
        };
    }

}
