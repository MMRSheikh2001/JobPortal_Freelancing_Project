package com.wordbridge.project.dto.mapper;

import com.wordbridge.project.dto.requestdto.CompanyProfileRequestDTO;
import com.wordbridge.project.dto.responsedto.CompanyProfileResponseDTO;
import com.wordbridge.project.entity.Address;
import com.wordbridge.project.entity.CompanyProfile;
import com.wordbridge.project.entity.User;
import com.wordbridge.project.repository.AddressRepository;
import com.wordbridge.project.repository.PoliceStationRepository;
import com.wordbridge.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CompanyProfileMapper {

    private final AddressRepository addressRepository;


    private final PoliceStationRepository policeStationRepository;


    private final UserRepository userRepository;

    public CompanyProfileResponseDTO toDTO(CompanyProfile cp) {
        CompanyProfileResponseDTO dto = new CompanyProfileResponseDTO();
        dto.setId(cp.getId());
        dto.setUserId(cp.getUser().getId());
        dto.setUserEmail(cp.getUser().getEmail());

        dto.setName(cp.getName());
        dto.setPhone(cp.getPhone());
        dto.setCompanyEmail(cp.getCompanyEmail());
        dto.setImage(cp.getImage());

        dto.setCompanyDescription(cp.getCompanyDescription());
        dto.setCompanyWebsite(cp.getCompanyWebsite());
        dto.setIndustry(cp.getIndustry());

        dto.setFoundedYear(cp.getFoundedYear());
        dto.setTradeLicenseNumber(cp.getTradeLicenseNumber());

        dto.setCreatedAt(cp.getCreatedAt());
        dto.setUpdatedAt(cp.getUpdatedAt());

//Location Code Start

        if (cp.getLocation() != null) {
            dto.setLocationId(cp.getLocation().getId());
            dto.setLocationDetails(cp.getLocation().getDetails());
            dto.setLocationPostCode(cp.getLocation().getPostCode());

            dto.setLocationCountryId(cp.getLocation().getPoliceStation().getDistrict().getDivision().getCountry().getId());
            dto.setLocationCountryName(cp.getLocation().getPoliceStation().getDistrict().getDivision().getCountry().getName());
            dto.setLocationCountryCode(cp.getLocation().getPoliceStation().getDistrict().getDivision().getCountry().getCode());

            dto.setLocationDivisionId(cp.getLocation().getPoliceStation().getDistrict().getDivision().getId());
            dto.setLocationDivisionName(cp.getLocation().getPoliceStation().getDistrict().getDivision().getName());

            dto.setLocationDistrictId(cp.getLocation().getPoliceStation().getDistrict().getId());
            dto.setLocationDistrictName(cp.getLocation().getPoliceStation().getDistrict().getName());

            dto.setLocationPoliceStationId(cp.getLocation().getPoliceStation().getId());
            dto.setLocationPoliceStationName(cp.getLocation().getPoliceStation().getName());


        }


        return dto;
    }

    public CompanyProfile toEntity(CompanyProfileRequestDTO dto) {
        CompanyProfile cp = new CompanyProfile();

        User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("No User Found By this Id"));
        cp.setUser(user);

        cp.setName(dto.getName());
        cp.setPhone(dto.getPhone());
        cp.setCompanyEmail(dto.getCompanyEmail());

        cp.setCompanyDescription(dto.getCompanyDescription());
        String website = dto.getCompanyWebsite();
        if (!website.startsWith("http://") &&
                !website.startsWith("https://")) {

            website = "https://" + dto.getCompanyWebsite();
        }

        cp.setCompanyWebsite(website);
        cp.setIndustry(dto.getIndustry());

        cp.setFoundedYear(dto.getFoundedYear());
        cp.setTradeLicenseNumber(dto.getTradeLicenseNumber());

        //Location save or update  Code

        Address location;

        if (dto.getLocationId() != null) {
            location = addressRepository.findById(dto.getLocationId())
                    .orElseThrow(() -> new RuntimeException("Location Not Found"));
        } else {
            location = new Address();
        }

        location.setDetails(dto.getLocationDetails());
        location.setPostCode(dto.getLocationPostCode());

        location.setPoliceStation(
                policeStationRepository.findById(dto.getLocationPoliceStationId())
                        .orElseThrow(() -> new RuntimeException("Police Station Not Found"))
        );

        cp.setLocation(location);


        return cp;
    }


}


