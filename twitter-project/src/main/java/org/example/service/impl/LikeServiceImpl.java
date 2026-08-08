package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.entity.Like;
import org.example.entity.Tweet;
import org.example.entity.User;
import org.example.exception.BadRequestException;
import org.example.repository.LikeRepository;
import org.example.repository.TweetRepository;
import org.example.repository.UserRepository;
import org.example.service.AuthService;
import org.example.service.LikeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    @Override
    @Transactional
    public void likeTweet(Long tweetId, String username) {
        Long userId = authService.getUserIdByUsername(username);

        if (!tweetRepository.existsById(tweetId)) {
            throw new BadRequestException("Tweet bulunamadı: " + tweetId);
        }

        if (likeRepository.existsByUserIdAndTweetId(userId, tweetId)) {
            throw new BadRequestException("Bu tweet zaten beğenilmiş.");
        }


        User user = userRepository.getReferenceById(userId);
        Tweet tweet = tweetRepository.getReferenceById(tweetId);

        Like like = Like.builder()
                .user(user)
                .tweet(tweet)
                .build();

        likeRepository.save(like);
    }

    @Override
    @Transactional
    public void unlikeTweet(Long tweetId, String username) {
        Long userId = authService.getUserIdByUsername(username);

        if (!likeRepository.existsByUserIdAndTweetId(userId, tweetId)) {
            throw new BadRequestException("Bu tweet henüz beğenilmemiş.");
        }


        likeRepository.deleteByUserIdAndTweetId(userId, tweetId);
    }
}