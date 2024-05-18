package com.ua.hackaton2023.web.controllers;

import com.ua.hackaton2023.entity.Carrier;
import com.ua.hackaton2023.services.CarrierService;
import com.ua.hackaton2023.web.carrier.CarDto;
import com.ua.hackaton2023.web.carrier.CarrierResponseDto;
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

    @PatchMapping("/cargo/response")
    public ResponseEntity<Carrier> responseCargo(
            @RequestBody CarrierResponseDto carrierResponseDto
    ) {
        return new ResponseEntity<>(carrierService.responseCargo(carrierResponseDto), HttpStatus.OK);
    }

    @PostMapping("/add/car")
    public ResponseEntity<Carrier> addCar(
            @Valid @RequestBody CarDto carDto
    ) {
        return new ResponseEntity<>(carrierService.addCar(carDto), HttpStatus.OK);
    }

    @DeleteMapping("/delete/car")
    public ResponseEntity<?> deleteCar(
            @RequestParam("carId") Long carId
    ) {
        carrierService.deleteCar(carId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/add/cars")
    public ResponseEntity<Carrier> addCars(
            @Valid @RequestBody List<CarDto> carDtos
    ) {
        return new ResponseEntity<>(carrierService.addCars(carDtos), HttpStatus.OK);
    }
}
