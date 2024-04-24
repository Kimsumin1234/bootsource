package com.example.board.repository;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.board.entity.Board;
import com.example.board.entity.Member;
import com.example.board.entity.Reply;

@SpringBootTest
public class ReplyRepositoryTest {

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void testInsert() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            long bno = (long) (Math.random() * 100) + 1;

            Board board = Board.builder().bno(bno).build();
            Member member = Member.builder().email("user2@naver.com").build();

            Reply reply = Reply.builder()
                    .text("Reply..." + i)
                    .replyer(member)
                    .board(board)
                    .build();

            replyRepository.save(reply);
        });
    }

    @Transactional
    @Test
    public void getRow() {
        Reply reply = replyRepository.findById(7L).get();
        System.out.println(reply);

        // fetch = FetchType.LAZY 이기 때문에 reply 부모게시글 안가져옴
        System.out.println(reply.getBoard());
    }

    @Transactional
    @Test
    public void getReplies() {
        // bno 를 미리 알고있다는 전제, 댓글볼때 게시글을 들어가서 보니깐
        // SELECT * FROM REPLY r WHERE r.BOARD_BNO = 100;
        Board board = Board.builder().bno(100L).build();
        List<Reply> replies = replyRepository.getRepliesByBoardOrderByRnoDesc(board);
        System.out.println(replies);
    }
}
