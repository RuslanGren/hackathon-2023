package com.ua.hackaton2023.services.impl;

import com.ua.hackaton2023.entity.*;
import com.ua.hackaton2023.exceptions.BadRequestException;
import com.ua.hackaton2023.exceptions.cargo.CargoAccessDeniedException;
import com.ua.hackaton2023.exceptions.carrier.CarrierNotFoundException;
import com.ua.hackaton2023.exceptions.carrier.CarrierResponseNotFoundException;
import com.ua.hackaton2023.exceptions.customer.CargoFinishException;
import com.ua.hackaton2023.exceptions.customer.CustomerCargoAccessDeniedException;
import com.ua.hackaton2023.repository.CarrierResponseRepository;
import com.ua.hackaton2023.repository.CustomerRepository;
import com.ua.hackaton2023.services.CargoService;
import com.ua.hackaton2023.services.CarrierService;
import com.ua.hackaton2023.services.CustomerService;
import com.ua.hackaton2023.web.cargo.CargoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CargoService cargoService;
    private final CarrierResponseRepository carrierResponseRepository;
    private final CarrierService carrierService;
    private final UserServiceImpl userService;

    private static final int MIN_RATING = 1;
    private static final int MAX_RATING = 5;

    @Override
    public List<Cargo> getCargosByUser(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Customer doesn't exists")).getCargoList();
    }

    @Override
    public Customer getCustomer() {
        return customerRepository.findByUser(userService.getUser());
    }

    @Override
    public Customer addCargo(CargoDto cargoDto) {
        Customer customer = getCustomer();
        Cargo cargo = cargoService.createCargo(cargoDto, customer);
        cargoService.saveCargo(cargo);

        List<Cargo> cargoList = customer.getCargoList();
        cargoList.add(cargo);
        customer.setCargoList(cargoList);
        return customerRepository.save(customer);
    }

    @Override
    public Customer addCustomer(User user, String name) {
        if (user == null) {
            throw new NullPointerException();
        }
        return customerRepository.save(new Customer(name, user));
    }

    @Override
    public void deleteCargo(Long cargoId) {
        Cargo cargo = cargoService.getCargoById(cargoId);
        Customer customer = getCustomer();
        if (!cargo.getCustomer().equals(customer)) {
            throw new CustomerCargoAccessDeniedException();
        }
        List<Cargo> cargos = customer.getCargoList();
        cargos.remove(cargo);
        customer.setCargoList(cargos);
        customerRepository.save(customer);
        cargoService.removeCargo(cargo);
    }

    @Override
    public Cargo chooseCargoCarrier(Long cargoId, Long carrierResponseId) {
        Customer customer = getCustomer();
        Cargo cargo = cargoService.getCargoById(cargoId);
        if (!cargo.getCustomer().equals(customer)) {
            throw new CargoAccessDeniedException();
        }

        CarrierResponse carrierResponse = carrierResponseRepository.findById(carrierResponseId)
                .orElseThrow(CarrierResponseNotFoundException::new);
        if (!cargo.getResponses().contains(carrierResponse)) {
            throw new CarrierNotFoundException();
        }
        carrierResponse.setApplied(true);
        carrierResponseRepository.save(carrierResponse); // save carrier response in db

        cargo.setActive(false);
        cargo.setCarrier(carrierResponse.getCarrier());
        return cargoService.saveCargo(cargo); // save cargo in db
    }

    @Override
    public Cargo finishCargo(Long cargoId, int stars) {
        if (stars < MIN_RATING || stars > MAX_RATING) {
            throw new CargoFinishException();
        }
        Customer customer = getCustomer();
        Cargo cargo = cargoService.getCargoById(cargoId);
        if (!cargo.getCustomer().equals(customer)) {
            throw new CargoAccessDeniedException();
        }
        if (cargo.isFinished()) {
            throw new CargoAccessDeniedException();
        }
        carrierService.setScore(cargo.getCarrier(), stars);

        cargo.setFinished(true);
        return cargoService.saveCargo(cargo);
    }

}
