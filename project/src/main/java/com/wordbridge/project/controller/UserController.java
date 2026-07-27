package com.wordbridge.project.controller;

import com.wordbridge.project.admin.UserSearchRequestDTO;
import com.wordbridge.project.dto.requestdto.UserRequestDTO;
import com.wordbridge.project.dto.responsedto.UserResponseDTO;

import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.security.AuthenticationService;
import com.wordbridge.project.service.UserService;


import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/api/users/")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;
    private final AuthenticationService authenticationService;

    // Register User
    //open api
    @PreAuthorize("permitAll()")
    @PostMapping("register")
    public ResponseEntity<UserResponseDTO> register(
            @RequestBody UserRequestDTO dto) {

        if (dto.getRole() == UserRole.ADMIN) {
            throw new AccessDeniedException("Cannot self-register as ADMIN");
        }

        UserResponseDTO user = userService.register(dto);

        return ResponseEntity.ok(user);
    }

    // Get All Users
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {

        return ResponseEntity.ok(
                userService.getAllUsers()
        );
    }

    // Get User By id
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @PathVariable Long id) {
        User currentUser = authenticationService.getCurrentUser();
        if (!currentUser.getId().equals(id) && currentUser.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Not allowed");
        }

        return ResponseEntity.ok(
                userService.getUserById(id)
        );
    }


    // Delete User
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.ok("User Deleted Successfully");
    }

    //Suspend or unsuspend user
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("{id}/toggle-suspend-status")
    public ResponseEntity<UserResponseDTO> toggleSuspendStatus(@PathVariable Long id) {
        return ResponseEntity.ok(userService.toggleSuspendedStatus(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("filter")
    public ResponseEntity<List<UserResponseDTO>> filter(
            @RequestBody UserSearchRequestDTO request
    ) {

        return ResponseEntity.ok(
                userService.search(request)
        );

    }

}
