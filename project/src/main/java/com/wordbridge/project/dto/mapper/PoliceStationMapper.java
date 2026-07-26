package com.wordbridge.project.dto.mapper;

import com.wordbridge.project.dto.requestdto.PoliceStationRequestDTO;
import com.wordbridge.project.dto.responsedto.PoliceStationResponseDTO;
import com.wordbridge.project.entity.PoliceStation;
import com.wordbridge.project.repository.DistrictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PoliceStationMapper {


    private final DistrictRepository districtRepository;


    public PoliceStation toEntity(PoliceStationRequestDTO dto) {

        PoliceStation policeStation = new PoliceStation();

        policeStation.setDistrict(districtRepository.findById(dto.getDistrictId()).orElseThrow(() -> new RuntimeException("District Not Found")));
        policeStation.setName(dto.getPoliceStationName());

        return policeStation;
    }

    public PoliceStationResponseDTO toDTO(PoliceStation entity) {

        PoliceStationResponseDTO dto = new PoliceStationResponseDTO();

        dto.setPoliceStationId(entity.getId());
        dto.setPoliceStationName(entity.getName());

        dto.setDistrictId(entity.getDistrict().getId());
        dto.setDistrictName(entity.getDistrict().getName());

        dto.setDivisionId(entity.getDistrict().getDivision().getId());
        dto.setDivisionName(entity.getDistrict().getDivision().getName());

        dto.setCountryId(entity.getDistrict().getDivision().getCountry().getId());
        dto.setCountryName(entity.getDistrict().getDivision().getCountry().getName());
        dto.setCountryCode(entity.getDistrict().getDivision().getCountry().getCode());

        return dto;
    }


}
