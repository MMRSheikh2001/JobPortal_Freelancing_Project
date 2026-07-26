package com.wordbridge.project.controller;

import com.wordbridge.project.admin.UserSearchRequestDTO;
import com.wordbridge.project.dto.requestdto.UserRequestDTO;
import com.wordbridge.project.dto.responsedto.UserResponseDTO;

import com.wordbridge.project.service.UserService;


import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/api/users/")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    // Register User
    //open api
    @PostMapping("register")
    public ResponseEntity<UserResponseDTO> register(
            @RequestBody UserRequestDTO dto) {

        UserResponseDTO user = userService.register(dto);

        return ResponseEntity.ok(user);
    }

    // Get All Users
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {

        return ResponseEntity.ok(
                userService.getAllUsers()
        );
    }

    // Get User By id
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                userService.getUserById(id)
        );
    }


    // Delete User
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.ok("User Deleted Successfully");
    }

    //Suspend or unsuspend user
    @PatchMapping("{id}/toggle-suspend-status")
    public ResponseEntity<UserResponseDTO> toggleSuspendStatus(@PathVariable Long id) {
        return ResponseEntity.ok(userService.toggleSuspendedStatus(id));
    }

    @PostMapping("filter")
    public ResponseEntity<List<UserResponseDTO>> filter(
            @RequestBody UserSearchRequestDTO request
    ) {

        return ResponseEntity.ok(
                userService.search(request)
        );

    }

}
