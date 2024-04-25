package com.example.movie.dto;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.Data;

// entity 타입의 리스트를 dto 타입 리스트로 변환
// 공통타입 : DTO - dto , EN - entity
@Data
public class PageResultDto<DTO, EN> {

    private List<DTO> dtoList;

    // 화면에서 시작 페이지 번호
    // 화면에서 끝 페이지 번호
    private int start, end;

    // 이전/다음 이동 링크 여부
    private boolean prev, next;

    // 현재 페이지 번호
    private int page;

    // 총 페이지 번호
    private int totalPage;

    // 게시물 수
    private int size;

    // 페이지 번호 목록
    private List<Integer> pageList;

    // Page<EN> result : 스프링에서 페이지 나누기와 관련된 모든 정보를 가지고 있는 객체
    // Function<EN, DTO> fn : entity 를 dto 로 변환해주는 메소드가 Function에 있다
    // fn : entity -> entityToDto(entity) 이러한 메소드가 담겨있다
    public PageResultDto(Page<EN> result, Function<EN, DTO> fn) {
        this.dtoList = result.stream().map(fn).collect(Collectors.toList());

        this.totalPage = result.getTotalPages();
        makePageList(result.getPageable());
    }

    public void makePageList(Pageable pageable) {

        // spring 에서 페이지는 0부터 시작하기 때문에 사용자입력값이랑 맞추려고 +1
        this.page = pageable.getPageNumber() + 1;
        this.size = pageable.getPageSize();

        // ceil : 올림
        // int tempEnd = (int) (Math.ceil(현재 페이지번호 / 10.0)) * 10;
        int tempEnd = (int) (Math.ceil(page / 10.0)) * 10;
        // start는 값이 이런식으로 고정 20-9=11 , 10-9=1 , 30-9=21
        this.start = tempEnd - 9;
        // end 는 총게시글 수에 따라 20으로 끝날수도 17로 끝날수있기 때문에
        this.end = totalPage > tempEnd ? tempEnd : totalPage;

        this.prev = start > 1;
        this.next = totalPage > tempEnd;

        // List<Integer>
        // boxed() : start, end 가 int 라서 Integer 로 담기 위해서 사용
        this.pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }

}
