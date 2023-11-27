package com.ua.hackaton2023.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User is already exists!")
public class UserAlreadyExistsException extends RuntimeException {
}
