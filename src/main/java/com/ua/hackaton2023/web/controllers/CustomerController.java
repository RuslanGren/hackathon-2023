package com.ua.hackaton2023.web.controllers;

import com.ua.hackaton2023.entity.Customer;
import com.ua.hackaton2023.services.CustomerService;
import com.ua.hackaton2023.web.cargo.CargoDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/get/all")
    public ResponseEntity<List<Customer>> getAll() {
        return new ResponseEntity<>(customerService.getAll(), HttpStatus.OK);
    }

    @PostMapping("/add/cargo")
    public ResponseEntity<Customer> addCargo(@Valid @RequestBody CargoDto cargoDto, @RequestParam("id") Long id) {
        return new ResponseEntity<>(customerService.addCargo(cargoDto, id), HttpStatus.OK);
    }
}
