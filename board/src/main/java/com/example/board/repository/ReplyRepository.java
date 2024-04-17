package com.example.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.board.entity.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    // bno를 이용해서 reply 삭제
    // Reply 에 Id 는 rno 이기 때문에 deleteById(bno) 를 못한다
    // @Query("delete from Reply r where r.board.bno = ?1")
    @Modifying // delete, update 구문을 쓸때 반드시 필요, 안쓰면 select 가 적용됨
    @Query("delete from Reply r where r.board.bno =:bno")
    void deldeleteByBno(Long bno);
}
