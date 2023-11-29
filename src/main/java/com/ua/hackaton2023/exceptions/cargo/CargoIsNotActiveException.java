package com.ua.hackaton2023.exceptions.cargo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Cargo is not active!")
public class CargoIsNotActiveException extends RuntimeException {
}
