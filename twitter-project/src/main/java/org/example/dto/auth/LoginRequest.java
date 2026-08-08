package org.example.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class LoginRequest {

    @NotBlank(message = "Kullanıcı adı veya e-posta zorunludur.")
    private String usernameOrEmail;
    @NotBlank(message = "Şifre boş bırakılamaz.")
    private String password;

}
