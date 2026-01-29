package com.example.todo.repository;

import com.example.todo.model.TodoStep;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoStepRepository extends MongoRepository<TodoStep, String> {
    
    List<TodoStep> findByTodoIdOrderByStepOrderAsc(String todoId);
    long countByTodoId(String todoId);
    boolean existsByTodoId(String todoId);
    void deleteByTodoId(String todoId);
}