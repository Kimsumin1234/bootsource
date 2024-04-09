package com.example.todo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.todo.dto.TodoDto;
import com.example.todo.entity.Todo;
import com.example.todo.repository.TodoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor // @RequiredArgsConstructor + final == @Autowired
@Service
@Log4j2
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    @Override
    public List<TodoDto> getList() {
        log.info("service getList() 호출");

        // 미완료 목록만 보여주기
        List<Todo> list = todoRepository.findByCompleted(false);
        // Todo => TodoDto 로 변환
        // Todo 를 담기 위해서 TodoDto 박스를 만들고 값을 담는다
        // List<TodoDto> todoList = new ArrayList<>();
        // list.forEach(todo -> todoList.add(entityToDto(todo)));

        // stream 을 이용해서 변환
        List<TodoDto> todoList = list.stream().map(todo -> entityToDto(todo)).collect(Collectors.toList());
        return todoList;
    }

    @Override
    public TodoDto create(TodoDto dto) {
        // TodoDto => Todo(Entity, .save 로 DB에 저장을 해야해서)
        Todo entity = todoRepository.save(dtoToEntity(dto));

        return entityToDto(entity);
    }

    @Override
    public TodoDto getTodo(Long id) {
        Todo todo = todoRepository.findById(id).get();
        return entityToDto(todo);
    }

    @Override
    public List<TodoDto> getCompletedList() {
        // completed 가 true 인걸 찾기
        List<Todo> result = todoRepository.findByCompleted(true);

        // Todo => TodoDto 변환
        // List<TodoDto> compList = new ArrayList<>();
        // result.forEach(todo -> compList.add(entityToDto(todo)));

        // stream 을 이용해서 변환
        List<TodoDto> compList = result.stream().map(todo -> entityToDto(todo)).collect(Collectors.toList());

        return compList;
    }

    @Override
    public Long todoUpdate(Long id) {
        // 업데이트 완료후 id 만 리턴
        Todo todo = todoRepository.findById(id).get();
        todo.setCompleted(true);
        Todo result = todoRepository.save(todo);

        return result.getId();

    }

    @Override
    public void todoDelete(Long id) {
        todoRepository.deleteById(id);
    }

}
