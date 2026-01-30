package com.example.todo.service;

import com.example.todo.dto.TodoRequest;
import com.example.todo.dto.TodoDTO;
import com.example.todo.exception.TodoNotFoundException;
import com.example.todo.mapper.TodoMapper;
import com.example.todo.model.Todo;
import com.example.todo.repository.TodoRepository;
import com.example.todo.repository.TodoStepRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private TodoStepRepository todoStepRepository;

    @Mock
    private TodoMapper todoMapper;

    @InjectMocks
    private TodoService todoService;

    private String userId;
    private String todoId;
    private Todo testTodo;
    private TodoDTO testTodoDTO;

    @BeforeEach
    void setUp() {
        userId = "user-123";
        todoId = "todo-456";
        
        testTodo = new Todo("Test Todo", userId);
        testTodo.setId(todoId);
        
        testTodoDTO = new TodoDTO(todoId, "Test Todo", false);
    }

    @Test
    void testCreateTodo_Success() {
        // Arrange
        TodoRequest request = new TodoRequest();
        request.setTitle("New Todo");
        
        when(todoRepository.existsByTitleIgnoreCaseAndUserId("New Todo", userId)).thenReturn(false);
        when(todoRepository.save(any(Todo.class))).thenReturn(testTodo);
        when(todoMapper.toResponse(testTodo)).thenReturn(testTodoDTO);

        // Act
        TodoDTO result = todoService.createTodo(request, userId);

        // Assert
        assertNotNull(result);
        assertEquals("Test Todo", result.title());
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    void testCreateTodo_DuplicateTitle_ThrowsException() {
        // Arrange
        TodoRequest request = new TodoRequest();
        request.setTitle("Existing Todo");
        
        when(todoRepository.existsByTitleIgnoreCaseAndUserId("Existing Todo", userId)).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
            todoService.createTodo(request, userId),
            "Should throw IllegalArgumentException for duplicate title"
        );
        verify(todoRepository, never()).save(any(Todo.class));
    }

    @Test
    void testToggleTodoDone_Success() {
        // Arrange
        when(todoRepository.findByIdAndUserId(todoId, userId)).thenReturn(Optional.of(testTodo));
        when(todoRepository.save(any(Todo.class))).thenReturn(testTodo);
        when(todoMapper.toResponse(testTodo)).thenReturn(testTodoDTO);

        // Act
        TodoDTO result = todoService.toggleTodoDone(todoId, userId);

        // Assert
        assertNotNull(result);
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    void testDeleteTodo_Success() {
        // Arrange
        when(todoRepository.existsByIdAndUserId(todoId, userId)).thenReturn(true);

        // Act
        todoService.deleteTodo(todoId, userId);

        // Assert
        verify(todoStepRepository, times(1)).deleteByTodoId(todoId);
        verify(todoRepository, times(1)).deleteByIdAndUserId(todoId, userId);
    }

    @Test
    void testDeleteTodo_NotFound_ThrowsException() {
        // Arrange
        when(todoRepository.existsByIdAndUserId(todoId, userId)).thenReturn(false);

        // Act & Assert
        assertThrows(TodoNotFoundException.class, () -> 
            todoService.deleteTodo(todoId, userId),
            "Should throw TodoNotFoundException when todo not found"
        );
        verify(todoRepository, never()).deleteByIdAndUserId(anyString(), anyString());
    }
}
