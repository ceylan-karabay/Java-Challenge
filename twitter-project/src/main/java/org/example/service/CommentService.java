package org.example.service;

import org.example.dto.comment.CommentCreateRequest;
import org.example.dto.comment.CommentResponse;
import org.example.dto.comment.CommentUpdateRequest;
import org.example.dto.common.PagedResponse;

public interface CommentService {
    CommentResponse createComment(CommentCreateRequest request, String username);
    CommentResponse updateComment(Long commentId, CommentUpdateRequest request, String username);
    void deleteComment(Long commentId, String username);
    PagedResponse<CommentResponse> getCommentsByTweetId(Long tweetId, int page, int size);
}
