package com.example.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.todo.entity.Todo;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    // find 만 치면 자동완성 해준다
    // completed 컬럼 값에 따라 조회 하는 메소드
    List<Todo> findByCompleted(Boolean completed);

    // https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html 사이트
    // where completed = ? order by id desc
    // id 를 기준으로 정렬해서 completed 컬럼 값에 따라 조회 하는 메소드
    List<Todo> findByCompletedOrderByIdDesc(Boolean completed);

    // important 컬럼 값에 따라 조회 하는 메소드
    List<Todo> findByImportant(Boolean important);
}
