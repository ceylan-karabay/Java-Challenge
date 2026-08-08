package org.example.service;




public interface RetweetService {
    void retweet(Long tweetId, String username);
    void undoRetweet(Long tweetId, String username);
}
