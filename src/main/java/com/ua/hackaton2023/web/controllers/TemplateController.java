package com.ua.hackaton2023.web.controllers;

import com.ua.hackaton2023.services.AuthService;
import com.ua.hackaton2023.web.user.JwtRequest;
import com.ua.hackaton2023.web.user.JwtResponse;
import com.ua.hackaton2023.web.user.RegistrationUserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class TemplateController {
    private final AuthService authService;

    @GetMapping("/login")
    public String displayLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        JwtRequest request = new JwtRequest();
        request.setEmail(username);
        request.setPassword(password);
        JwtResponse response = authService.createAuthToken(request);
        System.out.println(response.getToken());
        return "successful";
    }

    @GetMapping("/successful")
    public String displaySuccessfulPage() {
        return "successful";
    }

    @GetMapping("/register")
    public String displayRegisterPage(Model model) {
        model.addAttribute("user", new RegistrationUserDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerNewUser(
            @ModelAttribute("user") @Valid RegistrationUserDto userDto,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            System.out.println(userDto.getEmail());
            System.out.println(userDto.getUsername());
            System.out.println(userDto.getPassword());
            System.out.println(userDto.getConfirmPassword());
            System.out.println(userDto.getRole());
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}
