package com.kameleoon.kameleoontrialtask.controller;

import com.kameleoon.kameleoontrialtask.dto.UserDTO;
import com.kameleoon.kameleoontrialtask.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(
        final UserService userService
    ) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String addNewUser(final Model model) {
        model.addAttribute("user", new UserDTO());

        return "register";
    }

    @PostMapping("/save_new_user")
    public String saveNewUser(
        @ModelAttribute("user") @Valid final UserDTO user,
        final BindingResult result
    ) {
        if (result.hasErrors()) {
            return "register";
        }

        userService.saveNewUser(user);

        return "redirect:/";
    }

    @GetMapping("/profile/{name}")
    public String getUserInfo(
        @PathVariable final String name,
        final Model model
    ) {
        final String username = userService.getUsernameFromSecurityContextHolder();

        if (username != null) {
            model.addAttribute("logged", userService.findByUsername(username));
        }

        model.addAttribute("loggedUsername", username);
        model.addAttribute("user", userService.findByUsername(name));

        return "profile";
    }
}
