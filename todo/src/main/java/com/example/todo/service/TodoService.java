package com.example.todo.service;

import java.util.List;

import com.example.todo.dto.TodoDto;
import com.example.todo.entity.Todo;

public interface TodoService {

    List<TodoDto> getList();

    TodoDto create(TodoDto dto);

    TodoDto getTodo(Long id);

    List<TodoDto> getCompletedList();

    Long todoUpdate(Long id);

    void todoDelete(Long id);

    // interface 에서 원래는 추상 메소드만 가능하나
    // 구현 메소드를 내릴려면 private -> public default 로 변경하면된다
    public default TodoDto entityToDto(Todo entity) {
        return TodoDto.builder().id(entity.getId()).title(entity.getTitle()).createdDate(entity.getCreatedDate())
                .lastModifiedDate(entity.getLastModifiedDate()).completed(entity.getCompleted())
                .important(entity.getImportant()).build();
    }

    public default Todo dtoToEntity(TodoDto dto) {
        return Todo.builder().id(dto.getId()).title(dto.getTitle())
                .completed(dto.getCompleted())
                .important(dto.getImportant()).build();
    }

}