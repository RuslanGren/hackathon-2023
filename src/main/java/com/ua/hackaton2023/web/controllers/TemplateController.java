package com.ua.hackaton2023.web.controllers;

import com.ua.hackaton2023.services.AuthService;
import com.ua.hackaton2023.web.user.JwtRequest;
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

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class TemplateController {
    private final AuthService authService;

    @GetMapping("/login")
    public String displayLoginPage(
            @RequestParam(required = false) Optional<Long> chatId,
            Model model) {
        model.addAttribute("chatId", chatId);
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(required = false) Optional<Long> chatId
    ) {
        JwtRequest request = new JwtRequest();
        request.setEmail(username);
        request.setPassword(password);
        authService.createAuthToken(request, chatId);
        return "successful";
    }

    @GetMapping("/register")
    public String displayRegisterPage(@RequestParam(required = false) Optional<Long> chatId, Model model) {
        model.addAttribute("user", new RegistrationUserDto());
        model.addAttribute("chatId", chatId);
        return "register";
    }

    @PostMapping("/register")
    public String registerNewUser(
            @RequestParam(required = false) Optional<Long> chatId,
            @ModelAttribute("user") @Valid RegistrationUserDto userDto,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("chatId", chatId);
            return "register";
        }

        try {
            authService.createNewUser(userDto);
            return chatId.map(id -> "redirect:/login?chatId=" + id)
                    .orElse("redirect:/login");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("chatId", chatId);
            return "register";
        }
    }
}
