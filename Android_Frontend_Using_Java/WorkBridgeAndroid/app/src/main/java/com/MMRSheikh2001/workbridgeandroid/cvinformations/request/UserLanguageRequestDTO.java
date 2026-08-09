package com.MMRSheikh2001.workbridgeandroid.cvinformations.request;

import com.MMRSheikh2001.workbridgeandroid.enums.LanguageProficiency;

import lombok.Data;

@Data
public class UserLanguageRequestDTO {

    private LanguageProficiency proficiency;

    private Long languageId;

    private Long userProfileId;


}
