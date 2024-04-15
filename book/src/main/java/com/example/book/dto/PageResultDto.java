package com.example.book.dto;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.Data;

// java Generics 개념
// PageResultDto<DTO, EN> : ~Dto, Entity 객체를 담기위해서 구조를 설계

@Data
public class PageResultDto<DTO, EN> {
    // 화면에 보여줄 목록
    private List<DTO> dtoList;

    // 총 페이지 번호
    private int totalPage;

    // 현재 페이지 번호
    private int page;

    // 목록 크기(한 페이지에 보여줄 게시물 수)
    private int size;

    // 시작, 끝 페이지 번호
    private int start, end;

    private boolean prev, next;

    // 페이지 번호 목록
    private List<Integer> pageList;

    public PageResultDto(Page<EN> result, Function<EN, DTO> fn) {
        dtoList = result.stream().map(fn).collect(Collectors.toList());

        // 페이지 나누기
        // 1. 전체 게시물 개수 : 360
        // 2. 한 페이지에 10개씩 보여준다면 : 전체 필요 페이지 수 36
        // 3. 총개수 / 한페이지당 보여줄 게시물 수 : totalPage = result.getTotalPages();
        totalPage = result.getTotalPages();

        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable) {
        // pageable.getPageNumber() : 사용자가 요청한 페이지 정보(페이지 번호가 0부터 시작해서 +1)
        this.page = pageable.getPageNumber() + 1;

        // 한페이지당 보여줄 게시물 수
        this.size = pageable.getPageSize();

        // ex) page=5, (Math.ceil(page / 10.0))=1
        int tempEnd = (int) (Math.ceil(page / 10.0)) * 10;
        this.start = tempEnd - 9;

        // totalPage= 총페이지 수
        this.end = totalPage > tempEnd ? tempEnd : totalPage;
        this.prev = start > 1;
        this.next = totalPage > tempEnd;

        // int 타입으로 1~10 생성후 => List<Integer> list 객체에 담기 위해서 boxed() 사용
        // boxed() : int ==> Integer
        this.pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }
}
