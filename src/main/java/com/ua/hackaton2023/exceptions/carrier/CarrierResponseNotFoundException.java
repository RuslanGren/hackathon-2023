package com.ua.hackaton2023.exceptions.carrier;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "CarrierResponseNotFoundException")
public class CarrierResponseNotFoundException extends RuntimeException {
}
