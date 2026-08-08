package org.example.dto.tweet;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TweetCreateRequest {

    @NotBlank(message = "Tweet içeriği boş olamaz.")
    @Size(min = 1, max = 280, message = "Tweet en fazla 280 karakter olabilir.")
    private String content;

    private String mediaUrl;

}
