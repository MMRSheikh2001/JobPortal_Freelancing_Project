package com.wordbridge.project.security;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String email;
    private String password;

}
