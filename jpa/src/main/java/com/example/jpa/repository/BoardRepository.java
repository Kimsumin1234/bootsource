package com.example.jpa.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jpa.entity.Board;
import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    // 기본제공 : 1. id로 찾기, 2. 전체찾기
    // title 로 찾기
    List<Board> findByTitle(String title); // 완전히 일치

    // writer 로 찾기
    List<Board> findByWriter(String writer); // 완전히 일치

    // like
    List<Board> findByTitleLike(String title);

    List<Board> findByTitleStartingWith(String title);

    List<Board> findByTitleEndingWith(String title);

    List<Board> findByTitleContaining(String title);

    // writer 가 user 로 시작하는 작성자 찾기
    List<Board> findByWriterStartingWith(String writer);

    // title 에 Title 이라는 문자열이 포함되어 있거나
    // content 에 Content 문자열이 포함되어 있는 데이터 조회
    List<Board> findByTitleContainingOrContent(String title, String content);

    List<Board> findByTitleContainingOrContentContaining(String title, String content);

    // title 에 Title 문자열이 포함되어 있고, id 가 50 보다 큰 게시물 조회
    List<Board> findByTitleContainingAndIdGreaterThan(String title, Long id);

    // id 가 50 보다 큰 게시물 조회 시 내침차순 정렬
    List<Board> findByIdGreaterThanOrderByIdDesc(Long id);

    // Pageable (org.springframework.data.domain.Pageable) : 페이지나누기 할때 도움을준다
    List<Board> findByIdGreaterThanOrderByIdDesc(Long id, Pageable pageable);

}
