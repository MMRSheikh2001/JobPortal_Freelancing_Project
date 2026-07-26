package com.wordbridge.project.security;

import lombok.Data;

@Data
public class ResetPasswordRequestDTO {
    private String token;
    private String newPassword;
}
