package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.common.ApiResponse;
import org.example.dto.common.PagedResponse;
import org.example.dto.tweet.TweetCreateRequest;
import org.example.dto.tweet.TweetResponse;
import org.example.dto.tweet.TweetUpdateRequest;
import org.example.service.AuthService;
import org.example.service.LikeService;
import org.example.service.RetweetService;
import org.example.service.TweetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/tweet")
public class TweetController {

    private final TweetService tweetService;
    private final AuthService authService;
    private final LikeService likeService;
    private final RetweetService retweetService;

    public TweetController(TweetService tweetService,
                           AuthService authService,
                           LikeService likeService,
                           RetweetService retweetService) {
        this.tweetService = tweetService;
        this.authService = authService;
        this.likeService = likeService;
        this.retweetService = retweetService;
    }

    @GetMapping("/feed")
    public ResponseEntity<ApiResponse<PagedResponse<TweetResponse>>> getFeed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Principal principal) {

        PagedResponse<TweetResponse> response = tweetService.getFeed(page, size, principal.getName());
        return ResponseEntity.ok(ApiResponse.success(response, "Anasayfa akışı başarıyla getirildi."));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TweetResponse>> createTweet(
            @Valid @RequestBody TweetCreateRequest request,
            Principal principal) {

        TweetResponse response = tweetService.createTweet(request, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Tweet başarıyla oluşturuldu."));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<PagedResponse<TweetResponse>>> getTweetsByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Principal principal) {

        Long currentUserId = (principal != null) ? authService.getUserIdByUsername(principal.getName()) : null;

        PagedResponse<TweetResponse> response = tweetService.getTweetsByUserId(userId, page, size, currentUserId);
        return ResponseEntity.ok(ApiResponse.success(response, "Kullanıcıya ait tweetler başarıyla getirildi."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TweetResponse>> getTweetById(
            @PathVariable Long id,
            Principal principal) {

        Long currentUserId = (principal != null) ? authService.getUserIdByUsername(principal.getName()) : null;

        TweetResponse response = tweetService.getTweetById(id, currentUserId);
        return ResponseEntity.ok(ApiResponse.success(response, "Tweet detayları getirildi."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TweetResponse>> updateTweet(
            @PathVariable Long id,
            @Valid @RequestBody TweetUpdateRequest request,
            Principal principal) {

        Long currentUserId = authService.getUserIdByUsername(principal.getName());
        TweetResponse response = tweetService.updateTweet(id, request, currentUserId);
        return ResponseEntity.ok(ApiResponse.success(response, "Tweet güncellendi."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTweet(
            @PathVariable Long id,
            Principal principal) {

        Long currentUserId = authService.getUserIdByUsername(principal.getName());
        tweetService.deleteTweet(id, currentUserId);
        return ResponseEntity.ok(ApiResponse.success(null, "Tweet başarıyla silindi."));
    }


    @PostMapping("/{id}/like")
    public ResponseEntity<ApiResponse<Void>> likeTweet(
            @PathVariable("id") Long tweetId,
            Principal principal) {

        likeService.likeTweet(tweetId, principal.getName());
        return ResponseEntity.ok(ApiResponse.success(null, "Tweet başarıyla beğenildi."));
    }

    @DeleteMapping("/{id}/like")
    public ResponseEntity<ApiResponse<Void>> unlikeTweet(
            @PathVariable("id") Long tweetId,
            Principal principal) {

        likeService.unlikeTweet(tweetId, principal.getName());
        return ResponseEntity.ok(ApiResponse.success(null, "Tweet beğenisi kaldırıldı."));
    }


    @PostMapping("/{id}/retweet")
    public ResponseEntity<ApiResponse<Void>> retweet(
            @PathVariable("id") Long tweetId,
            Principal principal) {

        retweetService.retweet(tweetId, principal.getName());
        return ResponseEntity.ok(ApiResponse.success(null, "Tweet başarıyla retweet edildi."));
    }

    @DeleteMapping("/{id}/retweet")
    public ResponseEntity<ApiResponse<Void>> undoRetweet(
            @PathVariable("id") Long tweetId,
            Principal principal) {

        retweetService.undoRetweet(tweetId, principal.getName());
        return ResponseEntity.ok(ApiResponse.success(null, "Retweet geri alındı."));
    }


}