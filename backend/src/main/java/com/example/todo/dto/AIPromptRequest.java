package com.example.todo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AIPromptRequest {
    
    @NotBlank(message = "Todo ID is required")
    private String todoId;
    
    @NotBlank(message = "Todo title is required")
    @Size(max = 100, message = "Title too long")
    private String todoTitle;
    
    @Size(max = 500, message = "Context too long (max 500 characters)")
    private String userContext; 

    public AIPromptRequest() {
    }

    public AIPromptRequest(String todoId, String todoTitle, String userContext) {
        this.todoId = todoId;
        this.todoTitle = todoTitle;
        this.userContext = userContext;
    }

    public String getTodoId() {
        return todoId;
    }

    public void setTodoId(String todoId) {
        this.todoId = todoId;
    }

    public String getTodoTitle() {
        return todoTitle;
    }

    public void setTodoTitle(String todoTitle) {
        this.todoTitle = todoTitle;
    }

    public String getUserContext() {
        return userContext;
    }

    public void setUserContext(String userContext) {
        this.userContext = userContext;
    }
}