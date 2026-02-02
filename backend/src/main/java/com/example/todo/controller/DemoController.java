package com.example.todo.controller;

import com.example.todo.dto.AuthResponse;
import com.example.todo.service.DemoDataService;
import com.example.todo.service.AuthService;
import com.example.todo.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/demo")
@CrossOrigin(origins = "*")
public class DemoController {

    private final AuthService authService;
    private final DemoDataService demoDataService;

    private static final String DEMO_EMAIL = "demo@doneit.app";
    private static final String DEMO_USERNAME = "demo";
    private static final String DEMO_PASSWORD = "demo123!secure";

    @Autowired
    public DemoController(AuthService authService, DemoDataService demoDataService) {
        this.authService = authService;
        this.demoDataService = demoDataService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> demoLogin() {
        LoginRequest demoRequest = new LoginRequest();
        demoRequest.setUsernameOrEmail(DEMO_EMAIL);
        demoRequest.setPassword(DEMO_PASSWORD);
        
        try {
            demoDataService.resetDemoData(DEMO_EMAIL);
            AuthResponse response = authService.loginOrCreateDemo(demoRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
