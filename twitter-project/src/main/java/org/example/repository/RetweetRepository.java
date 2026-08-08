package org.example.repository;

import org.example.entity.Retweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RetweetRepository extends JpaRepository<Retweet, Long> {

    boolean existsByUserIdAndTweetId(Long userId, Long tweetId);

    Optional<Retweet> findByUserIdAndTweetId(Long userId, Long tweetId);

    @Modifying
    @Query("DELETE FROM Retweet r WHERE r.user.id = :userId AND r.tweet.id = :tweetId")
    void deleteByUserIdAndTweetId(@Param("userId") Long userId, @Param("tweetId") Long tweetId);

    @Modifying
    @Query("DELETE FROM Retweet r WHERE r.tweet.id = :tweetId")
    void deleteByTweetId(@Param("tweetId") Long tweetId);

    long countByTweetId(Long tweetId);
}