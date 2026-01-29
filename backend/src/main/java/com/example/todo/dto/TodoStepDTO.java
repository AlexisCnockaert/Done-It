package com.example.todo.dto;

import java.time.LocalDateTime;

public record TodoStepDTO(
        String id,
        String todoId,
        String description,
        int stepOrder,
        boolean completed,
        LocalDateTime createdAt
) {}