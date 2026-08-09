package com.wordbridge.project.serviceimpl;

import com.wordbridge.project.dto.mapper.CompanyProfileMapper;
import com.wordbridge.project.dto.requestdto.CompanyProfileRequestDTO;
import com.wordbridge.project.dto.responsedto.CompanyProfileResponseDTO;
import com.wordbridge.project.entity.CompanyProfile;
import com.wordbridge.project.entity.UserProfile;
import com.wordbridge.project.job.CompanyProfileSpecification;
import com.wordbridge.project.job.CompanySearchRequestDTO;
import com.wordbridge.project.repository.CompanyProfileRepository;
import com.wordbridge.project.repository.UserRepository;
import com.wordbridge.project.service.CompanyProfileService;
import com.wordbridge.project.util.ImageStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CompanyProfileServiceImpl implements CompanyProfileService {

    private final CompanyProfileRepository companyProfileRepository;

    private final CompanyProfileMapper companyProfileMapper;

    private final ImageStorageService imageStorageService;

    private final UserRepository userRepository;


    @Override
    @Transactional
    public CompanyProfileResponseDTO save(CompanyProfileRequestDTO cp, MultipartFile image) {


        if (companyProfileRepository.existsByUserId(cp.getUserId())) {
            throw new RuntimeException("Profile already exists");
        }
        CompanyProfile companyProfile = companyProfileMapper.toEntity(cp);

        String email = userRepository.findById(cp.getUserId()).orElseThrow(() -> new RuntimeException("No User Found")).getEmail();
        if (image != null && !image.isEmpty()) {
            String fileName = imageStorageService.uploadImage(image, email, "companyprofiles");
            companyProfile.setImage(fileName);

        }
        CompanyProfile saved = companyProfileRepository.save(companyProfile);

        return companyProfileMapper.toDTO(saved);
    }

    @Override
    public List<CompanyProfileResponseDTO> getAll() {

        return companyProfileRepository.findAll().stream().map(companyProfileMapper::toDTO).toList();
    }

    @Override
    public CompanyProfileResponseDTO findById(Long id) {
        CompanyProfile companyProfile = companyProfileRepository.findById(id).orElseThrow(() -> new RuntimeException("Company Profile Not Found By this Id"));
        return companyProfileMapper.toDTO(companyProfile);
    }

    @Override
    public void delete(Long id) {
        CompanyProfile companyProfile = companyProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Company Profile found"));

        if (companyProfile.getImage() != null) {
            imageStorageService.deleteImage(
                    "companyprofiles",
                    companyProfile.getImage()
            );
        }

        companyProfileRepository.delete(companyProfile);
    }

    @Override
    @Transactional
    public void deleteImage(Long profileId) {


        CompanyProfile profile = companyProfileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile Not Found"));

        if (profile.getImage() != null) {

            imageStorageService.deleteImage("companyprofiles",
                    profile.getImage()

            );

            profile.setImage(null);

            companyProfileRepository.save(profile);
        }

    }

    @Override
    @Transactional
    public CompanyProfileResponseDTO update(Long id, CompanyProfileRequestDTO cp, MultipartFile image) {


        CompanyProfile existingProfile = companyProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company Profile Not Found"));

        CompanyProfile companyProfile = companyProfileMapper.toEntity(cp);

        companyProfile.setId(id);

        //Preserving  created At
        companyProfile.setCreatedAt(existingProfile.getCreatedAt());

        // Keep old image if no new image uploaded
        if (image != null && !image.isEmpty()) {

            String email = userRepository.findById(cp.getUserId())
                    .orElseThrow(() -> new RuntimeException("No User Found"))
                    .getEmail();

            String fileName = imageStorageService.uploadImage(
                    image,
                    email,
                    "companyprofiles"
            );

            companyProfile.setImage(fileName);

        } else {

            companyProfile.setImage(existingProfile.getImage());

        }

        CompanyProfile updated = companyProfileRepository.save(companyProfile);

        return companyProfileMapper.toDTO(updated);
    }

    // Find Company Profile By User Id
    @Override
    public CompanyProfileResponseDTO findByUserId(Long userId) {
        CompanyProfile companyProfile = companyProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No company profile found"));
        return companyProfileMapper.toDTO(companyProfile);
    }

    @Override
    public List<CompanyProfileResponseDTO> findByLocationPoliceStationId(Long id) {
        return companyProfileRepository.findByLocationPoliceStationId(id).stream().map(companyProfileMapper::toDTO).toList();
    }

    @Override
    public List<CompanyProfileResponseDTO> findByLocationPoliceStationDistrictId(Long id) {
        return companyProfileRepository.findByLocationPoliceStationDistrictId(id).stream().map(companyProfileMapper::toDTO).toList();
    }

    @Override
    public Boolean existsByUserId(Long userId) {
        return companyProfileRepository.existsByUserId(userId);
    }

    @Override
    public Long count() {
        return companyProfileRepository.count();
    }

    @Override
    public List<CompanyProfileResponseDTO> search(String keyword) {
        return companyProfileRepository.findByNameContainingIgnoreCase(keyword)
                .stream().map(companyProfileMapper::toDTO).toList();
    }


    @Override
    public Integer getProfileCompletionPercentage(Long userId) {

        CompanyProfile companyProfile =
                companyProfileRepository
                        .findByUserId(userId)
                        .orElseThrow(() ->
                                new RuntimeException("Company profile not found"));

        return calculateProfileCompletion(companyProfile);

    }

    @Override
    public List<CompanyProfileResponseDTO> search(
            CompanySearchRequestDTO request
    ) {

        Specification<CompanyProfile> specification =
                CompanyProfileSpecification.search(request);

        return companyProfileRepository
                .findAll(specification)
                .stream()
                .map(companyProfileMapper::toDTO)
                .toList();

    }


    private Integer calculateProfileCompletion(CompanyProfile profile) {

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
        if (profile.getCompanyEmail() != null && !profile.getCompanyEmail().isBlank())
            completed++;

        total++;
        if (profile.getImage() != null && !profile.getImage().isBlank())
            completed++;

        // Company Information

        total++;
        if (profile.getCompanyDescription() != null &&
                !profile.getCompanyDescription().isBlank())
            completed++;

        total++;
        if (profile.getCompanyWebsite() != null &&
                !profile.getCompanyWebsite().isBlank())
            completed++;

        total++;
        if (profile.getIndustry() != null &&
                !profile.getIndustry().isBlank())
            completed++;

        total++;
        if (profile.getFoundedYear() != null &&
                !profile.getFoundedYear().isBlank())
            completed++;

        total++;
        if (profile.getTradeLicenseNumber() != null &&
                !profile.getTradeLicenseNumber().isBlank())
            completed++;

        // Address

        total++;
        if (profile.getLocation() != null)
            completed++;

        // Jobs

        total++;
        if (profile.getJobs() != null &&
                !profile.getJobs().isEmpty())
            completed++;

        return Math.round((completed * 100f) / total);

    }
}
