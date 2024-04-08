package com.example.todo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.todo.dto.TodoDto;
import com.example.todo.entity.Todo;
import com.example.todo.repository.TodoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor // @RequiredArgsConstructor + final == @Autowired
@Service
@Log4j2
public class TodoServiceImpl {

    private final TodoRepository todoRepository;

    public List<TodoDto> getList() {
        log.info("service getList() 호출");

        List<Todo> entity = todoRepository.findAll();
        // Todo => TodoDto 로 변환
        List<TodoDto> todoList = new ArrayList<>();
        entity.forEach(todo -> todoList.add(entityToDto(todo)));
        return todoList;
    }

    private TodoDto entityToDto(Todo entity) {
        return TodoDto.builder().id(entity.getId()).title(entity.getTitle()).createdDate(entity.getCreatedDate())
                .lastModifiedDate(entity.getLastModifiedDate()).completed(entity.getCompleted())
                .important(entity.getImportant()).build();
    }
}
