package com.ua.hackaton2023.web.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {
    @NotBlank(message = "Імя не повинно бути пустим!")
    @Size(min = 3, max = 16, message = "Розмір імені повинний бути між 3 і 16 символами!")
    private String username;

    @NotBlank(message = "Пароль не повинний бути пустим!")
    @Size(min = 4, max = 16, message = "Розмір пароля повинний бути між 4 і 16 символами!")
    private String password;

    private String role;
}
