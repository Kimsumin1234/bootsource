package com.example.guestbook.repository;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.guestbook.entity.GuestBook;

@SpringBootTest
public class GuestBookRepositoryTest {

    @Autowired
    private GuestBookRepository guestBookRepository;

    @Test
    public void testInsert() {
        // 300개 테스트 데이터 삽입
        IntStream.rangeClosed(1, 300).forEach(i -> {
            GuestBook guestBook = GuestBook.builder()
                    .writer("user" + (i % 10))
                    .title("Guest Title..." + i)
                    .content("테스트 내용입니다" + i)
                    .build();
            guestBookRepository.save(guestBook);
        });
    }

    @Test
    public void testList() {
        // 전체 리스트
        guestBookRepository.findAll().forEach(guestbook -> System.out.println(guestbook));

    }

    @Test
    public void testRow() {
        // 특정 Row 조회
        System.out.println(guestBookRepository.findById(10L));
    }

    @Test
    public void testUpdate() {
        // 특정 Row 수정 (title, content)
        GuestBook guestBook = guestBookRepository.findById(3L).get();
        guestBook.setTitle("수정된 Title 3");
        guestBook.setContent("수정된 내용입니다 3");
        System.out.println(guestBookRepository.save(guestBook));
    }

    @Test
    public void testDelete() {
        guestBookRepository.deleteById(300L);
    }
}
