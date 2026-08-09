package com.wordbridge.project.serviceimpl;

import com.wordbridge.project.dto.mapper.UserLanguageMapper;
import com.wordbridge.project.dto.requestdto.UserLanguageRequestDTO;
import com.wordbridge.project.dto.responsedto.UserLanguageResponseDTO;
import com.wordbridge.project.entity.UserLanguage;
import com.wordbridge.project.repository.UserLanguageRepository;
import com.wordbridge.project.service.UserLanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserLanguageServiceImpl implements UserLanguageService {
    
    private final UserLanguageRepository userLanguageRepository;
    private final UserLanguageMapper userLanguageMapper;


    @Override
    public UserLanguageResponseDTO save(UserLanguageRequestDTO dto) {
        if (userLanguageRepository.existsByUserProfileIdAndLanguageId(dto.getUserProfileId(), dto.getLanguageId())) {
            throw new RuntimeException("User Language already exists");
        }
        UserLanguage userLanguage = userLanguageMapper.toEntity(dto);
        UserLanguage saved = userLanguageRepository.save(userLanguage);
        return userLanguageMapper.toDTO(saved);
    }

    @Override
    public List<UserLanguageResponseDTO> getAll() {
        return userLanguageRepository.findAll().stream().map(userLanguageMapper::toDTO).toList();
    }

    @Override
    public UserLanguageResponseDTO findById(Long id) {
        UserLanguage userLanguage = userLanguageRepository.findById(id).orElseThrow(() -> new RuntimeException("No User Language Found"));
        return userLanguageMapper.toDTO(userLanguage);
    }

    @Override
    public UserLanguageResponseDTO update(Long id, UserLanguageRequestDTO dto) {

        UserLanguage existing = userLanguageRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User Language Not Found"));

        UserLanguage existingLanguage =
                userLanguageRepository.findByUserProfileIdAndLanguageId(
                        dto.getUserProfileId(),
                        dto.getLanguageId()
                );

        if (existingLanguage != null &&
                !existingLanguage.getId().equals(id)) {

            throw new RuntimeException(
                    "User Language already exists"
            );
        }


        UserLanguage userLanguage = userLanguageMapper.toEntity(dto);

        userLanguage.setId(existing.getId());


        UserLanguage updated =
                userLanguageRepository.save(userLanguage);

        return userLanguageMapper.toDTO(updated);
    }

    @Override
    public void delete(Long id) {
        userLanguageRepository.deleteById(id);
    }

    @Override
    public List<UserLanguageResponseDTO> findByUserProfileId(Long userProfileId) {
        return userLanguageRepository.findByUserProfileId(userProfileId).stream().map(userLanguageMapper::toDTO).toList();
    }

    @Override
    public List<UserLanguageResponseDTO> findByLanguageId(Long languageId) {
        return userLanguageRepository.findByLanguageId(languageId).stream().map(userLanguageMapper::toDTO).toList();
    }

    @Override
    public UserLanguageResponseDTO findByUserProfileIdAndLanguageId(Long userProfileId, Long languageId) {
        UserLanguage userLanguage = userLanguageRepository.findByUserProfileIdAndLanguageId(userProfileId, languageId);
        if (userLanguage == null) {
            throw new RuntimeException("User Language Not Found by this id");
        }
        return userLanguageMapper.toDTO(userLanguage);
    }

    @Override
    public void deleteByUserProfileIdAndLanguageId(Long userProfileId, Long languageId) {
        userLanguageRepository.deleteByUserProfileIdAndLanguageId(userProfileId, languageId);
    }

    @Override
    public Long countLanguagesByUserProfileId(Long userProfileId) {
        return userLanguageRepository.countByUserProfileId(userProfileId);
    }
}
