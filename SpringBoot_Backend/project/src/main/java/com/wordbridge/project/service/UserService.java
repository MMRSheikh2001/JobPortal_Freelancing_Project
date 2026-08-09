package com.wordbridge.project.service;


import com.wordbridge.project.admin.UserSearchRequestDTO;
import com.wordbridge.project.dto.requestdto.UserRequestDTO;
import com.wordbridge.project.dto.responsedto.UserResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    UserResponseDTO register(UserRequestDTO dto);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(Long id);



    void deleteUser(Long id);

    Long countAllUsers();

    UserResponseDTO  toggleSuspendedStatus(Long id);

    List<UserResponseDTO> search(
            UserSearchRequestDTO request
    );
}
