package com.example.todo.service;

import com.example.todo.dto.AuthResponse;
import com.example.todo.dto.LoginRequest;
import com.example.todo.dto.RegisterRequest;
import com.example.todo.dto.UserDTO;
import com.example.todo.model.User;
import com.example.todo.repository.UserRepository;
import com.example.todo.repository.TodoRepository;
import com.example.todo.repository.TodoStepRepository;
import com.example.todo.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final TodoRepository todoRepository;
    private final TodoStepRepository todoStepRepository;

    @Autowired
    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider,
            TodoRepository todoRepository,
            TodoStepRepository todoStepRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.todoRepository = todoRepository;
        this.todoStepRepository = todoStepRepository;
    }

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword())
        );

        User savedUser = userRepository.save(user);

        String token = jwtTokenProvider.generateToken(
                savedUser.getId(),
                savedUser.getUsername()
        );

        return new AuthResponse(
                token,
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail()
        );
    }

    public AuthResponse login(LoginRequest request) {

        User user = userRepository
                .findByUsernameOrEmail(request.getUsernameOrEmail(), request.getUsernameOrEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String token = jwtTokenProvider.generateToken(user.getId(), user.getUsername());

        return new AuthResponse(
                token,
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }

    @Transactional(readOnly = true)
    public UserDTO getCurrentUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return new UserDTO(user.getId(), user.getUsername(), user.getEmail());
    }

    public AuthResponse loginOrCreateDemo(LoginRequest request) {
        String demoEmail = "demo@doneit.app";
        var existingDemoUser = userRepository.findByEmail(demoEmail);
        if (existingDemoUser.isPresent()) {
            String demoUserId = existingDemoUser.get().getId();
            var todos = todoRepository.findByUserId(demoUserId);
            for (var todo : todos) {
                todoStepRepository.deleteByTodoId(todo.getId());
            }
            todoRepository.deleteByUserId(demoUserId);
            userRepository.deleteAllByEmail(demoEmail);
        }
        
        User demoUser = createDemoUser(request);

        String token = jwtTokenProvider.generateToken(demoUser.getId(), demoUser.getUsername());
        
        return new AuthResponse(
            token,
            demoUser.getId(),
            demoUser.getUsername(),
            demoUser.getEmail()
        );
    }

    @Transactional(readOnly = true)
    public String getUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(User::getId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private User createDemoUser(LoginRequest request) {
        User user = new User(
                "demo",
                request.getUsernameOrEmail(),
                passwordEncoder.encode(request.getPassword())
        );
        return userRepository.save(user);
    }
}