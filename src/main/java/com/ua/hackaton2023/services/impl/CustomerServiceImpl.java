package com.ua.hackaton2023.services.impl;

import com.ua.hackaton2023.entity.*;
import com.ua.hackaton2023.exceptions.cargo.CargoAccessDeniedException;
import com.ua.hackaton2023.exceptions.carrier.CarrierNotFoundException;
import com.ua.hackaton2023.exceptions.carrier.CarrierResponseNotFoundException;
import com.ua.hackaton2023.exceptions.customer.CustomerCargoAccessDeniedException;
import com.ua.hackaton2023.repository.CarrierResponseRepository;
import com.ua.hackaton2023.repository.CustomerRepository;
import com.ua.hackaton2023.services.CargoService;
import com.ua.hackaton2023.services.CustomerService;
import com.ua.hackaton2023.web.cargo.CargoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CargoService cargoService;
    private final CarrierResponseRepository carrierResponseRepository;

    @Override
    public Customer getCustomerByUserDetails(UserDetails userDetails) {
        return customerRepository.findByUser((User) userDetails);
    }

    @Override
    public Customer addCargo(CargoDto cargoDto, UserDetails userDetails) {
        Customer customer = getCustomerByUserDetails(userDetails);

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
    public void deleteCargo(Long cargoId, UserDetails userDetails) {
        Cargo cargo = cargoService.getCargoById(cargoId);
        Customer customer = getCustomerByUserDetails(userDetails);
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
    public Cargo chooseCargoCarrier(Long cargoId, Long carrierResponseId, UserDetails userDetails) {
        Customer customer = getCustomerByUserDetails(userDetails);
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
        return cargoService.saveCargo(cargo); // save cargo in db
    }

}
