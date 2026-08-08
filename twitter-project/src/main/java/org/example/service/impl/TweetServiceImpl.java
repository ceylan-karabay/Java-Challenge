package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.common.PagedResponse;
import org.example.dto.tweet.TweetCreateRequest;
import org.example.dto.tweet.TweetResponse;
import org.example.dto.tweet.TweetUpdateRequest;
import org.example.entity.Like;
import org.example.entity.Retweet;
import org.example.entity.Tweet;
import org.example.entity.User;
import org.example.exception.ResourceNotFoundException;
import org.example.exception.UnauthorizedException;
import org.example.mapper.TweetMapper;
import org.example.repository.*;
import org.example.service.TweetService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final RetweetRepository retweetRepository;
    private final FollowRepository followRepository;
    private final TweetMapper tweetMapper;
    private final CommentRepository commentRepository;

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<TweetResponse> getFeed(
            int page,
            int size,
            String currentUsername) {

        User currentUser = getUserByUsername(currentUsername);

        List<Long> targetUserIds =
                new ArrayList<>(
                        followRepository.findFollowingIdsByFollowerId(
                                currentUser.getId()
                        )
                );

        targetUserIds.add(currentUser.getId());

        Pageable pageable = PageRequest.of(page, size);

        Page<Tweet> tweetPage =
                tweetRepository.findByUserIdInOrderByCreatedAtDesc(
                        targetUserIds,
                        pageable
                );

        List<TweetResponse> content = tweetPage.getContent()
                .stream()
                .map(tweet -> {

                    TweetResponse response =
                            tweetMapper.toResponse(tweet);

                    response.setLikedByCurrentUser(
                            likeRepository.existsByUserIdAndTweetId(
                                    currentUser.getId(),
                                    tweet.getId()
                            )
                    );

                    response.setRetweetedByCurrentUser(
                            retweetRepository.existsByUserIdAndTweetId(
                                    currentUser.getId(),
                                    tweet.getId()
                            )
                    );

                    return response;
                })
                .toList();

        return new PagedResponse<>(
                content,
                tweetPage.getNumber(),
                tweetPage.getSize(),
                tweetPage.getTotalElements(),
                tweetPage.getTotalPages(),
                tweetPage.isLast()
        );
    }

    @Override
    @Transactional
    public TweetResponse createTweet(TweetCreateRequest request, String currentUsername) {
        User user = getUserByUsername(currentUsername);

        Tweet tweet = tweetMapper.toEntity(request);
        tweet.setUser(user);

        Tweet savedTweet = tweetRepository.save(tweet);
        return tweetMapper.toResponse(savedTweet);
    }

    @Override
    @Transactional(readOnly = true)
    public TweetResponse getTweetById(Long id, Long currentUserId) {

        Tweet tweet = getTweetEntityById(id);

        TweetResponse response = tweetMapper.toResponse(tweet);

        if (currentUserId != null) {
            response.setLikedByCurrentUser(
                    likeRepository.existsByUserIdAndTweetId(
                            currentUserId,
                            id
                    )
            );

            response.setRetweetedByCurrentUser(
                    retweetRepository.existsByUserIdAndTweetId(
                            currentUserId,
                            id
                    )
            );
        }

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<TweetResponse> getAllTweets(
            int page,
            int size,
            Long currentUserId) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by("createdAt").descending()
                );

        Page<Tweet> tweetPage =
                tweetRepository.findAll(pageable);

        List<TweetResponse> content = tweetPage.getContent()
                .stream()
                .map(tweet -> {

                    TweetResponse response =
                            tweetMapper.toResponse(tweet);

                    if (currentUserId != null) {

                        response.setLikedByCurrentUser(
                                likeRepository.existsByUserIdAndTweetId(
                                        currentUserId,
                                        tweet.getId()
                                )
                        );

                        response.setRetweetedByCurrentUser(
                                retweetRepository.existsByUserIdAndTweetId(
                                        currentUserId,
                                        tweet.getId()
                                )
                        );
                    }

                    return response;
                })
                .toList();

        return new PagedResponse<>(
                content,
                tweetPage.getNumber(),
                tweetPage.getSize(),
                tweetPage.getTotalElements(),
                tweetPage.getTotalPages(),
                tweetPage.isLast()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<TweetResponse> getTweetsByUserId(
            Long userId,
            int page,
            int size,
            Long currentUserId) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Tweet> tweetPage =
                tweetRepository.findByUserIdOrderByCreatedAtDesc(
                        userId,
                        pageable
                );

        List<TweetResponse> content = tweetPage.getContent()
                .stream()
                .map(tweet -> {

                    TweetResponse response =
                            tweetMapper.toResponse(tweet);

                    if (currentUserId != null) {

                        response.setLikedByCurrentUser(
                                likeRepository.existsByUserIdAndTweetId(
                                        currentUserId,
                                        tweet.getId()
                                )
                        );

                        response.setRetweetedByCurrentUser(
                                retweetRepository.existsByUserIdAndTweetId(
                                        currentUserId,
                                        tweet.getId()
                                )
                        );
                    }

                    return response;
                })
                .toList();

        return new PagedResponse<>(
                content,
                tweetPage.getNumber(),
                tweetPage.getSize(),
                tweetPage.getTotalElements(),
                tweetPage.getTotalPages(),
                tweetPage.isLast()
        );
    }

    @Override
    @Transactional
    public TweetResponse updateTweet(Long id, TweetUpdateRequest request, Long currentUserId) {
        Tweet tweet = getTweetEntityById(id);

        if (!tweet.getUser().getId().equals(currentUserId)) {
            throw new UnauthorizedException("Bu tweeti güncelleme yetkiniz yok.");
        }

        tweet.setContent(request.getContent());
        Tweet updatedTweet = tweetRepository.save(tweet);

        return tweetMapper.toResponse(updatedTweet);
    }
    @Override
    @Transactional
    public void deleteTweet(Long tweetId, Long userId) {

        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Tweet bulunamadı: " + tweetId
                        )
                );


        if (!tweet.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Bu tweeti silme yetkiniz yok."
            );
        }


        commentRepository.deleteByTweetId(tweetId);
        likeRepository.deleteByTweetId(tweetId);
        retweetRepository.deleteByTweetId(tweetId);


        tweetRepository.delete(tweet);
    }

    @Override
    @Transactional
    public TweetResponse likeTweet(Long tweetId, String currentUsername) {
        User user = getUserByUsername(currentUsername);
        Tweet tweet = getTweetEntityById(tweetId);


        boolean alreadyLiked = likeRepository.existsByUserIdAndTweetId(user.getId(), tweet.getId());
        if (!alreadyLiked) {
            Like like = Like.builder()
                    .user(user)
                    .tweet(tweet)
                    .build();
            likeRepository.save(like);
        }

        return tweetMapper.toResponse(tweet);
    }

    @Override
    @Transactional
    public TweetResponse unlikeTweet(Long tweetId, String currentUsername) {
        User user = getUserByUsername(currentUsername);
        Tweet tweet = getTweetEntityById(tweetId);


        likeRepository.deleteByUserIdAndTweetId(user.getId(), tweet.getId());

        return tweetMapper.toResponse(tweet);
    }

    @Override
    @Transactional
    public TweetResponse retweet(Long tweetId, String currentUsername) {
        User user = getUserByUsername(currentUsername);
        Tweet tweet = getTweetEntityById(tweetId);

        boolean alreadyRetweeted = retweetRepository.existsByUserIdAndTweetId(user.getId(), tweet.getId());
        if (alreadyRetweeted) {

            retweetRepository.deleteByUserIdAndTweetId(user.getId(), tweet.getId());
        } else {
            Retweet retweet = Retweet.builder()
                    .user(user)
                    .tweet(tweet)
                    .build();
            retweetRepository.save(retweet);
        }

        return tweetMapper.toResponse(tweet);
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı: " + username));
    }

    private Tweet getTweetEntityById(Long id) {
        return tweetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tweet bulunamadı: " + id));
    }
}