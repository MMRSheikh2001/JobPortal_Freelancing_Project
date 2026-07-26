package com.wordbridge.project.service;


import com.wordbridge.project.dto.requestdto.UserProfileRequestDTO;
import com.wordbridge.project.dto.responsedto.UserProfileResponseDTO;
import com.wordbridge.project.enums.JobType;
import com.wordbridge.project.enums.WorkPlaceType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@Service
public interface UserProfileService {
    UserProfileResponseDTO save(UserProfileRequestDTO up, MultipartFile image);

    List<UserProfileResponseDTO> getAll();

    UserProfileResponseDTO findById(Long id);

    void delete(Long id);

    void deleteImage(Long profileId);

    UserProfileResponseDTO update(Long id, UserProfileRequestDTO up, MultipartFile image);

    UserProfileResponseDTO findByUserId(Long userId);

    List<UserProfileResponseDTO> findByPresentAddressPoliceStationId(Long id);

    List<UserProfileResponseDTO> findByPresentAddressPoliceStationDistrictId(Long id);


    List<UserProfileResponseDTO> findByPermanentAddressPoliceStationId(Long id);

    List<UserProfileResponseDTO> findByPermanentAddressPoliceStationDistrictId(Long id);


    List<UserProfileResponseDTO> filterUsers(

            String keyword,

            Long countryId,

            Long divisionId,

            Long districtId,

            Long policeStationId,

            JobType jobType,

            WorkPlaceType workPlaceType,

            String gender

    );

    Integer getProfileCompletionPercentage(Long userId);

    Long countAllJobSeeker();
}
