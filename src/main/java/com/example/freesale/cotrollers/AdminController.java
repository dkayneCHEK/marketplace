package com.example.freesale.cotrollers;

import com.example.freesale.entities.UserEntity;
import com.example.freesale.entities.enums.Role;
import com.example.freesale.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {
    private final UserService userService;

    @GetMapping("/admin")
    public String admin(Model model, Principal principal) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("user", userService.getUserByPrincipal(principal));

        return "/admin/admin";
    }


    @PostMapping("/admin/user/ban/{id}")
    public String userBan(@PathVariable("id") Long id) {
        userService.banUser(id);

        return "redirect:/admin";
    }

    @GetMapping("/admin/user/edit/{user}")
    public String userEdit(@PathVariable("user") UserEntity user, Model model, Principal principal) {
        model.addAttribute("user", user);
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("roles", Role.values());

        return "admin/edit_user";
    }

    @PostMapping("/admin/user/edit")
    public String userEdit(@RequestParam("userId") UserEntity user, @RequestParam Map<String, String> form) {
        userService.changeUserRoles(user, form);
        return "redirect:/admin";
    }
}

