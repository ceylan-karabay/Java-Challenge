package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.comment.CommentCreateRequest;
import org.example.dto.comment.CommentResponse;
import org.example.dto.comment.CommentUpdateRequest;
import org.example.dto.common.ApiResponse;
import org.example.dto.common.PagedResponse;
import org.example.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/tweet/{tweetId}")
    public ResponseEntity<ApiResponse<PagedResponse<CommentResponse>>> getCommentsByTweetId(
            @PathVariable Long tweetId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PagedResponse<CommentResponse> response = commentService.getCommentsByTweetId(tweetId, page, size);
        return ResponseEntity.ok(ApiResponse.success(response, "Yorumlar getirildi."));
    }


    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @Valid @RequestBody CommentCreateRequest request,
            Principal principal) {
        CommentResponse response = commentService.createComment(request, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Yorum başarıyla eklendi."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentUpdateRequest request,
            Principal principal) {
        CommentResponse response = commentService.updateComment(id, request, principal.getName());
        return ResponseEntity.ok(ApiResponse.success(response, "Yorum başarıyla güncellendi."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long id,
            Principal principal) {
        commentService.deleteComment(id, principal.getName());
        return ResponseEntity.ok(ApiResponse.success(null, "Yorum başarıyla silindi."));
    }
}