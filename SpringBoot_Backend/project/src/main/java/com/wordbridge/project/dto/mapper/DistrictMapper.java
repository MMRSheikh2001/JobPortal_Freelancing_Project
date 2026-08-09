package com.wordbridge.project.dto.mapper;


import com.wordbridge.project.dto.requestdto.DistrictRequestDTO;
import com.wordbridge.project.dto.responsedto.DistrictResponseDTO;
import com.wordbridge.project.entity.District;
import com.wordbridge.project.repository.DivisionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DistrictMapper {

    private final DivisionRepository divisionRepository;


    public District toEntity(DistrictRequestDTO dto) {

        District district = new District();

        district.setDivision(divisionRepository.findById(dto.getDivisionId()).orElseThrow(() -> new RuntimeException("Division Not Found")));
        district.setName(dto.getDistrictName());

        return district;
    }

    public DistrictResponseDTO toDTO(District entity) {

        DistrictResponseDTO dto = new DistrictResponseDTO();

        dto.setDistrictId(entity.getId());
        dto.setDistrictName(entity.getName());

        dto.setDivisionId(entity.getDivision().getId());
        dto.setDivisionName(entity.getDivision().getName());

        dto.setCountryId(entity.getDivision().getCountry().getId());
        dto.setCountryName(entity.getDivision().getCountry().getName());
        dto.setCountryCode(entity.getDivision().getCountry().getCode());

        return dto;
    }


}
