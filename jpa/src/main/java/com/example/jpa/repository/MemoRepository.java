package com.example.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jpa.entity.Memo;

// extends JpaRepository< Entity , Entity 에서 기본키 타입 >
public interface MemoRepository extends JpaRepository<Memo, Long> {
    // DAO 역할
    // 메소드를 아무것도 작성안해도 기본적인 CRUD 메소드를 호출할수있다
}
