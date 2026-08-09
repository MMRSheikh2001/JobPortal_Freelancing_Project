package com.MMRSheikh2001.workbridgeandroid.response;


import lombok.Data;

@Data
public class LoginResponseDTO {
    private String token;
    private String tokenType = "Bearer";

    private Long userId;
    private String email;
    private String role;

    private Long profileId;

    private String displayName;

    private String image;

}
