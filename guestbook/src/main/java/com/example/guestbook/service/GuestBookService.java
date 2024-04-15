package com.example.guestbook.service;

import java.util.List;

import com.example.guestbook.dto.GuestBookDto;
import com.example.guestbook.entity.GuestBook;

public interface GuestBookService {
    List<GuestBookDto> getList();

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
