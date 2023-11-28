package com.ua.hackaton2023.services.impl;

import com.ua.hackaton2023.entity.Carrier;
import com.ua.hackaton2023.entity.User;
import com.ua.hackaton2023.repository.CarrierRepository;
import com.ua.hackaton2023.services.CarrierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarrierServiceImpl implements CarrierService {
    private final CarrierRepository carrierRepository;

    @Override
    public Carrier addCarrier(User user) {
        if (user == null) {
            throw new NullPointerException();
        }
        return carrierRepository.save(new Carrier(user));
    }
}
