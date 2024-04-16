package com.example.guestbook.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.guestbook.dto.GuestBookDto;
import com.example.guestbook.dto.PageRequestDto;
import com.example.guestbook.dto.PageResultDto;
import com.example.guestbook.entity.GuestBook;

@SpringBootTest
public class GuestBookServiceTest {

    @Autowired
    private GuestBookService service;

    @Test
    public void testList() {
        PageRequestDto requestDto = PageRequestDto.builder().page(11).size(10).type("tc").keyword("Title").build();

        PageResultDto<GuestBookDto, GuestBook> result = service.getList(requestDto);

        // 목록확인
        result.getDtoList().forEach(list -> System.out.println(list));

        System.out.println("prev : " + result.isPrev());
        System.out.println("next : " + result.isNext());
        System.out.println("total : " + result.getTotalPage());
        result.getPageList().forEach(page -> System.out.println(page));
    }
}
