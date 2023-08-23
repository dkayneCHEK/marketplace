package com.example.freesale.services;

import com.example.freesale.entities.UserEntity;
import com.example.freesale.entities.enums.Role;
import com.example.freesale.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean createUser(UserEntity user) {
        String email = user.getEmail();
        if (userRepository.findByEmail(email) != null) return false;
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.ROLE_USER);
        log.info("Saving new User with email: {}", email);
        userRepository.save(user);
        return true;
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public void banUser(Long id) {
        UserEntity user = userRepository.findById(id).orElse(null);
        if(user != null) {
            if(user.isActive()) {
                user.setActive(false);
                log.info("Ban user with id: {}, email: {}", user.getId(), user.getEmail());
            }
            else {
                user.setActive(true);
                log.info("Unban user with id: {}, email: {}", user.getId(), user.getEmail());
            }
        }
        userRepository.save(user);
    }

    public void changeUserRoles(UserEntity user, Map<String, String> form) {
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());
        user.getRoles().clear();
        for (String key : form.keySet()) {
            if(roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userRepository.save(user);
    }

    public UserEntity getUserByPrincipal(Principal principal) {
        if (principal == null) return new UserEntity();
        return userRepository.findByEmail(principal.getName());
    }
}
