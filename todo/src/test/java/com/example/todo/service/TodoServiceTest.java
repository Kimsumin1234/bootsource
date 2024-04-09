package com.example.todo.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.todo.dto.TodoDto;

@SpringBootTest
public class TodoServiceTest {

    @Autowired // 여기서는 final 을 못써서 이방식을 사용
    private TodoService service;

    // Service <==> Repository 간에 데이터값 이동이 잘되나 확인
    // Service 가 잘 동작하는지 확인

    @Test
    public void serviceList() {
        System.out.println(service.getList());
    }

    @Test
    public void serviceCreate() {
        TodoDto dto = TodoDto.builder().title("서비스 create Test").important(true).build();
        System.out.println(service.create(dto));
    }

    @Test
    public void serviceRead() {
        System.out.println(service.getTodo(12L));
    }

    @Test
    public void serviceDelete() {

    }
}
