package com.ua.hackaton2023.exceptions.car;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Car not found!")
public class CarNotFoundException extends RuntimeException {
}
