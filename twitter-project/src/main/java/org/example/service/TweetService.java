package org.example.service;

import org.example.dto.common.PagedResponse;
import org.example.dto.tweet.TweetCreateRequest;
import org.example.dto.tweet.TweetResponse;
import org.example.dto.tweet.TweetUpdateRequest;

import java.util.List;

public interface TweetService {
    TweetResponse createTweet(
            TweetCreateRequest request,
            String currentUsername
    );

    TweetResponse getTweetById(
            Long id,
            Long currentUserId
    );

    PagedResponse<TweetResponse> getAllTweets(
            int page,
            int size,
            Long currentUserId
    );

    PagedResponse<TweetResponse> getTweetsByUserId(
            Long userId,
            int page,
            int size,
            Long currentUserId
    );

    TweetResponse updateTweet(
            Long id,
            TweetUpdateRequest request,
            Long currentUserId
    );

    void deleteTweet(
            Long id,
            Long currentUserId
    );

    TweetResponse likeTweet(
            Long tweetId,
            String currentUsername
    );

    TweetResponse unlikeTweet(
            Long tweetId,
            String currentUsername
    );

    TweetResponse retweet(
            Long tweetId,
            String currentUsername
    );

    PagedResponse<TweetResponse> getFeed(
            int page,
            int size,
            String currentUsername
    );
}
