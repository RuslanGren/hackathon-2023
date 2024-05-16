package com.ua.hackaton2023.web.controllers;

import com.ua.hackaton2023.entity.Cargo;
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

    @GetMapping("/{id}/cargos")
    public ResponseEntity<List<Cargo>> getCargosByUser(
            @PathVariable(name = "id") Long id
    ) {
        return new ResponseEntity<>(customerService.getCargosByUser(id), HttpStatus.OK);
    }

    @PostMapping("/cargos/add")
    public ResponseEntity<Customer> addCargo(
            @Valid @RequestBody CargoDto cargoDto
            ) {
        return new ResponseEntity<>(customerService.addCargo(cargoDto), HttpStatus.OK);
    }

    @DeleteMapping("/cargos/delete")
    public ResponseEntity<?> deleteCargo(
        @RequestParam("cargoId") Long cargoId
    ) {
        customerService.deleteCargo(cargoId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/cargos/choose")
    public ResponseEntity<Cargo> chooseCargoCarrier(
            @RequestParam("cargoId") Long cargoId,
            @RequestParam("carrierResponseId") Long carrierResponseId
    ) {
        return new ResponseEntity<>(customerService.chooseCargoCarrier(cargoId, carrierResponseId),
                HttpStatus.OK);
    }

    @PatchMapping("/cargos/finish")
    public ResponseEntity<Cargo> finishCargo(
            @RequestParam("cargoId") Long cargoId,
            @RequestParam("stars") int stars
    ) {
        return new ResponseEntity<>(customerService.finishCargo(cargoId, stars), HttpStatus.OK);
    }
}
