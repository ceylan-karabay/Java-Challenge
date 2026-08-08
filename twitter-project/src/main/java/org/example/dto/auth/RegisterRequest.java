package org.example.dto.auth;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank(message = "Kullanıcı adı boş bırakılamaz.")
    @Size(min = 3, max = 30, message = "Kullanıcı adı 3 - 30 karakter arasında olmalıdır")
    private String username;

    @NotBlank(message = "Email alanı boş bırakılamaz.")
    @Email(message = "Geçerli bir email adresi giriniz.")
    private String email;

    @NotBlank(message = "Şifre boş bırakılmaz")
    @Size(min = 6, message = "Şifre en az 6 karakter olmalıdır")

    private String password;

    @NotBlank(message = "Ad ve soyad alanı boş olamaz.")
    private String fullName;

}
