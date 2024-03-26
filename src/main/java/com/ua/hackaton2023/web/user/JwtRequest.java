package com.ua.hackaton2023.web.user;

import lombok.Data;

@Data
public class JwtRequest {
    private String email;
    private String password;
}
