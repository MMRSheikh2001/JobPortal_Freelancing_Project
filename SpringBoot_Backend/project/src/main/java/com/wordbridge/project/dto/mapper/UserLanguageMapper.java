package com.wordbridge.project.dto.mapper;

import com.wordbridge.project.dto.requestdto.UserLanguageRequestDTO;
import com.wordbridge.project.dto.responsedto.UserLanguageResponseDTO;
import com.wordbridge.project.entity.Language;
import com.wordbridge.project.entity.UserLanguage;
import com.wordbridge.project.entity.UserProfile;
import com.wordbridge.project.repository.LanguageRepository;
import com.wordbridge.project.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserLanguageMapper {

    private final UserProfileRepository userProfileRepository;

    private final LanguageRepository languageRepository;

    public UserLanguageResponseDTO toDTO(UserLanguage ul) {
        UserLanguageResponseDTO dto = new UserLanguageResponseDTO();
        dto.setId(ul.getId());
        dto.setProficiency(ul.getProficiency());

        dto.setLanguageId(ul.getLanguage().getId());
        dto.setLanguageName(ul.getLanguage().getName());

        dto.setUserProfileId(ul.getUserProfile().getId());
        dto.setUserName(ul.getUserProfile().getName());
        dto.setUserEmail(ul.getUserProfile().getUser().getEmail());

        return dto;
    }

    public UserLanguage toEntity(UserLanguageRequestDTO dto) {
        UserLanguage ul = new UserLanguage();
        ul.setProficiency(dto.getProficiency());

        Language language = languageRepository.findById(dto.getLanguageId()).orElseThrow(() -> new RuntimeException("No Language Found"));
        ul.setLanguage(language);

        UserProfile userProfile = userProfileRepository.findById(dto.getUserProfileId()).orElseThrow(() -> new RuntimeException("No User Profile Found By this Id"));
        ul.setUserProfile(userProfile);


        return ul;
    }


}
