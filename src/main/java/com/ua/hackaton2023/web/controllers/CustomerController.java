package com.ua.hackaton2023.web.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @PostMapping("/add/cargo")
    public ResponseEntity<String> addCargo() {
        return new ResponseEntity<>("SUCCESSFUL", HttpStatus.OK);
    }
}
