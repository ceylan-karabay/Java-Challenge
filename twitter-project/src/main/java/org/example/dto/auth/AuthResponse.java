package org.example.dto.auth;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.dto.user.UserSummaryResponse;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String accessToken;
    @Builder.Default
    private String tokenType = "Bearer";
    private UserSummaryResponse user;

}
