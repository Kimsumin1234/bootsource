package com.example.board.repository.search;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchBoardRepository {
    // @Query(select m,t from Member m join m.team t = ?1)
    // => select 가 2개 이상이라면 Object[] 로 담는다

    // 전체조회 시 board, member, reply 정보를 다 조회해야함
    // List<Object[]> list(); // 페이지 나누기 전
    Page<Object[]> list(String type, String keyword, Pageable pageable); // 페이지 나누기 후+검색

    // 상세조회
    Object[] getRow(Long bno);
}
