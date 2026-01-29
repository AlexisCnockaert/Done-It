package com.example.todo.dto;

import java.util.List;

public record GeneratePlanDTO(
        String todoId,
        List<TodoStepDTO> steps,
        String message
) {
    public GeneratePlanDTO(String todoId, List<TodoStepDTO> steps) {
        this(todoId, steps, "Plan generated successfully");
    }
}