package com.example.movie.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDto {
    private int page;
    private int size;

    // 검색조건을 담을 변수 선언
    // String 은 초기화를 안해주면 null 이 뜬다
    private String type;
    private String keyword;

    // 페이지 정보가 안들어왔을때 기본값을 디폴트 생성자를 통해서 정한다
    public PageRequestDto() {
        this.page = 1;
        this.size = 10;

        // 검색조건 도 디폴트 설정 추가 (초기화작업)
        this.type = "";
        this.keyword = "";
    }

    // 스프링부트에서 페이지 나누기 정보를 저장해놓은 객체 => Pageable
    // PageRequestDto 만드는 목적 : Pageable 객체를 사용하기 위해서
    // 스프링부트에서 페이지는 0 부터 시작
    // page-1 : 사용자 요청 페이지, 사용자가 1페이지를 요청하면 스프링부트는 0페이지가 입력되야함
    public Pageable getPageable(Sort sort) {
        return PageRequest.of(page - 1, size, sort);
    }
}
