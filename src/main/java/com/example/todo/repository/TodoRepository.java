package com.example.todo.repository;

import com.example.todo.model.Todo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends MongoRepository<Todo, String> {
    
    List<Todo> findByUserId(String userId);
    
    Optional<Todo> findByIdAndUserId(String id, String userId);
    
    boolean existsByTitleIgnoreCaseAndUserId(String title, String userId);
    
    boolean existsByIdAndUserId(String id, String userId);
    
    void deleteByIdAndUserId(String id, String userId);
}