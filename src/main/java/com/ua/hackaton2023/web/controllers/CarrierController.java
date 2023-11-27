package com.ua.hackaton2023.web.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/carrier")
public class CarrierController {
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("YES COOL", HttpStatus.OK);
    }
}
