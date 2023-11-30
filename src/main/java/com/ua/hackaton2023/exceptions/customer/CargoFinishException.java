package com.ua.hackaton2023.exceptions.customer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Cargo finish exception")
public class CargoFinishException extends RuntimeException {
}
