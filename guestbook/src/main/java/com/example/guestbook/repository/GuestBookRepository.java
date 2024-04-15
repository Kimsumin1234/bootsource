package com.example.guestbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.example.guestbook.entity.GuestBook;

// QuerydslPredicateExecutor<> 상속 받는 이유
// => 동적인 쿼리가 필요해서  ex) 검색작업
public interface GuestBookRepository extends JpaRepository<GuestBook, Long>, QuerydslPredicateExecutor<GuestBook> {

}
