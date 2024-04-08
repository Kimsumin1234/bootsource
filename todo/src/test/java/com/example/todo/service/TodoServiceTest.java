package com.example.todo.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TodoServiceTest {

    @Autowired // 여기서는 final 을 못써서 이방식을 사용
    private TodoServiceImpl service;

    // Service <==> Repository 간에 데이터값 이동이 잘되나 확인
    // Service 가 잘 동작하는지 확인

    @Test
    public void serviceList() {
        System.out.println(service.getList());
    }
}
