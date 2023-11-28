package com.ua.hackaton2023.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Password doesn't match")
public class PasswordBadRequestException extends RuntimeException {
}
