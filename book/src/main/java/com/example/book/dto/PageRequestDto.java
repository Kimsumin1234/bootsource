package com.example.book.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class PageRequestDto {
    private int page;
    private int size;

    public PageRequestDto() {
        this.page = 1;
        this.size = 10;
    }

    public Pageable getPageable(Sort sort) {
        // Pageable 에 page 는 0부터 시작해서 (page - 1)
        // controller 에서 이작업을 해준다 , 원하는 페이지 입력, 게시글수 조절
        // Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        // 이걸 하기 위해서 담을수 있게 객체를 생성한다
        return PageRequest.of(page - 1, size, sort);
    }

}
