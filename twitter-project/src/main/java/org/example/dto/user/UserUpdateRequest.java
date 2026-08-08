package org.example.dto.user;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    @Size(max = 50, message = "Ad soyad en fazla 50 karakter olabilir.")
    private String name;

    @Size(max = 160, message = "Biyografi en fazla 160 karakter olabilir.")
    private String bio;

    private String profileImageUrl;

    private String bannerImageUrl;
}
