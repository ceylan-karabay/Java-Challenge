package org.example.repository;

import org.example.entity.Tweet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {

    @Override
    @EntityGraph(attributePaths = {"user"})
    Page<Tweet> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"user"})
    Page<Tweet> findByUserIdInOrderByCreatedAtDesc(List<Long> userIds, Pageable pageable);

    @EntityGraph(attributePaths = {"user"})
    Page<Tweet> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    List<Tweet> findByUserId(Long userId);
}
