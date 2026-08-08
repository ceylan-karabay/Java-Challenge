package org.example.controller;

import org.example.dto.common.ApiResponse;
import org.example.dto.common.PagedResponse;
import org.example.dto.user.UserSummaryResponse;
import org.example.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }


    @PostMapping("/{id}/follow")
    public ResponseEntity<ApiResponse<Void>> followUser(
            @PathVariable("id") Long targetUserId,
            Principal principal) {

        followService.followUser(targetUserId, principal.getName());
        return ResponseEntity.ok(ApiResponse.success(null, "Kullanıcı takip edildi."));
    }


    @DeleteMapping("/{id}/follow")
    public ResponseEntity<ApiResponse<Void>> unfollowUser(
            @PathVariable("id") Long targetUserId,
            Principal principal) {

        followService.unfollowUser(targetUserId, principal.getName());
        return ResponseEntity.ok(ApiResponse.success(null, "Kullanıcı takipten çıkarıldı."));
    }


    @GetMapping("/{id}/followers")
    public ResponseEntity<ApiResponse<PagedResponse<UserSummaryResponse>>> getFollowers(
            @PathVariable("id") Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PagedResponse<UserSummaryResponse> response = followService.getFollowers(userId, page, size);
        return ResponseEntity.ok(ApiResponse.success(response, "Takipçi listesi getirildi."));
    }


    @GetMapping("/{id}/following")
    public ResponseEntity<ApiResponse<PagedResponse<UserSummaryResponse>>> getFollowing(
            @PathVariable("id") Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PagedResponse<UserSummaryResponse> response = followService.getFollowing(userId, page, size);
        return ResponseEntity.ok(ApiResponse.success(response, "Takip edilenler listesi getirildi."));
    }
}
