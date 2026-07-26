package com.wordbridge.project.dto.mapper;

import com.wordbridge.project.dto.requestdto.ResumeRequestDTO;
import com.wordbridge.project.dto.responsedto.ResumeFileResponseDTO;
import com.wordbridge.project.entity.Resume;
import com.wordbridge.project.entity.UserProfile;
import com.wordbridge.project.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResumeMapper {
    private final UserProfileRepository userProfileRepository;

  public   ResumeFileResponseDTO toDTO(Resume r){
        ResumeFileResponseDTO dto=new ResumeFileResponseDTO();
        dto.setId(r.getId());
        dto.setUserProfileId(r.getUserProfile().getId());
        dto.setUserName(r.getUserProfile().getName());
        dto.setFileName(r.getFileName());
        dto.setUploadedAt(r.getUploadedAt());


        return dto;
    }

 public    Resume toEntity(ResumeRequestDTO dto){
        Resume r=new Resume();
        UserProfile userProfile=userProfileRepository.findById(dto.getUserProfileId())
                .orElseThrow(()->new RuntimeException("No User Profile found"));
        r.setUserProfile(userProfile);




        return r;
    }


}
