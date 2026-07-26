package com.wordbridge.project.dto.requestdto;

import com.wordbridge.project.enums.LanguageProficiency;
import lombok.Data;

@Data
public class UserLanguageRequestDTO {

    private LanguageProficiency proficiency;

    private Long languageId;

    private Long userProfileId;


}
