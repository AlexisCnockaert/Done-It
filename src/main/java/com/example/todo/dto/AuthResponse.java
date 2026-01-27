package com.example.todo.dto;

public record AuthResponse(
        String token,
        String userId,
        String username,
        String email
) {}