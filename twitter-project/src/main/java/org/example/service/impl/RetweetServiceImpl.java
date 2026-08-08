package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.entity.Retweet;
import org.example.entity.Tweet;
import org.example.entity.User;
import org.example.exception.BadRequestException;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.RetweetRepository;
import org.example.repository.TweetRepository;
import org.example.repository.UserRepository;
import org.example.service.AuthService;
import org.example.service.RetweetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RetweetServiceImpl implements RetweetService {

    private final RetweetRepository retweetRepository;
    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;
    private final AuthService authService;

    @Override
    @Transactional
    public void retweet(Long tweetId, String username) {
        Long userId = authService.getUserIdByUsername(username);

        if (!tweetRepository.existsById(tweetId)) {
            throw new ResourceNotFoundException("Tweet bulunamadı: " + tweetId);
        }

        if (retweetRepository.existsByUserIdAndTweetId(userId, tweetId)) {
            throw new BadRequestException("Bu tweet zaten retweet edilmiş.");
        }

        User userRef = userRepository.getReferenceById(userId);
        Tweet tweetRef = tweetRepository.getReferenceById(tweetId);

        Retweet retweet = Retweet.builder()
                .user(userRef)
                .tweet(tweetRef)
                .build();

        retweetRepository.save(retweet);
    }

    @Override
    @Transactional
    public void undoRetweet(Long tweetId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı: " + username));

        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new ResourceNotFoundException("Tweet bulunamadı: " + tweetId));


        retweetRepository.findByUserIdAndTweetId(user.getId(), tweetId)
                .ifPresent(retweetRepository::delete);


        if (tweet.getRetweetCount() > 0) {
            tweet.setRetweetCount(tweet.getRetweetCount() - 1);
            tweetRepository.save(tweet);
        }
    }
}