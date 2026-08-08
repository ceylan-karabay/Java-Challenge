package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.common.ApiResponse;
import org.example.dto.common.PagedResponse;
import org.example.dto.user.UserResponse;
import org.example.dto.user.UserSummaryResponse;
import org.example.dto.user.UserUpdateRequest;
import org.example.service.FollowService;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FollowService followService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyProfile(Principal principal) {
        UserResponse response = userService.getUserProfileByUsername(principal.getName());
        return ResponseEntity.ok(ApiResponse.success(response, "Profil bilgileri getirildi."));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PagedResponse<UserSummaryResponse>>> searchUsers(
            @RequestParam(required = false, defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Principal principal) {

        PagedResponse<UserSummaryResponse> response =
                userService.searchUsers(
                        query,
                        page,
                        size,
                        principal.getName()
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        "Kullanıcı arama sonuçları getirildi."
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        UserResponse response = userService.getUserProfileById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Kullanıcı bilgileri getirildi."));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @Valid @RequestBody UserUpdateRequest request,
            Principal principal) {

        UserResponse response = userService.updateUserProfile(principal.getName(), request);
        return ResponseEntity.ok(ApiResponse.success(response, "Profil başarıyla güncellendi."));
    }


    @GetMapping("/{id}/user-followers")
    public ResponseEntity<ApiResponse<PagedResponse<UserSummaryResponse>>> getFollowers(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PagedResponse<UserSummaryResponse> response = followService.getFollowers(id, page, size);
        return ResponseEntity.ok(ApiResponse.success(response, "Takipçiler başarıyla getirildi."));
    }

    @GetMapping("/{id}/user-following")
    public ResponseEntity<ApiResponse<PagedResponse<UserSummaryResponse>>> getFollowing(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PagedResponse<UserSummaryResponse> response = followService.getFollowing(id, page, size);
        return ResponseEntity.ok(ApiResponse.success(response, "Takip edilenler başarıyla getirildi."));
    }

    @PostMapping("/{id}/user-follow")
    public ResponseEntity<ApiResponse<Void>> followUser(@PathVariable Long id, Principal principal) {
        followService.followUser(id, principal.getName());
        return ResponseEntity.ok(ApiResponse.success(null, "Kullanıcı takip edildi."));
    }

    @PostMapping("/{id}/user-unfollow")
    public ResponseEntity<ApiResponse<Void>> unfollowUser(@PathVariable Long id, Principal principal) {
        followService.unfollowUser(id, principal.getName());
        return ResponseEntity.ok(ApiResponse.success(null, "Takipten çıkarıldı."));
    }
}