package org.example.service;



public interface LikeService {
    void likeTweet(Long tweetId, String username);
    void unlikeTweet(Long tweetId, String username);
}
