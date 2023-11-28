package com.ua.hackaton2023.web.controllers;

import com.ua.hackaton2023.entity.Carrier;
import com.ua.hackaton2023.services.CarrierService;
import com.ua.hackaton2023.web.carrier.CarDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carrier")
public class CarrierController {
    private final CarrierService carrierService;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("YES COOL", HttpStatus.OK);
    }

    @PostMapping("/add/car")
    public ResponseEntity<Carrier> addCar(@Valid @RequestBody CarDto carDto) {
        return new ResponseEntity<>(carrierService.addCar(carDto), HttpStatus.OK);
    }

    @PostMapping("/add/cars")
    public ResponseEntity<Carrier> addCars(@Valid @RequestBody List<CarDto> carDtos) {
        return new ResponseEntity<>(carrierService.addCars(carDtos), HttpStatus.OK);
    }
}
