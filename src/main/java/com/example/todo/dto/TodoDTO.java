package com.example.todo.dto;

public record TodoDTO (
        String id,
        String title,
        boolean done
) {
}