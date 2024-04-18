package com.example.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.rest.dto.SampleDto;

@Controller
public class BasicController {

    // @Controller - 메소드가 끝나고 찾는 대상은 템플릿임
    @GetMapping("/basic")
    @ResponseBody // 이걸 사용하면 템플릿 찾지말고 리턴값이 데이터값이라고 알려줌
    public String getMethodName() {
        return "반갑습니다";
    }

    // 데이터 + 상태코드(Http 상태코드 - 200,500,404,...)
    // ResponseEntity : 일반 컨트롤러에서도 리턴값이 데이터임을 의미 (@ResponseBody 사용안해도됨)
    // ex) 경로 : http://localhost:8080/check2?height=150&weight=45
    @GetMapping(value = "/check2", produces = MediaType.APPLICATION_JSON_VALUE)
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

}
