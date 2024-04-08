package com.example.todo.repository;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.todo.entity.Todo;

@SpringBootTest
public class TodoRepositoryTest {
    // 모델2 에서의 DAO 와 TodoRepository 가 같은 개념 이니깐
    // service 에서 호출하는 메소드를 테스트 하는 작업
    // 나중에 한꺼번에 할려고 하면 에러가 났을때 찾기가 어려우니깐 차근차근 테스트를 한다

    @Autowired
    private TodoRepository todoRepository;

    // todo 삽입 하는 메소드
    @Test
    public void todoInsertTest() {
        // 오라클 에서는 디폴트 설정 이 잘되서 오류가 안나는데
        // jpa 에서는 디폴트 설정이 제대로 반영이 안되서 오류가 뜬다
        // insert into todotbl
        // (completed,created_date,impotant,last_modified_date,title,todo_id) values
        // (?,?,?,?,?,?) 이런식으로 ? 를 다 불러와서 값을 다 지정해야한다

        // IntStream.rangeClosed(1, 5).forEach(i -> {
        // Todo todo = Todo.builder().completed(true).important(true).title("title " +
        // i).build();
        // todoRepository.save(todo);
        // });
        // IntStream.rangeClosed(6, 10).forEach(i -> {
        // Todo todo = Todo.builder().completed(false).important(false).title("title " +
        // i).build();
        // todoRepository.save(todo);
        // });

        // @DynamicInsert 적용 후
        // ? 갯수가 줄었다
        // into
        // todotbl
        // (created_date, last_modified_date, title, todo_id)
        // values
        // (?, ?, ?, ?)
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Todo todo = Todo.builder().title("강아지 목욕 " + i).build();
            todoRepository.save(todo);
        });
    }

    // todo 전체 목록 조회
    @Test
    public void todoGetListTest() {
        todoRepository.findAll().forEach(todo -> System.out.println(todo));
    }

    // todo 완료 목록 조회 TodoRepository 에 메소드 작성
    @Test
    public void todoCompletedListTest() {
        todoRepository.findByCompleted(true).forEach(todo -> System.out.println(todo));
    }

    // todo 중요 목록 조회 TodoRepository 에 메소드 작성
    @Test
    public void todoImportantListTest() {
        todoRepository.findByImportant(true).forEach(todo -> System.out.println(todo));
    }

    // todo 1개 조회
    @Test
    public void todoGetRow() {
        Todo todo = todoRepository.findById(1L).get();
        System.out.println(todo);
    }

    // todo 수정
    // 제목 / 왼료 / 중요
    @Test
    public void todoUpdateTest() {
        Todo todo = todoRepository.findById(3L).get();
        todo.setTitle("강아지 수정");
        todo.setCompleted(true);
        todo.setImportant(true);
        todoRepository.save(todo);
    }

    // todo 삭제
    @Test
    public void todoDeleteTest() {
        Todo todo = todoRepository.findById(4L).get();
        todoRepository.delete(todo);
    }
}
