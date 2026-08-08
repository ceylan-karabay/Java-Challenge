package org.example.service;

import org.example.entity.Retweet;
import org.example.entity.Tweet;
import org.example.entity.User;
import org.example.repository.RetweetRepository;
import org.example.repository.TweetRepository;
import org.example.repository.UserRepository;
import org.example.service.impl.RetweetServiceImpl;
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
class RetweetServiceImplTest {

    @Mock
    private RetweetRepository retweetRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TweetRepository tweetRepository;
    @Mock
    private AuthService authService;

    @InjectMocks
    private RetweetServiceImpl retweetService;

    private User mockUser;
    private Tweet mockTweet;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("ceylan");

        mockTweet = new Tweet();
        mockTweet.setId(10L);
    }

    @Test
    @DisplayName("retweet - Başarılı Retweet Yapma")
    void retweet_Success() {
        // Given
        when(authService.getUserIdByUsername("ceylan")).thenReturn(1L);
        when(tweetRepository.existsById(10L)).thenReturn(true);
        when(retweetRepository.existsByUserIdAndTweetId(1L, 10L)).thenReturn(false);
        when(userRepository.getReferenceById(1L)).thenReturn(mockUser);
        when(tweetRepository.getReferenceById(10L)).thenReturn(mockTweet);


        assertDoesNotThrow(() -> retweetService.retweet(10L, "ceylan"));
        verify(retweetRepository, times(1)).save(any(Retweet.class));
    }

    @Test
    @DisplayName("undoRetweet - Başarılı Retweet Geri Alma")
    void undoRetweet_Success() {

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("ceylan");

        Tweet mockTweet = new Tweet();
        mockTweet.setId(10L);
        mockTweet.setRetweetCount(5);

        Retweet mockRetweet = new Retweet();
        mockRetweet.setId(100L);
        mockRetweet.setUser(mockUser);
        mockRetweet.setTweet(mockTweet);


        when(userRepository.findByUsername("ceylan")).thenReturn(Optional.of(mockUser));
        when(tweetRepository.findById(10L)).thenReturn(Optional.of(mockTweet));
        when(retweetRepository.findByUserIdAndTweetId(1L, 10L)).thenReturn(Optional.of(mockRetweet));

        assertDoesNotThrow(() -> retweetService.undoRetweet(10L, "ceylan"));


        verify(retweetRepository, times(1)).delete(mockRetweet);
        verify(tweetRepository, times(1)).save(mockTweet);


        org.junit.jupiter.api.Assertions.assertEquals(4, mockTweet.getRetweetCount());
    }
}