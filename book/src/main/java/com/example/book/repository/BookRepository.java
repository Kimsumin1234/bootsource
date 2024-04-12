package com.example.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.example.book.entity.Book;
import com.example.book.entity.QBook;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

public interface BookRepository extends JpaRepository<Book, Long>, QuerydslPredicateExecutor<Book> {

    // interface 클래스 이기 때문에 구현 메소드를 사용하기 위해서 default 사용
    public default Predicate makePredicate() {
        BooleanBuilder builder = new BooleanBuilder();
        QBook book = QBook.book;

        // id > 0 - DB 에 부담을 줄이기 위해 PK 를 조건에 사용
        builder.and(book.id.gt(0L));

        return builder;
    }
}
