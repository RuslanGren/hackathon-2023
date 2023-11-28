package com.ua.hackaton2023.web.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {
    @NotBlank(message = "Імя не повинно бути пустим")
    @Size(max = 64, message = "Розмір пошти занадто великий!")
    @Email
    private String username;

    @NotBlank(message = "Пароль не повинний бути пустим")
    @Size(min = 4, max = 64, message = "Розмір пароля повинний бути між 4 і 64 символами")
    private String password;

    @NotBlank(message = "Пароль не повинний бути пустим")
    @Size(min = 4, max = 64, message = "Розмір пароля повинний бути між 4 і 64 символами")
    private String repeatedPassword;

    private String role;
}
