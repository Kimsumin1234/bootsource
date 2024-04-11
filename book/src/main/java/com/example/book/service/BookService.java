package com.example.book.service;

import java.util.List;

import com.example.book.dto.BookDto;
import com.example.book.entity.Book;

public interface BookService {
    List<BookDto> getList();

    Long bookCreate(BookDto dto);

    List<String> categoryNameList();

    BookDto getRow(Long id);

    Long bookUpdate(BookDto updateDto);

    void bookDelete(Long id);

    public default BookDto entityToDto(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .writer(book.getWriter())
                .price(book.getPrice())
                .salePrice(book.getSalePrice())
                .createdDate(book.getCreatedDate())
                .lastModifiedDate(book.getLastModifiedDate())
                .categoryName(book.getCategory().getName())
                .publisherName(book.getPublisher().getName())
                .build();

    }

    public default Book dtoToEntity(BookDto dto) {
        return Book.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .writer(dto.getWriter())
                .price(dto.getPrice())
                .salePrice(dto.getSalePrice())
                .build();
    }
}
