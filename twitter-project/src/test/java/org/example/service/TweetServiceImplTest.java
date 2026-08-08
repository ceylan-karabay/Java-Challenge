package org.example.service;

import org.example.dto.tweet.TweetCreateRequest;
import org.example.dto.tweet.TweetResponse;
import org.example.entity.Tweet;
import org.example.entity.User;
import org.example.mapper.TweetMapper;
import org.example.repository.*;
import org.example.service.impl.TweetServiceImpl;
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
class TweetServiceImplTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private TweetRepository tweetRepository;

    @Mock private TweetMapper tweetMapper;
    @Mock
    private LikeRepository likeRepository;

    @Mock
    private RetweetRepository retweetRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TweetServiceImpl tweetService;

    private User mockUser;
    private Tweet mockTweet;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("ceylan");

        mockTweet = new Tweet();
        mockTweet.setId(10L);
        mockTweet.setContent("Merhaba dünya!");
        mockTweet.setUser(mockUser);
    }

    @Test
    @DisplayName("createTweet - Başarılı Tweet Oluşturma")
    void createTweet_Success() {
        // Given
        TweetCreateRequest request = new TweetCreateRequest();
        request.setContent("Merhaba dünya!");

        TweetResponse expectedResponse = new TweetResponse();
        expectedResponse.setContent("Merhaba dünya!");

        when(userRepository.findByUsername("ceylan")).thenReturn(Optional.of(mockUser));
        when(tweetMapper.toEntity(request)).thenReturn(mockTweet); // Mapper eklendi
        when(tweetRepository.save(any(Tweet.class))).thenReturn(mockTweet);
        when(tweetMapper.toResponse(mockTweet)).thenReturn(expectedResponse); // Mapper eklendi


        TweetResponse response = tweetService.createTweet(request, "ceylan");

        assertNotNull(response);
        assertEquals("Merhaba dünya!", response.getContent());
        verify(tweetRepository, times(1)).save(any(Tweet.class));
    }

    @Test
    @DisplayName("deleteTweet - Başarılı Silme")
    void deleteTweet_Success() {

        when(tweetRepository.findById(10L)).thenReturn(Optional.of(mockTweet));


        assertDoesNotThrow(() -> tweetService.deleteTweet(10L, 1L));


        verify(tweetRepository, times(1)).delete(mockTweet);
    }

    @Test
    @DisplayName("deleteTweet - Başkasına Ait Tweet Silinmeye Çalışıldığında Exception Fırlatmalı")
    void deleteTweet_UnauthorizedUser_ThrowsBadRequestException() {

        when(tweetRepository.findById(10L)).thenReturn(Optional.of(mockTweet));


        assertThrows(
                RuntimeException.class,
                () -> tweetService.deleteTweet(10L, 99L)
        );
        verify(tweetRepository, never()).delete(any());
    }
}
