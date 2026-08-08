package org.example.repository;

import org.example.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);


    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);


    // KULLANICI ARAMA İÇİN EKLENEN METOD
    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<User> searchByUsernameOrFullName(@Param("query") String query, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.id != :currentUserId AND u.id NOT IN " +
            "(SELECT f.following.id FROM Follow f WHERE f.follower.id = :currentUserId)")
    List<User> findSuggestedUsers(@Param("currentUserId") Long currentUserId, Pageable pageable);
}