package com.example.guestbook.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.guestbook.dto.GuestBookDto;
import com.example.guestbook.dto.PageRequestDto;
import com.example.guestbook.dto.PageResultDto;
import com.example.guestbook.entity.GuestBook;
import com.example.guestbook.entity.QGuestBook;
import com.example.guestbook.repository.GuestBookRepository;
import com.querydsl.core.BooleanBuilder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class GuestBookServiceImpl implements GuestBookService {

    private final GuestBookRepository guestBookRepository;

    // 페이지 나누기 전
    // @Override
    // public List<GuestBookDto> getList() {
    // List<GuestBook> guestBooks =
    // guestBookRepository.findAll(Sort.by("gno").descending());
    // Function<GuestBook, GuestBookDto> fn = (entity -> entityToDto(entity));
    // return guestBooks.stream().map(fn).collect(Collectors.toList());
    // }

    // 페이지 나누기 후 List
    @Override
    public PageResultDto<GuestBookDto, GuestBook> getList(PageRequestDto requestDto) {
        Pageable pageable = requestDto.getPageable(Sort.by("gno").descending());
        // PagingAndSortingRepository 에 있는 findAll
        // Page<GuestBook> result = guestBookRepository.findAll(pageable);

        // querydsl 에 있는 findAll 사용
        BooleanBuilder builder = getSearch(requestDto);
        Page<GuestBook> result = guestBookRepository.findAll(builder, pageable);

        Function<GuestBook, GuestBookDto> fn = (entity -> entityToDto(entity));

        // public PageResultDto(Page<EN> result, Function<EN, DTO> fn)
        return new PageResultDto<GuestBookDto, GuestBook>(result, fn);
    }

    @Override
    public GuestBookDto getRow(Long gno) {
        GuestBook guestBook = guestBookRepository.findById(gno).get();
        return entityToDto(guestBook);
    }

    @Override
    public Long guestBookUpdate(GuestBookDto updateDto) {
        GuestBook guestBook = guestBookRepository.findById(updateDto.getGno()).get();
        guestBook.setTitle(updateDto.getTitle());
        guestBook.setContent(updateDto.getContent());
        GuestBook newGuestBook = guestBookRepository.save(guestBook);
        return newGuestBook.getGno();
    }

    @Override
    public void guestBookDelete(Long gno) {
        guestBookRepository.deleteById(gno);
    }

    @Override
    public Long GuestBookCreate(GuestBookDto dto) {
        GuestBook guestBook = dtoToEntity(dto);
        GuestBook newGuestBook = guestBookRepository.save(guestBook);
        return newGuestBook.getGno();
    }

    // Book 프로젝트는 동일한 메소드를 BookRepository 에 작성함
    private BooleanBuilder getSearch(PageRequestDto requestDto) {
        BooleanBuilder builder = new BooleanBuilder();

        // Q 클래스 사용
        // public static final QGuestBook guestBook = new QGuestBook("guestBook"); 이거를
        // 불러오는 메소드
        QGuestBook guestBook = QGuestBook.guestBook;

        // 사용자가 입력한 type, keyword 가져오기
        String type = requestDto.getType();
        String keyword = requestDto.getKeyword();

        // 부담을 줄이기 위해 PK 를 이용해서 조건 주기 : gno > 0
        builder.and(guestBook.gno.gt(0L));

        // 검색 타입이 없을 경우
        if (type == null || type.trim().length() == 0) {
            return builder;
        }

        // 검색 타입이 있는 경우
        // WHERE gno > 0 AND title LIKE '%title%' OR content LIKE '%content%' OR writer
        // LIKE '%writer%'
        // tc, tcw 도 있기 때문에 contains 를 사용
        BooleanBuilder conditionBuilder = new BooleanBuilder();
        if (type.contains("t")) {
            conditionBuilder.or(guestBook.title.contains(keyword));
        }
        if (type.contains("c")) {
            conditionBuilder.or(guestBook.content.contains(keyword));
        }
        if (type.contains("w")) {
            conditionBuilder.or(guestBook.writer.contains(keyword));
        }
        builder.and(conditionBuilder);

        return builder;
    }

}
