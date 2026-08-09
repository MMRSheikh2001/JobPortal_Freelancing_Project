package com.wordbridge.project.service;

import com.wordbridge.project.dto.requestdto.UserLanguageRequestDTO;
import com.wordbridge.project.dto.responsedto.UserLanguageResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserLanguageService {


    UserLanguageResponseDTO save(UserLanguageRequestDTO dto);

    List<UserLanguageResponseDTO> getAll();

    UserLanguageResponseDTO findById(Long id);

    UserLanguageResponseDTO update(
            Long id,
            UserLanguageRequestDTO dto
    );

    void delete(Long id);

    List<UserLanguageResponseDTO> findByUserProfileId(Long userProfileId);

    List<UserLanguageResponseDTO> findByLanguageId(Long languageId);


    UserLanguageResponseDTO
    findByUserProfileIdAndLanguageId(
            Long userProfileId,
            Long languageId
    );

    void deleteByUserProfileIdAndLanguageId(
            Long userProfileId,
            Long languageId
    );

    Long countLanguagesByUserProfileId(Long userProfileId);


}
