package com.example.todo.controller;

import com.example.todo.dto.TodoRequest;
import com.example.todo.dto.TodoDTO;
import com.example.todo.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping
    public ResponseEntity<List<TodoDTO>> getAllTodos(Authentication auth) { 
        String userId = (String) auth.getPrincipal();
        List<TodoDTO> todos = todoService.getAllTodos(userId);
        return ResponseEntity.ok(todos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoDTO> getTodoById(@PathVariable String id, Authentication auth) {
        String userId = (String) auth.getPrincipal();
        TodoDTO todo = todoService.getTodoById(id, userId);
        return ResponseEntity.ok(todo);
    }

    @PostMapping
    public ResponseEntity<TodoDTO> createTodo(
            @Valid @RequestBody TodoRequest request, 
            Authentication auth) {
        String userId = (String) auth.getPrincipal();
        TodoDTO createdTodo = todoService.createTodo(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTodo);
    }

    @PutMapping("/{id}/toggle")
    public ResponseEntity<TodoDTO> toggleTodoDone(@PathVariable String id, Authentication auth) {
        String userId = (String) auth.getPrincipal();
        TodoDTO updatedTodo = todoService.toggleTodoDone(id, userId);
        return ResponseEntity.ok(updatedTodo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable String id, Authentication auth) {
        String userId = (String) auth.getPrincipal();
        todoService.deleteTodo(id, userId);
        return ResponseEntity.noContent().build();
    }
}