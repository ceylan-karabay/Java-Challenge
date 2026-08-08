package org.example.service;

import org.example.entity.Follow;
import org.example.entity.User;
import org.example.exception.BadRequestException;
import org.example.repository.FollowRepository;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.example.service.impl.FollowServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowServiceImplTest {

    @Mock private FollowRepository followRepository;
    @Mock private UserRepository userRepository;
    @Mock private AuthService authService;

    @InjectMocks private FollowServiceImpl followService;

    private User follower;
    private User followed;

    @BeforeEach
    void setUp() {
        follower = new User();
        follower.setId(1L);
        follower.setUsername("takipEden");

        followed = new User();
        followed.setId(2L);
        followed.setUsername("takipEdilen");
    }

    @Test
    @DisplayName("followUser - Kendini Takip Etmeye Çalıştığında BadRequestException Fırlatmalı")
    void followUser_SelfFollow_ThrowsBadRequestException() {

        when(userRepository.findByUsername("takipEden")).thenReturn(Optional.of(follower));


        assertThrows(
                BadRequestException.class,
                () -> followService.followUser(1L, "takipEden")
        );
        verify(followRepository, never()).save(any());
    }

    @Test
    void unfollowUser_Success() {

        Long targetUserId = 2L;
        String currentUsername = "takipEden";

        User follower = new User();
        follower.setId(1L);
        follower.setUsername(currentUsername);


        when(userRepository.findByUsername(currentUsername))
                .thenReturn(Optional.of(follower));


        when(followRepository.existsByFollowerIdAndFollowingId(follower.getId(), targetUserId))
                .thenReturn(true);


        assertDoesNotThrow(() -> followService.unfollowUser(targetUserId, currentUsername));


        verify(followRepository).deleteByFollowerIdAndFollowingId(follower.getId(), targetUserId);
    }
}