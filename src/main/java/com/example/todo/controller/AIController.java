package com.example.todo.controller;

import com.example.todo.dto.AIPromptRequest;
import com.example.todo.dto.GeneratePlanDTO;
import com.example.todo.dto.TodoStepDTO;
import com.example.todo.service.TodoStepService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    @Autowired
    private TodoStepService todoStepService;

    @PostMapping("/generate-plan")
    public ResponseEntity<GeneratePlanDTO> generatePlan(
            @Valid @RequestBody AIPromptRequest request) {
        
        List<TodoStepDTO> steps = todoStepService.generateAIPlan(request);
        
        GeneratePlanDTO response = new GeneratePlanDTO(
                request.getTodoId(),
                steps
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/todos/{todoId}/steps")
    public ResponseEntity<List<TodoStepDTO>> getTodoSteps(@PathVariable String todoId) {
        List<TodoStepDTO> steps = todoStepService.getStepsByTodoId(todoId);
        return ResponseEntity.ok(steps);
    }


    @PutMapping("/steps/{stepId}/toggle")
    public ResponseEntity<TodoStepDTO> toggleStep(@PathVariable String stepId) {
        TodoStepDTO step = todoStepService.toggleStep(stepId);
        return ResponseEntity.ok(step);
    }

    @DeleteMapping("/steps/{stepId}")
    public ResponseEntity<Void> deleteStep(@PathVariable String stepId) {
        todoStepService.deleteStep(stepId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/todos/{todoId}/has-steps")
    public ResponseEntity<Boolean> hasSteps(@PathVariable String todoId) {
        boolean hasSteps = todoStepService.hasSteps(todoId);
        return ResponseEntity.ok(hasSteps);
    }
}