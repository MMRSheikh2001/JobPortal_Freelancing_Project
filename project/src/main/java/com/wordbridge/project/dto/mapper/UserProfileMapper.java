package com.wordbridge.project.dto.mapper;

import com.wordbridge.project.dto.requestdto.UserProfileRequestDTO;
import com.wordbridge.project.dto.responsedto.UserProfileResponseDTO;
import com.wordbridge.project.entity.Address;
import com.wordbridge.project.entity.User;
import com.wordbridge.project.entity.UserProfile;
import com.wordbridge.project.repository.AddressRepository;
import com.wordbridge.project.repository.PoliceStationRepository;
import com.wordbridge.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserProfileMapper {

    private final UserRepository userRepository;

    private final AddressRepository addressRepository;


    private final PoliceStationRepository policeStationRepository;


    public UserProfileResponseDTO toDTO(UserProfile entity) {
        UserProfileResponseDTO dto = new UserProfileResponseDTO();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUser().getId());
        dto.setUserEmail(entity.getUser().getEmail());

        dto.setName(entity.getName());
        dto.setPhone(entity.getPhone());
        dto.setImage(entity.getImage());

        dto.setHeadline(entity.getHeadline());
        dto.setProfessionalSummary(entity.getProfessionalSummary());
        dto.setBio(entity.getBio());

        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setGender(entity.getGender());
        dto.setNationality(entity.getNationality());
        dto.setReligion(entity.getReligion());
        dto.setMaritalStatus(entity.getMaritalStatus());

        dto.setFatherName(entity.getFatherName());
        dto.setMotherName(entity.getMotherName());

        dto.setNidNumber(entity.getNidNumber());
        dto.setPassportNumber(entity.getPassportNumber());

        dto.setGithubLink(entity.getGithubLink());
        dto.setPortfolioWebsite(entity.getPortfolioWebsite());
        dto.setLinkedinLink(entity.getLinkedinLink());

        dto.setExpectedSalary(entity.getExpectedSalary());
        dto.setCurrentSalary(entity.getCurrentSalary());

        dto.setPreferredJobType(entity.getPreferredJobType());
        dto.setPreferredWorkplace(entity.getPreferredWorkplace());

        dto.setCareerObjective(entity.getCareerObjective());
        dto.setFreelancerTitle(entity.getFreelancerTitle());

        dto.setProfileCompleted(entity.getProfileCompleted());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        //Setting All Present Address Fields

        if (entity.getPresentAddress() != null) {

            dto.setPresentAddressId(entity.getPresentAddress().getId());
            dto.setPresentAddressDetails(entity.getPresentAddress().getDetails());
            dto.setPresentAddressPostCode(entity.getPresentAddress().getPostCode());

            dto.setPresentCountryId(entity.getPresentAddress().getPoliceStation().getDistrict().getDivision().getCountry().getId());
            dto.setPresentCountryName(entity.getPresentAddress().getPoliceStation().getDistrict().getDivision().getCountry().getName());
            dto.setPresentCountryCode(entity.getPresentAddress().getPoliceStation().getDistrict().getDivision().getCountry().getCode());

            dto.setPresentDivisionId(entity.getPresentAddress().getPoliceStation().getDistrict().getDivision().getId());
            dto.setPresentDivisionName(entity.getPresentAddress().getPoliceStation().getDistrict().getDivision().getName());

            dto.setPresentDistrictId(entity.getPresentAddress().getPoliceStation().getDistrict().getId());
            dto.setPresentDistrictName(entity.getPresentAddress().getPoliceStation().getDistrict().getName());

            dto.setPresentPoliceStationId(entity.getPresentAddress().getPoliceStation().getId());
            dto.setPresentPoliceStationName(entity.getPresentAddress().getPoliceStation().getName());

        }


        //Setting All Permanent Address Field

        if (entity.getPermanentAddress() != null) {

            dto.setPermanentAddressId(entity.getPermanentAddress().getId());
            dto.setPermanentAddressDetails(entity.getPermanentAddress().getDetails());
            dto.setPermanentAddressPostCode(entity.getPermanentAddress().getPostCode());

            dto.setPermanentCountryId(entity.getPermanentAddress().getPoliceStation().getDistrict().getDivision().getCountry().getId());
            dto.setPermanentCountryName(entity.getPermanentAddress().getPoliceStation().getDistrict().getDivision().getCountry().getName());
            dto.setPermanentCountryCode(entity.getPermanentAddress().getPoliceStation().getDistrict().getDivision().getCountry().getCode());

            dto.setPermanentDivisionId(entity.getPermanentAddress().getPoliceStation().getDistrict().getDivision().getId());
            dto.setPermanentDivisionName(entity.getPermanentAddress().getPoliceStation().getDistrict().getDivision().getName());

            dto.setPermanentDistrictId(entity.getPermanentAddress().getPoliceStation().getDistrict().getId());
            dto.setPermanentDistrictName(entity.getPermanentAddress().getPoliceStation().getDistrict().getName());

            dto.setPermanentPoliceStationId(entity.getPermanentAddress().getPoliceStation().getId());
            dto.setPermanentPoliceStationName(entity.getPermanentAddress().getPoliceStation().getName());

        }


        return dto;
    }

    public UserProfile toEntity(UserProfileRequestDTO dto) {
        UserProfile u = new UserProfile();

        User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("No User Found By this Id"));

        u.setUser(user);
        u.setName(dto.getName());
        u.setPhone(dto.getPhone());

        u.setHeadline(dto.getHeadline());
        u.setProfessionalSummary(dto.getProfessionalSummary());
        u.setBio(dto.getBio());

        u.setDateOfBirth(dto.getDateOfBirth());

        u.setGender(dto.getGender());
        u.setNationality(dto.getNationality());
        u.setReligion(dto.getReligion());
        u.setMaritalStatus(dto.getMaritalStatus());

        u.setFatherName(dto.getFatherName());
        u.setMotherName(dto.getMotherName());

        u.setNidNumber(dto.getNidNumber());
        u.setPassportNumber(dto.getPassportNumber());

        u.setGithubLink(dto.getGithubLink());
        u.setLinkedinLink(dto.getLinkedinLink());
        u.setPortfolioWebsite(dto.getPortfolioWebsite());

        u.setExpectedSalary(dto.getExpectedSalary());
        u.setCurrentSalary(dto.getCurrentSalary());

        u.setPreferredJobType(dto.getPreferredJobType());
        u.setPreferredWorkplace(dto.getPreferredWorkplace());

        u.setCareerObjective(dto.getCareerObjective());
        u.setFreelancerTitle(dto.getFreelancerTitle());

        // Present Address
        Address presentAddress;

        if (dto.getPresentAddressId() != null) {
            presentAddress = addressRepository.findById(dto.getPresentAddressId())
                    .orElseThrow(() -> new RuntimeException("Present Address Not Found"));
        } else {
            presentAddress = new Address();
        }

        presentAddress.setDetails(dto.getPresentAddressDetails());
        presentAddress.setPostCode(dto.getPresentAddressPostCode());

        presentAddress.setPoliceStation(
                policeStationRepository.findById(dto.getPresentAddressPoliceStationId())
                        .orElseThrow(() -> new RuntimeException("Police Station Not Found"))
        );

        u.setPresentAddress(presentAddress);

        // Permanent Address
        Address permanentAddress;

        if (dto.getPermanentAddressId() != null) {
            permanentAddress = addressRepository.findById(dto.getPermanentAddressId())
                    .orElseThrow(() -> new RuntimeException("Permanent Address Not Found"));
        } else {
            permanentAddress = new Address();
        }

        permanentAddress.setDetails(dto.getPermanentAddressDetails());
        permanentAddress.setPostCode(dto.getPermanentAddressPostCode());

        permanentAddress.setPoliceStation(
                policeStationRepository.findById(dto.getPermanentAddressPoliceStationId())
                        .orElseThrow(() -> new RuntimeException("Police Station Not Found"))
        );

        u.setPermanentAddress(permanentAddress);

        u.setProfileCompleted(isProfileCompleted(dto));

        return u;
    }

    //To check if profile is completed
    private boolean isProfileCompleted(UserProfileRequestDTO dto) {
        return dto.getUserId() != null
                && dto.getName() != null && !dto.getName().isBlank()
                && dto.getPhone() != null && !dto.getPhone().isBlank()

                && dto.getHeadline() != null && !dto.getHeadline().isBlank()
                && dto.getProfessionalSummary() != null && !dto.getProfessionalSummary().isBlank()
                && dto.getBio() != null && !dto.getBio().isBlank()

                && dto.getDateOfBirth() != null

                && dto.getGender() != null
                && dto.getNationality() != null && !dto.getNationality().isBlank()
                && dto.getReligion() != null && !dto.getReligion().isBlank()
                && dto.getMaritalStatus() != null && !dto.getMaritalStatus().isBlank()

                && dto.getFatherName() != null && !dto.getFatherName().isBlank()
                && dto.getMotherName() != null && !dto.getMotherName().isBlank()


                && dto.getExpectedSalary() != null


                && dto.getPreferredJobType() != null
                && dto.getPreferredWorkplace() != null

                && dto.getCareerObjective() != null && !dto.getCareerObjective().isBlank()


                && dto.getPresentAddressDetails() != null && !dto.getPresentAddressDetails().isBlank()
                && dto.getPresentAddressPostCode() != null && !dto.getPresentAddressPostCode().isBlank()
                && dto.getPresentAddressPoliceStationId() != null

                && dto.getPermanentAddressDetails() != null && !dto.getPermanentAddressDetails().isBlank()
                && dto.getPermanentAddressPostCode() != null && !dto.getPermanentAddressPostCode().isBlank()
                && dto.getPermanentAddressPoliceStationId() != null;
    }


}
