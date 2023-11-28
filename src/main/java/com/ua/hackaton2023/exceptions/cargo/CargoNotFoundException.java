package com.ua.hackaton2023.exceptions.cargo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Cargo not found!")
public class CargoNotFoundException extends RuntimeException {
}
