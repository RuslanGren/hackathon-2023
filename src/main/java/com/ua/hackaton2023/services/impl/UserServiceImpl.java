package com.ua.hackaton2023.services.impl;

import com.ua.hackaton2023.entity.Carrier;
import com.ua.hackaton2023.entity.Customer;
import com.ua.hackaton2023.entity.Role;
import com.ua.hackaton2023.entity.User;
import com.ua.hackaton2023.exceptions.BadRequestException;
import com.ua.hackaton2023.exceptions.user.UserNotFoundException;
import com.ua.hackaton2023.repository.CarrierRepository;
import com.ua.hackaton2023.repository.CustomerRepository;
import com.ua.hackaton2023.repository.UserRepository;
import com.ua.hackaton2023.services.RoleService;
import com.ua.hackaton2023.services.UserService;
import com.ua.hackaton2023.web.user.RegistrationUserDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {
    private UserRepository userRepository;
    private CustomerRepository customerRepository;
    private CarrierRepository carrierRepository;
    private PasswordEncoder passwordEncoder;
    private RoleService roleService;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Autowired
    public void setCarrierRepository(CarrierRepository carrierRepository) {
        this.carrierRepository = carrierRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User '%s' not found", username)
        ));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
        );
    }

    @Override
    public User getUser() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication().getName();

        return userRepository
                .findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public Optional<User> findByChatId(Long chatId) {
        return userRepository.findByChatId(chatId);
    }

    @Override
    public User createNewUser(RegistrationUserDto registrationUserDto) {
        User user = new User();
        user.setUsername(registrationUserDto.getUsername());
        user.setEmail(registrationUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));

        String roleName = registrationUserDto.getRole();
        Role userRole = roleService.getRoleByName(roleName).orElseThrow(() -> new BadRequestException("Wrong role"));
        user.setRoles(List.of(userRole));
        userRepository.save(user);

        if (roleName.equals("ROLE_CUSTOMER")) {
            customerRepository.save(new Customer(registrationUserDto.getUsername(), user));
        } else if (roleName.equals("ROLE_CARRIER")) {
            carrierRepository.save(new Carrier(registrationUserDto.getUsername(), user));
        }

        return user;
    }

    @Override
    public void addChatId(String email, Long chatId) {
        User user = findByEmail(email).get();
        user.setChatId(chatId);
        userRepository.save(user);
    }
}
