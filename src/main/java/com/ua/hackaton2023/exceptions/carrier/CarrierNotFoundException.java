package com.ua.hackaton2023.exceptions.carrier;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Carrier not found")
public class CarrierNotFoundException extends RuntimeException {
}
