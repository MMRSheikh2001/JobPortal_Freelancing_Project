package com.wordbridge.project.security;


import com.wordbridge.project.entity.User;
import com.wordbridge.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {



    private final UserRepository userRepository;

    public User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }



}
