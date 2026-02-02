package com.example.todo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "todos")
public class Todo {
    
    @Id
    private String id;
    
    private String title;
    
    private String description;
    
    private boolean done;
    
    @Indexed  
    private String userId; 
    
    private LocalDateTime createdAt;

    public Todo() {
        this.done = false;
        this.createdAt = LocalDateTime.now();
    }

    public Todo(String title) {
        this();
        this.title = title;
    }

    public Todo(String title, String userId) {
        this();
        this.title = title;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
