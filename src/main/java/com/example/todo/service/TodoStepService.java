package com.example.todo.service;

import com.example.todo.dto.AIPromptRequest;
import com.example.todo.dto.TodoStepDTO;
import com.example.todo.exception.TodoNotFoundException;
import com.example.todo.mapper.TodoStepMapper;
import com.example.todo.model.TodoStep;
import com.example.todo.repository.TodoRepository;
import com.example.todo.repository.TodoStepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TodoStepService {

    private final TodoStepRepository todoStepRepository;
    private final TodoRepository todoRepository;  
    private final TodoStepMapper todoStepMapper;
    private final AIService aiService;

    @Autowired
    public TodoStepService(
            TodoStepRepository todoStepRepository,
            TodoRepository todoRepository,
            TodoStepMapper todoStepMapper,
            AIService aiService
    ) {
        this.todoStepRepository = todoStepRepository;
        this.todoRepository = todoRepository;
        this.todoStepMapper = todoStepMapper;
        this.aiService = aiService;
    }

    public List<TodoStepDTO> generateAIPlan(AIPromptRequest request) {
        if (!todoRepository.existsById(request.getTodoId())) {
            throw new TodoNotFoundException("Todo not found with id: " + request.getTodoId());
        }
        deleteStepsByTodoId(request.getTodoId());
        List<String> generatedSteps = aiService.generateSteps(
                request.getTodoTitle(),
                request.getUserContext()
        );
        List<TodoStep> steps = new ArrayList<>();
        for (int i = 0; i < generatedSteps.size(); i++) {
            TodoStep step = new TodoStep(
                    request.getTodoId(),
                    generatedSteps.get(i),
                    i + 1  
            );
            steps.add(step);
        }

        List<TodoStep> savedSteps = todoStepRepository.saveAll(steps);
        return todoStepMapper.toResponseList(savedSteps);
    }

    @Transactional(readOnly = true)
    public List<TodoStepDTO> getStepsByTodoId(String todoId) {
        if (!todoRepository.existsById(todoId)) {
            throw new TodoNotFoundException("Todo not found with id: " + todoId);
        }

        List<TodoStep> steps = todoStepRepository.findByTodoIdOrderByStepOrderAsc(todoId);
        return todoStepMapper.toResponseList(steps);
    }

    public TodoStepDTO toggleStep(String stepId) {
        TodoStep step = todoStepRepository.findById(stepId)
                .orElseThrow(() -> new TodoNotFoundException("Step not found with id: " + stepId));

        step.setCompleted(!step.isCompleted());
        TodoStep updatedStep = todoStepRepository.save(step);
        return todoStepMapper.toResponse(updatedStep);
    }

    public void deleteStep(String stepId) {
        if (!todoStepRepository.existsById(stepId)) {
            throw new TodoNotFoundException("Step not found with id: " + stepId);
        }
        todoStepRepository.deleteById(stepId);
    }


    public void deleteStepsByTodoId(String todoId) {
        todoStepRepository.deleteByTodoId(todoId);
    }

    @Transactional(readOnly = true)
    public boolean hasSteps(String todoId) {
        return todoStepRepository.existsByTodoId(todoId);
    }

    @Transactional(readOnly = true)
    public long countSteps(String todoId) {
        return todoStepRepository.countByTodoId(todoId);
    }
}