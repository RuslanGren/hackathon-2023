package com.ua.hackaton2023.web.controllers;

import com.ua.hackaton2023.entity.Carrier;
import com.ua.hackaton2023.services.CarrierService;
import com.ua.hackaton2023.web.carrier.CarDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carrier")
public class CarrierController {
    private final CarrierService carrierService;

    @PatchMapping("/cargo/pick")
    public ResponseEntity<Carrier> pickCargo(
            @RequestParam("cargoId") Long cargoId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return new ResponseEntity<>(carrierService.pickCargo(cargoId, userDetails), HttpStatus.OK);
    }

    @PostMapping("/add/car")
    public ResponseEntity<Carrier> addCar(
            @Valid @RequestBody CarDto carDto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return new ResponseEntity<>(carrierService.addCar(carDto, userDetails), HttpStatus.OK);
    }

    @DeleteMapping("/delete/car")
    public ResponseEntity<?> deleteCar(
            @RequestParam("carId") Long carId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        carrierService.deleteCar(carId, userDetails);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/add/cars")
    public ResponseEntity<Carrier> addCars(
            @Valid @RequestBody List<CarDto> carDtos,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return new ResponseEntity<>(carrierService.addCars(carDtos, userDetails), HttpStatus.OK);
    }
}
