package org.example.dto.comment;

import lombok.Data;
import org.example.dto.user.UserSummaryResponse;

import java.time.LocalDateTime;

@Data
public class CommentResponse {
    private Long id;
    private String content;
    private Long tweetId;
    private UserSummaryResponse author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
