package com.wordbridge.project.dto.requestdto;

import com.wordbridge.project.enums.UserRole;
import lombok.Data;

@Data
public class UserRequestDTO {


    private String email;

    private String password;

    private UserRole role;

    private String fullName;


}
