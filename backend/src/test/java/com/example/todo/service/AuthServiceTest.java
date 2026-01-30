package com.example.todo.service;

import com.example.todo.dto.AuthResponse;
import com.example.todo.dto.LoginRequest;
import com.example.todo.dto.RegisterRequest;
import com.example.todo.model.User;
import com.example.todo.repository.UserRepository;
import com.example.todo.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User testUser;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");

        loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail("testuser");
        loginRequest.setPassword("password123");

        testUser = new User("testuser", "test@example.com", "encodedPassword");
        testUser.setId("user-123");
    }

    @Test
    void testRegister_Success() {
        // Arrange
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtTokenProvider.generateToken("user-123", "testuser")).thenReturn("jwt-token");

        // Act
        AuthResponse response = authService.register(registerRequest);

        // Assert
        assertNotNull(response);
        assertEquals("testuser", response.username());
        assertEquals("test@example.com", response.email());
        assertEquals("jwt-token", response.token());
    }

    @Test
    void testRegister_DuplicateUsername_ThrowsException() {
        // Arrange
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authService.register(registerRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLogin_Success() {
        // Arrange
        when(userRepository.findByUsernameOrEmail("testuser", "testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtTokenProvider.generateToken("user-123", "testuser")).thenReturn("jwt-token");

        // Act
        AuthResponse response = authService.login(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("testuser", response.username());
        assertEquals("jwt-token", response.token());
    }

    @Test
    void testLogin_InvalidPassword_ThrowsException() {
        // Arrange
        when(userRepository.findByUsernameOrEmail("testuser", "testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authService.login(loginRequest));
        verify(jwtTokenProvider, never()).generateToken(anyString(), anyString());
    }

    @Test
    void testLogin_UserNotFound_ThrowsException() {
        // Arrange
        when(userRepository.findByUsernameOrEmail("testuser", "testuser")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authService.login(loginRequest));
    }
}
