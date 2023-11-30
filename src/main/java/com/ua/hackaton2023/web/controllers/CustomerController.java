package com.ua.hackaton2023.web.controllers;

import com.ua.hackaton2023.entity.Cargo;
import com.ua.hackaton2023.entity.Customer;
import com.ua.hackaton2023.services.CustomerService;
import com.ua.hackaton2023.web.cargo.CargoDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/cargo/add")
    public ResponseEntity<Customer> addCargo(
            @Valid @RequestBody CargoDto cargoDto,
            @AuthenticationPrincipal UserDetails userDetails
            ) {
        return new ResponseEntity<>(customerService.addCargo(cargoDto, userDetails), HttpStatus.OK);
    }

    @DeleteMapping("/cargo/delete")
    public ResponseEntity<?> deleteCargo(
        @RequestParam("cargoId") Long cargoId,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        customerService.deleteCargo(cargoId, userDetails);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/cargo/choose")
    public ResponseEntity<Cargo> chooseCargoCarrier(
            @RequestParam("cargoId") Long cargoId,
            @RequestParam("carrierResponseId") Long carrierResponseId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return new ResponseEntity<>(customerService.chooseCargoCarrier(cargoId, carrierResponseId, userDetails),
                HttpStatus.OK);
    }

    @PatchMapping("/cargo/finish")
    public ResponseEntity<Cargo> finishCargo(
            @RequestParam("cargoId") Long cargoId,
            @RequestParam("stars") int stars,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return new ResponseEntity<>(customerService.finishCargo(cargoId, stars, userDetails), HttpStatus.OK);
    }
}
