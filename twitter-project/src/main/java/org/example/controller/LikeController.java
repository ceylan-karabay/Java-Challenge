package org.example.controller;

import org.example.dto.common.ApiResponse;
import org.example.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }


    @PostMapping("/tweet/{tweetId}")
    public ResponseEntity<ApiResponse<Void>> likeTweet(
            @PathVariable Long tweetId,
            Principal principal) {
        likeService.likeTweet(tweetId, principal.getName());
        return ResponseEntity.ok(ApiResponse.success(null, "Tweet beğenildi."));
    }


    @DeleteMapping("/tweet/{tweetId}")
    public ResponseEntity<ApiResponse<Void>> unlikeTweet(
            @PathVariable Long tweetId,
            Principal principal) {
        likeService.unlikeTweet(tweetId, principal.getName());
        return ResponseEntity.ok(ApiResponse.success(null, "Beğeni geri çekildi."));
    }
}
