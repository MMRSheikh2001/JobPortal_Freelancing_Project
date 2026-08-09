package com.MMRSheikh2001.workbridgeandroid.cvinformations.response;

import com.MMRSheikh2001.workbridgeandroid.enums.LanguageProficiency;

import lombok.Data;

@Data
public class UserLanguageResponseDTO {

    private Long id;
    private LanguageProficiency proficiency;

    private Long languageId;
    private String languageName;

    private Long userProfileId;
    private String userName;
    private String userEmail;


}