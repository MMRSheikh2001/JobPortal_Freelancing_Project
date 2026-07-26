package com.wordbridge.project.dto.mapper;

import com.wordbridge.project.dto.requestdto.UserRequestDTO;
import com.wordbridge.project.dto.responsedto.UserResponseDTO;
import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.UserRole;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponseDTO toDTO(User user) {
        UserResponseDTO ur = new UserResponseDTO();
        ur.setId(user.getId());

        ur.setEmail(user.getEmail());

        ur.setRole(user.getRole());
        if(user.getRole()== UserRole.USER){
            ur.setName(user.getUserProfile().getName());
        } else if (user.getRole()== UserRole.COMPANY){
            ur.setName(user.getCompanyProfile().getName());
        } else {
            ur.setName("ADMIN");
        }

        ur.setIsActive(user.getIsActive());
        ur.setIsVerified(user.getIsVerified());
        ur.setIsSuspended(user.getIsSuspended());

        ur.setCreatedAt(user.getCreatedAt());
        ur.setUpdatedAt(user.getUpdatedAt());


        return ur;
    }

    public User toEntity(UserRequestDTO ur) {
        User u = new User();


        u.setEmail(ur.getEmail().trim().toLowerCase());
        u.setPassword(ur.getPassword());
        u.setRole(ur.getRole());




        return u;
    }


}
