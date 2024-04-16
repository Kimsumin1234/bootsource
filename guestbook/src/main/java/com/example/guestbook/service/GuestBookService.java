package com.example.guestbook.service;

import java.util.List;

import com.example.guestbook.dto.GuestBookDto;
import com.example.guestbook.dto.PageRequestDto;
import com.example.guestbook.dto.PageResultDto;
import com.example.guestbook.entity.GuestBook;

public interface GuestBookService {
    // 페이지 나누기 전
    // List<GuestBookDto> getList();

    // 페이지 나누기 후
    // PageResultDto<DTO, EN> : PageResultDto<GuestBookDto, GuestBook>, 페이지 나누기 정보를
    // 사용할 객체
    // PageRequestDto requestDto : 페이지 나누기 정보를 저장할 객체
    PageResultDto<GuestBookDto, GuestBook> getList(PageRequestDto requestDto);

    GuestBookDto getRow(Long gno);

    Long guestBookUpdate(GuestBookDto updateDto);

    void guestBookDelete(Long gno);

    Long GuestBookCreate(GuestBookDto dto);

    public default GuestBookDto entityToDto(GuestBook book) {
        return GuestBookDto.builder()
                .gno(book.getGno())
                .title(book.getTitle())
                .writer(book.getWriter())
                .content(book.getContent())
                .createdDate(book.getCreatedDate())
                .lastModifiedDate(book.getLastModifiedDate())
                .build();

    }

    public default GuestBook dtoToEntity(GuestBookDto dto) {
        return GuestBook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .writer(dto.getWriter())
                .content(dto.getContent())
                .build();
    }
}
