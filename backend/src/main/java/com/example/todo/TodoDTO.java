package com.example.todo;

public record TodoDTO (
        String id,
        String title,
        boolean done
) {
}