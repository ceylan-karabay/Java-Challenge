package org.example.mapper;

import org.example.dto.tweet.TweetCreateRequest;
import org.example.dto.tweet.TweetResponse;
import org.example.dto.tweet.TweetUpdateRequest;
import org.example.dto.user.UserSummaryResponse;
import org.example.entity.Tweet;
import org.example.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TweetMapperTest {

    private TweetMapper tweetMapper;

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        tweetMapper = new TweetMapperImpl();

        ReflectionTestUtils.setField(tweetMapper, "userMapper", userMapper);
    }

    @Test
    @DisplayName("TweetCreateRequest -> Tweet Dönüşümü")
    void toEntity_Success() {
        TweetCreateRequest request = new TweetCreateRequest();
        request.setContent("Test tweet metni");

        Tweet tweet = tweetMapper.toEntity(request);

        assertNotNull(tweet);
        assertEquals("Test tweet metni", tweet.getContent());
    }

    @Test
    @DisplayName("Tweet -> TweetResponse Dönüşümü")
    void toResponse_Success() {
        User user = new User();
        user.setId(1L);
        user.setUsername("ceylan");

        Tweet tweet = new Tweet();
        tweet.setId(100L);
        tweet.setContent("Test tweet metni");
        tweet.setUser(user);

        UserSummaryResponse userSummaryResponse = new UserSummaryResponse();
        userSummaryResponse.setId(1L);
        userSummaryResponse.setUsername("ceylan");

        when(userMapper.toSummaryResponse(any(User.class))).thenReturn(userSummaryResponse);

        TweetResponse response = tweetMapper.toResponse(tweet);

        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals("Test tweet metni", response.getContent());
        assertNotNull(response.getAuthor());
        assertEquals("ceylan", response.getAuthor().getUsername());
    }

    @Test
    @DisplayName("Tweet List -> TweetResponse List Dönüşümü")
    void toResponseList_Success() {
        Tweet tweet = new Tweet();
        tweet.setId(100L);
        tweet.setContent("Test tweet metni");

        List<TweetResponse> responseList = tweetMapper.toResponseList(Collections.singletonList(tweet));

        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        assertEquals(100L, responseList.get(0).getId());
    }

    @Test
    @DisplayName("TweetUpdateRequest ile Var Olan Tweet Güncelleme")
    void updateEntityFromDto_Success() {
        Tweet tweet = new Tweet();
        tweet.setId(100L);
        tweet.setContent("Eski içerik");

        TweetUpdateRequest updateRequest = new TweetUpdateRequest();
        updateRequest.setContent("Yeni güncellenmiş içerik");

        tweetMapper.updateEntityFromDto(updateRequest, tweet);

        assertEquals(100L, tweet.getId());
        assertEquals("Yeni güncellenmiş içerik", tweet.getContent());
    }
}