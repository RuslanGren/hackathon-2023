package com.ua.hackaton2023.web.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationUserDto {
    @NotBlank(message = "Повна назва не повинна бути пустою")
    @Size(max = 64, message = "Розмір повної назви занадто великий!")
    private String username;

    @NotBlank(message = "Пошта не повинна бути пустою")
    @Size(max = 64, message = "Розмір пошти занадто великий!")
    @Email
    private String email;

    @NotBlank(message = "Пароль не повинний бути пустим")
    @Size(min = 4, max = 64, message = "Розмір пароля повинний бути між 4 і 64 символами")
    private String password;

    @NotBlank(message = "Пароль не повинний бути пустим")
    @Size(min = 4, max = 64, message = "Розмір пароля повинний бути між 4 і 64 символами")
    private String confirmPassword;

    @NotNull
    private String role;
}
