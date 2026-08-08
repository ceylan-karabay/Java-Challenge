package org.example.service;

import org.example.dto.auth.RegisterRequest;
import org.example.entity.User;
import org.example.exception.BadRequestException;
import org.example.repository.UserRepository;
import org.example.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    @DisplayName("register - Başarılı Kullanıcı Kaydı")
    void register_Success() {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setUsername("yeniKullanici");
        request.setEmail("yeni@mail.com");
        request.setPassword("123456");

        when(userRepository.existsByUsername("yeniKullanici")).thenReturn(false);
        when(userRepository.existsByEmail("yeni@mail.com")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("hashedPassword");


        String result = authService.register(request);


        assertEquals("Kullanıcı başarıyla kaydedildi.", result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("register - Kullanıcı Adı Zaten Varsa BadRequestException Fırlatmalı")
    void register_UsernameExists_ThrowsBadRequestException() {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("mevcutKullanici");

        when(userRepository.existsByUsername("mevcutKullanici")).thenReturn(true);


        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> authService.register(request)
        );

        assertEquals("Bu kullanıcı adı daha önce alınmış.", exception.getMessage());
        verify(userRepository, never()).save(any());
    }
}