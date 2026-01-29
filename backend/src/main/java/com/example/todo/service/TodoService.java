package com.example.todo.service;

import com.example.todo.dto.TodoRequest;
import com.example.todo.dto.TodoDTO;
import com.example.todo.exception.TodoNotFoundException;
import com.example.todo.mapper.TodoMapper;
import com.example.todo.model.Todo;
import com.example.todo.repository.TodoRepository;
import com.example.todo.repository.TodoStepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TodoService {

    private final TodoRepository todoRepository;
    private final TodoMapper todoMapper;
    private final TodoStepRepository todoStepRepository;

    @Autowired
    public TodoService(
            TodoRepository todoRepository,
            TodoMapper todoMapper,
            TodoStepRepository todoStepRepository) {
        this.todoRepository = todoRepository;
        this.todoMapper = todoMapper;
        this.todoStepRepository = todoStepRepository;
    }

    @Transactional(readOnly = true)
    public List<TodoDTO> getAllTodos(String userId) {  
        List<Todo> todos = todoRepository.findByUserId(userId);  
        return todoMapper.toResponseList(todos);
    }

    @Transactional(readOnly = true)
    public TodoDTO getTodoById(String id, String userId) {  
        Todo todo = todoRepository.findByIdAndUserId(id, userId)  
                .orElseThrow(() -> new TodoNotFoundException("Todo not found"));
        return todoMapper.toResponse(todo);
    }

    public TodoDTO createTodo(TodoRequest request, String userId) {  
        String title = request.getTitle().trim();
        
        if (todoRepository.existsByTitleIgnoreCaseAndUserId(title, userId)) {  
            throw new IllegalArgumentException("You already have a todo with this title");
        }

        Todo todo = new Todo(title, userId);  
        Todo savedTodo = todoRepository.save(todo);
        return todoMapper.toResponse(savedTodo);
    }

    public TodoDTO toggleTodoDone(String id, String userId) { 
        Todo todo = todoRepository.findByIdAndUserId(id, userId)  
                .orElseThrow(() -> new TodoNotFoundException("Todo not found"));
        
        todo.setDone(!todo.isDone());
        Todo updatedTodo = todoRepository.save(todo);
        return todoMapper.toResponse(updatedTodo);
    }

    public void deleteTodo(String id, String userId) {  
        if (!todoRepository.existsByIdAndUserId(id, userId)) { 
            throw new TodoNotFoundException("Todo not found");
        }
        
        todoStepRepository.deleteByTodoId(id);
        todoRepository.deleteByIdAndUserId(id, userId);  
    }
}