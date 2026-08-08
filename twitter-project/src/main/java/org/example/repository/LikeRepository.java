package org.example.repository;

import org.example.entity.Like;
import org.example.entity.Tweet;
import org.example.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByUserAndTweet(User user, Tweet tweet);
    Optional<Like> findByUserAndTweet(User user, Tweet tweet);

    boolean existsByUserIdAndTweetId(Long userId, Long tweetId);
    Optional<Like> findByUserIdAndTweetId(Long userId, Long tweetId);


    @Modifying
    @Query("DELETE FROM Like l WHERE l.user.id = :userId AND l.tweet.id = :tweetId")
    void deleteByUserIdAndTweetId(@Param("userId") Long userId, @Param("tweetId") Long tweetId);

    @Modifying
    @Query("DELETE FROM Like l WHERE l.tweet.id = :tweetId")
    void deleteByTweetId(@Param("tweetId") Long tweetId);

    long countByTweetId(Long tweetId);


    @EntityGraph(attributePaths = {"tweet", "tweet.user"})
    Page<Like> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}