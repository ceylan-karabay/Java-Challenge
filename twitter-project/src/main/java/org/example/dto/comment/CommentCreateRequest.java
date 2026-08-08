package org.example.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentCreateRequest {
    @NotNull(message = "Tweet ID boş olamaz.")
    private Long tweetId;

    @NotBlank(message = "Yorum içeriği boş olamaz.")
    @Size(max = 280, message = "Yorum en fazla 280 karakter olabilir.")
    private String content;
}
