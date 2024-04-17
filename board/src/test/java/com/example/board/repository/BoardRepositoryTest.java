package com.example.board.repository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.example.board.entity.Board;
import com.example.board.entity.Member;

@SpringBootTest
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void testInsert() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Member member = Member.builder().email("user" + i + "@gmail.com").build();

            Board board = Board.builder()
                    .title("title..." + i)
                    .content("content..." + i)
                    .writer(member)
                    .build();
            boardRepository.save(board);
        });
    }

    // 테스트는 @Transactional 를 해야 board.getWriter() 를 불러온다
    @Transactional
    @Test
    public void readBoard() {
        Board board = boardRepository.findById(4L).get();

        // Board(bno=4, title=title...4, content=content...4,
        // writer=Member(email=user4@gmail.com, password=1111, name=USER4)) 관계 설정된 거까지
        // 다가져옴
        // FetchType.LAZY, (exclude = "writer") 로 바꿔서 필요할때만 정보를 가져오게 설정
        System.out.println(board.getWriter());
    }

    // 페이지 나누기 전
    // @Test
    // public void testList() {
    // List<Object[]> list = boardRepository.list();
    // for (Object[] objects : list) {

    // // [Board(bno=17, title=title...17, content=content...17),
    // // Member(email=user17@gmail.com, password=1111, name=USER17), 4]
    // // System.out.println(Arrays.toString(objects));

    // // 각각 담기
    // Board board = (Board) objects[0];
    // Member member = (Member) objects[1];
    // Long replyCnt = (Long) objects[2];
    // System.out.println(board + " " + member + " " + replyCnt);
    // }
    // }

    // 페이지 나누기 후
    @Test
    public void testList() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Object[]> list = boardRepository.list("title", "100", pageable);
        for (Object[] objects : list) {

            System.out.println(Arrays.toString(objects));

            // 각각 담기
            // Board board = (Board) objects[0];
            // Member member = (Member) objects[1];
            // Long replyCnt = (Long) objects[2];
            // System.out.println(board + " " + member + " " + replyCnt);
        }
    }

    @Test
    public void testGetRow() {
        Object[] row = boardRepository.getRow(5L);
        System.out.println(Arrays.toString(row));
    }

    @Transactional
    @Test
    public void testremove() {
        // bno=4 에 댓글이 달려있다
        // Reply 와 Board 가 묶여있다 둘중에 하나라도 실패가 나면 원상복구 해야해서
        // @Transactional 을 사용해서 두개의 테이블을 하나의 작업으로 만들어준다
        replyRepository.deldeleteByBno(4L);
        boardRepository.deleteById(4L);
    }
}
