package com.example.freesale.cotrollers;

import com.example.freesale.entities.UserEntity;
import com.example.freesale.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String login(Principal principal, Model model) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));

        return "/user/login";
    }

    @GetMapping("/registration")
    public String registration(Principal principal, Model model) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));

        return "/user/registration";
    }

    @PostMapping("/registration")
    public String createNewUser(UserEntity user, Model model) {
        if (!userService.createUser(user)) {
            model.addAttribute("errorMessage", "Пользователь с email: "
                    + user.getEmail() + " уже существует");
            return "/user/registration";
        }
        userService.createUser(user);

        return "redirect:/login";
    }

    @GetMapping("/user/{user}")
    public String getUserProfile(@PathVariable("user") UserEntity user, Model model, Principal principal) {
        model.addAttribute("user", user);
        model.addAttribute("userByPrincipal", userService.getUserByPrincipal(principal));
        model.addAttribute("products", user.getProducts());

        return "/user/user_profile";
    }

    @GetMapping("/profile")
    public String profile(Principal principal,
                          Model model) {
        UserEntity user = userService.getUserByPrincipal(principal);
        model.addAttribute("user", user);

        return "/user/profile";
    }
}
