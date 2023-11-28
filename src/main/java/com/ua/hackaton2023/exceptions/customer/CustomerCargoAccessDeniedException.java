package com.ua.hackaton2023.exceptions.customer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Cargo access denied")
public class CustomerCargoAccessDeniedException extends RuntimeException {
}
