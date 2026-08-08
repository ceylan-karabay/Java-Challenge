package org.example.repository;

import org.example.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {


    @EntityGraph(attributePaths = {"user"})
    Page<Comment> findByTweetIdOrderByCreatedAtDesc(Long tweetId, Pageable pageable);


    @EntityGraph(attributePaths = {"tweet", "tweet.user"})
    Page<Comment> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    Page<Comment> findByTweetId(Long tweetId, Pageable pageable);

    @Modifying
    @Query("DELETE FROM Comment c WHERE c.tweet.id = :tweetId")
    void deleteByTweetId(@Param("tweetId") Long tweetId);

    long countByTweetId(Long tweetId);
}