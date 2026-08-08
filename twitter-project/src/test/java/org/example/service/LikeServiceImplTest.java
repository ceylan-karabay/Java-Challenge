package org.example.service;

import org.example.entity.Like;
import org.example.entity.Tweet;
import org.example.entity.User;
import org.example.exception.BadRequestException;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.LikeRepository;
import org.example.repository.TweetRepository;
import org.example.repository.UserRepository;
import org.example.service.impl.LikeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeServiceImplTest {

    @Mock private LikeRepository likeRepository;
    @Mock private UserRepository userRepository;
    @Mock private TweetRepository tweetRepository;
    @Mock private AuthService authService;

    @InjectMocks private LikeServiceImpl likeService;

    private User mockUser;
    private Tweet mockTweet;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");

        mockTweet = new Tweet();
        mockTweet.setId(100L);
    }

    @Test
    @DisplayName("likeTweet - Başarılı Beğenme İşlemi")
    void likeTweet_Success() {

        when(authService.getUserIdByUsername("testuser")).thenReturn(1L);
        when(tweetRepository.existsById(100L)).thenReturn(true);
        when(likeRepository.existsByUserIdAndTweetId(1L, 100L)).thenReturn(false);
        when(userRepository.getReferenceById(1L)).thenReturn(mockUser);
        when(tweetRepository.getReferenceById(100L)).thenReturn(mockTweet);

        assertDoesNotThrow(() -> likeService.likeTweet(100L, "testuser"));
        verify(likeRepository, times(1)).save(any(Like.class));
    }

    @Test
    @DisplayName("likeTweet - Tweet Zaten Beğenilmişse BadRequestException Fırlatmalı")
    void likeTweet_AlreadyLiked_ThrowsBadRequestException() {

        when(authService.getUserIdByUsername("testuser")).thenReturn(1L);
        when(tweetRepository.existsById(100L)).thenReturn(true);
        when(likeRepository.existsByUserIdAndTweetId(1L, 100L)).thenReturn(true);


        assertThrows(BadRequestException.class, () -> likeService.likeTweet(100L, "testuser"));
        verify(likeRepository, never()).save(any());
    }

    @Test
    void unlikeTweet_Success() {

        Long tweetId = 1L;
        String username = "testuser";
        Long userId = 10L;


        when(authService.getUserIdByUsername(username)).thenReturn(userId);

        when(likeRepository.existsByUserIdAndTweetId(userId, tweetId)).thenReturn(true);


        assertDoesNotThrow(() -> likeService.unlikeTweet(tweetId, username));

        verify(likeRepository).deleteByUserIdAndTweetId(userId, tweetId);
    }
}