package com.wordbridge.project.serviceimpl;

import com.wordbridge.project.dto.mapper.UserProfileMapper;
import com.wordbridge.project.dto.requestdto.UserProfileRequestDTO;
import com.wordbridge.project.dto.responsedto.UserProfileResponseDTO;


import com.wordbridge.project.entity.UserProfile;
import com.wordbridge.project.enums.JobType;
import com.wordbridge.project.enums.WorkPlaceType;
import com.wordbridge.project.repository.UserProfileRepository;
import com.wordbridge.project.repository.UserRepository;
import com.wordbridge.project.service.UserProfileService;
import com.wordbridge.project.util.ImageStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;


    private final UserProfileMapper userProfileMapper;


    private final ImageStorageService imageStorageService;

    private final UserRepository userRepository;


    @Override
    @Transactional
    public UserProfileResponseDTO save(UserProfileRequestDTO up, MultipartFile image) {
        if (userProfileRepository.existsByUserId(up.getUserId())) {
            throw new RuntimeException("Profile already exists");
        }
        UserProfile userProfile = userProfileMapper.toEntity(up);

        String email = userRepository.findById(up.getUserId()).orElseThrow(() -> new RuntimeException("No User Found")).getEmail();
        if (image != null && !image.isEmpty()) {
            String fileName = imageStorageService.uploadImage(image, email, "userprofiles");
            userProfile.setImage(fileName);

        }
        UserProfile saved = userProfileRepository.save(userProfile);

        return userProfileMapper.toDTO(saved);
    }

    @Override
    public List<UserProfileResponseDTO> getAll() {
        return userProfileRepository.findAll().stream().map(userProfileMapper::toDTO).toList();
    }

    @Override
    public UserProfileResponseDTO findById(Long id) {
        UserProfile userProfile = userProfileRepository.findById(id).orElseThrow(() -> new RuntimeException("User Profile Not Found By this Id"));
        return userProfileMapper.toDTO(userProfile);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No User Profile found"));

        if (userProfile.getImage() != null) {
            imageStorageService.deleteImage(
                    "userprofiles",
                    userProfile.getImage()
            );
        }

        userProfileRepository.delete(userProfile);
    }

    @Override
    @Transactional
    public void deleteImage(Long profileId) {
        UserProfile profile = userProfileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile Not Found"));

        if (profile.getImage() != null) {

            imageStorageService.deleteImage("userprofiles",
                    profile.getImage()

            );

            profile.setImage(null);

            userProfileRepository.save(profile);
        }
    }

    @Override
    @Transactional
    public UserProfileResponseDTO update(
            Long id,
            UserProfileRequestDTO up,
            MultipartFile image) {

        UserProfile existingProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User Profile Not Found"));

        UserProfile userProfile = userProfileMapper.toEntity(up);

        userProfile.setId(id);

        //Preserving  created At
        userProfile.setCreatedAt(existingProfile.getCreatedAt());
        userProfile.setUpdatedAt(LocalDateTime.now());

        // Keep old image if no new image uploaded
        if (image != null && !image.isEmpty()) {

            String email = userRepository.findById(up.getUserId())
                    .orElseThrow(() -> new RuntimeException("No User Found"))
                    .getEmail();

            String fileName = imageStorageService.uploadImage(
                    image,
                    email,
                    "userprofiles"
            );

            userProfile.setImage(fileName);

        } else {

            userProfile.setImage(existingProfile.getImage());

        }
        Integer percentage = calculateProfileCompletion(userProfile);
        userProfile.setProfileCompleted(percentage == 100);

        UserProfile updated = userProfileRepository.save(userProfile);

        return userProfileMapper.toDTO(updated);
    }


    //Find User Profile By User Id
    @Override
    public UserProfileResponseDTO findByUserId(Long userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId).orElseThrow(()->new RuntimeException("No user profile found"));
        return userProfileMapper.toDTO(userProfile);
    }

    @Override
    public List<UserProfileResponseDTO> findByPresentAddressPoliceStationId(Long id) {
        return userProfileRepository.findByPresentAddressPoliceStationId(id).stream().map(userProfileMapper::toDTO).toList();
    }

    @Override
    public List<UserProfileResponseDTO> findByPresentAddressPoliceStationDistrictId(Long id) {
        return userProfileRepository.findByPresentAddressPoliceStationDistrictId(id).stream().map(userProfileMapper::toDTO).toList();
    }

    @Override
    public List<UserProfileResponseDTO> findByPermanentAddressPoliceStationId(Long id) {
        return userProfileRepository.findByPermanentAddressPoliceStationId(id).stream().map(userProfileMapper::toDTO).toList();
    }

    @Override
    public List<UserProfileResponseDTO> findByPermanentAddressPoliceStationDistrictId(Long id) {
        return userProfileRepository.findByPermanentAddressPoliceStationDistrictId(id).stream().map(userProfileMapper::toDTO).toList();

    }

    @Override
    public List<UserProfileResponseDTO> filterUsers(

            String keyword,

            Long countryId,

            Long divisionId,

            Long districtId,

            Long policeStationId,

            JobType jobType,

            WorkPlaceType workPlaceType,

            String gender

    ) {

        return userProfileRepository
                .filterUsers(

                        keyword,

                        countryId,

                        divisionId,

                        districtId,

                        policeStationId,

                        jobType,

                        workPlaceType,

                        gender

                )
                .stream()
                .map(userProfileMapper::toDTO)
                .toList();
    }

    @Override
    public Integer getProfileCompletionPercentage(Long userId) {

        UserProfile userProfile =
                userProfileRepository
                        .findByUserId(userId)
                        .orElseThrow(() ->
                                new RuntimeException("User profile not found"));

        return calculateProfileCompletion(userProfile);

    }

    @Override
    public Long countAllJobSeeker() {
        return userProfileRepository.count();
    }


    private Integer calculateProfileCompletion(UserProfile profile) {

        int total = 0;
        int completed = 0;

        // Basic Information

        total++;
        if (profile.getName() != null && !profile.getName().isBlank())
            completed++;

        total++;
        if (profile.getPhone() != null && !profile.getPhone().isBlank())
            completed++;

        total++;
        if (profile.getImage() != null && !profile.getImage().isBlank())
            completed++;

        total++;
        if (profile.getHeadline() != null && !profile.getHeadline().isBlank())
            completed++;

        total++;
        if (profile.getProfessionalSummary() != null && !profile.getProfessionalSummary().isBlank())
            completed++;

        total++;
        if (profile.getBio() != null && !profile.getBio().isBlank())
            completed++;

        // Personal Information

        total++;
        if (profile.getDateOfBirth() != null)
            completed++;

        total++;
        if (profile.getGender() != null)
            completed++;

        total++;
        if (profile.getNationality() != null && !profile.getNationality().isBlank())
            completed++;

        total++;
        if (profile.getReligion() != null && !profile.getReligion().isBlank())
            completed++;

        total++;
        if (profile.getMaritalStatus() != null && !profile.getMaritalStatus().isBlank())
            completed++;

        total++;
        if (profile.getFatherName() != null && !profile.getFatherName().isBlank())
            completed++;

        total++;
        if (profile.getMotherName() != null && !profile.getMotherName().isBlank())
            completed++;

        total++;
        if (profile.getNidNumber() != null && !profile.getNidNumber().isBlank())
            completed++;

        total++;
        if (profile.getPassportNumber() != null && !profile.getPassportNumber().isBlank())
            completed++;

        // Social Links

        total++;
        if (profile.getGithubLink() != null && !profile.getGithubLink().isBlank())
            completed++;

        total++;
        if (profile.getLinkedinLink() != null && !profile.getLinkedinLink().isBlank())
            completed++;

        total++;
        if (profile.getPortfolioWebsite() != null && !profile.getPortfolioWebsite().isBlank())
            completed++;

        // Career

        total++;
        if (profile.getExpectedSalary() != null)
            completed++;

        total++;
        if (profile.getCurrentSalary() != null)
            completed++;

        total++;
        if (profile.getPreferredJobType() != null)
            completed++;

        total++;
        if (profile.getPreferredWorkplace() != null)
            completed++;

        total++;
        if (profile.getCareerObjective() != null && !profile.getCareerObjective().isBlank())
            completed++;

        total++;
        if (profile.getFreelancerTitle() != null && !profile.getFreelancerTitle().isBlank())
            completed++;

        // Addresses

        total++;
        if (profile.getPresentAddress() != null)
            completed++;

        total++;
        if (profile.getPermanentAddress() != null)
            completed++;

        // Collections

        total++;
        if (profile.getUserSkills() != null && !profile.getUserSkills().isEmpty())
            completed++;

        total++;
        if (profile.getUserLanguages() != null && !profile.getUserLanguages().isEmpty())
            completed++;

        total++;
        if (profile.getEducations() != null && !profile.getEducations().isEmpty())
            completed++;

        total++;
        if (profile.getExperiences() != null && !profile.getExperiences().isEmpty())
            completed++;

        total++;
        if (profile.getTrainings() != null && !profile.getTrainings().isEmpty())
            completed++;

        total++;
        if (profile.getPortfolios() != null && !profile.getPortfolios().isEmpty())
            completed++;

        total++;
        if (profile.getReferences() != null && !profile.getReferences().isEmpty())
            completed++;

        total++;
        if (profile.getExtracurriculars() != null && !profile.getExtracurriculars().isEmpty())
            completed++;

        return Math.round((completed * 100f) / total);
    }
}
