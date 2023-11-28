package com.ua.hackaton2023.exceptions.customer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Customer not found!")
public class CustomerNotFoundException extends RuntimeException {
}
