package org.example.dto.tweet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.user.UserSummaryResponse;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TweetResponse {
    private Long id;
    private String content;
    private String mediaUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private UserSummaryResponse author;

    private long likeCount;
    private long retweetCount;
    private long replyCount;

    private boolean likedByCurrentUser;
    private boolean retweetedByCurrentUser;

}
