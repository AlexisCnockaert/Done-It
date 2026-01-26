package com.example.todo.service;

import com.example.todo.dto.TodoDTO;
import com.example.todo.service.TodoStepService;
import com.example.todo.dto.TodoRequest;
import com.example.todo.exception.TodoNotFoundException;
import com.example.todo.mapper.TodoMapper;
import com.example.todo.model.Todo;
import com.example.todo.repository.TodoRepository;
import com.example.todo.repository.TodoStepRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TodoService {

    private final TodoRepository todoRepository;
    private final TodoMapper todoMapper;
    private final TodoStepRepository todoStepRepository; 
    private final TodoStepService todoStepService;

    @Autowired
    public TodoService(
            TodoStepService todoStepService,
            TodoRepository todoRepository,
            TodoMapper todoMapper,
            TodoStepRepository todoStepRepository 
    ) {
        this.todoStepService = todoStepService;
        this.todoRepository = todoRepository;
        this.todoMapper = todoMapper;
        this.todoStepRepository = todoStepRepository; 
    }

    @Transactional(readOnly = true)
    public List<TodoDTO> getAllTodos() {
        List<Todo> todos = todoRepository.findAll();
        return todoMapper.toResponseList(todos);
    }

    @Transactional(readOnly = true)
    public TodoDTO getTodoById(String id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException("Todo not found with id: " + id));
        return todoMapper.toResponse(todo);
    }

    public TodoDTO createTodo(TodoRequest request) {
        if (todoRepository.existsByTitleIgnoreCase(request.getTitle().trim())) {
            throw new IllegalArgumentException("A todo with this title already exists");
        }
        Todo todo = new Todo(request.getTitle().trim());
        Todo savedTodo = todoRepository.save(todo);
        return todoMapper.toResponse(savedTodo);
    }

    public TodoDTO toggleTodoDone(String id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException("Todo not found with id: " + id));

        todo.setDone(!todo.isDone());
        Todo updatedTodo = todoRepository.save(todo);
        return todoMapper.toResponse(updatedTodo);
    }

    public void deleteTodo(String id) {
        if (!todoRepository.existsById(id)) {
            throw new TodoNotFoundException("Todo not found with id: " + id);
        }
        todoStepService.deleteStepsByTodoId(id);
        
        todoRepository.deleteById(id);
    }
}