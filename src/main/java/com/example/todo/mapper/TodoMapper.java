package com.example.todo.mapper;

import com.example.todo.dto.TodoDTO;
import com.example.todo.model.Todo;
import org.springframework.stereotype.Component;

@Component
public class TodoMapper {

    public TodoDTO toResponse(Todo todo) {
        return new TodoDTO(
                todo.getId(),
                todo.getTitle(),
                todo.isDone()
        );
    }

    public java.util.List<TodoDTO> toResponseList(java.util.List<Todo> todos) {
        return todos.stream()
                .map(this::toResponse)
                .collect(java.util.stream.Collectors.toList());
    }
}
