package com.ua.hackaton2023.services.impl;

import com.ua.hackaton2023.entity.Role;
import com.ua.hackaton2023.repository.RoleRepository;
import com.ua.hackaton2023.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role getCustomerRole() {
        return roleRepository.findByName("ROLE_CUSTOMER").get();
    }

    @Override
    public Role getCarrierRole() {
        return roleRepository.findByName("ROLE_CARRIER").get();
    }

    @Override
    public Optional<Role> getRoleByName(String roleName) {
        return roleRepository.findByName(roleName);
    }
}
