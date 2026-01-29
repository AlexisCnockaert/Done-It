package com.example.todo.mapper;

import com.example.todo.dto.TodoStepDTO;
import com.example.todo.model.TodoStep;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TodoStepMapper {

    public TodoStepDTO toResponse(TodoStep step) {
        if (step == null) {
            return null;
        }
        
        return new TodoStepDTO(
                step.getId(),
                step.getTodoId(),
                step.getDescription(),
                step.getStepOrder(),
                step.isCompleted(),
                step.getCreatedAt()
        );
    }

    public List<TodoStepDTO> toResponseList(List<TodoStep> steps) {
        return steps.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}