package com.ua.hackaton2023.services;

import com.ua.hackaton2023.entity.Role;

import java.util.Optional;

public interface RoleService {
    Role getCustomerRole();

    Role getCarrierRole();

    Optional<Role> getRoleByName(String roleName);
}
