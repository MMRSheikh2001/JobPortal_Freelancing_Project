package com.MMRSheikh2001.workbridgeandroid.request;

import com.MMRSheikh2001.workbridgeandroid.enums.UserRole;

import lombok.Data;

@Data
public class UserRequestDTO {


    private String email;

    private String password;

    private UserRole role;

    private String fullName;


}
