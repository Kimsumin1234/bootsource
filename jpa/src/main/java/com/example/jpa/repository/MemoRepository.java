package com.example.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jpa.entity.Memo;

// extends JpaRepository< Entity , Entity 에서 기본키 타입 >
public interface MemoRepository extends JpaRepository<Memo, Long> {
    // DAO 역할
    // 메소드를 아무것도 작성안해도 기본적인 CRUD 메소드를 호출할수있다

    // mno 가 5보다 작은 메모 조회
    List<Memo> findByMnoLessThan(Long mno);

    // mno 가 10보다 작은 메모 조회(mno 내림차순)
    List<Memo> findByMnoLessThanOrderByMnoDesc(Long mno);

    // mno 가 50이상 and mno 가 70이하 메모조회
    List<Memo> findByMnoBetween(Long start, Long end);
}
