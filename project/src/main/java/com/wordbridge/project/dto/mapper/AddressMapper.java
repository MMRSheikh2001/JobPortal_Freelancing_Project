package com.wordbridge.project.dto.mapper;

import com.wordbridge.project.dto.requestdto.AddressRequestDTO;
import com.wordbridge.project.dto.responsedto.AddressResponseDTO;
import com.wordbridge.project.entity.Address;
import com.wordbridge.project.repository.PoliceStationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressMapper {

    private final PoliceStationRepository policeStationRepository;


    public Address toEntity(AddressRequestDTO dto) {

        Address address = new Address();
        address.setDetails(dto.getAddressDetails());
        address.setPostCode(dto.getPostCode());
        address.setPoliceStation(policeStationRepository.findById(dto.getPoliceStationId()).orElseThrow(() -> new RuntimeException("Police Station Not Found")));


        return address;
    }

    public AddressResponseDTO toDTO(Address entity) {

        AddressResponseDTO dto = new AddressResponseDTO();


        dto.setAddressId(entity.getId());
        dto.setAddressDetails(entity.getDetails());
        dto.setPostCode(entity.getPostCode());

        dto.setPoliceStationId(entity.getPoliceStation().getId());
        dto.setPoliceStationName(entity.getPoliceStation().getName());

        dto.setDistrictId(entity.getPoliceStation().getDistrict().getId());
        dto.setDistrictName(entity.getPoliceStation().getDistrict().getName());

        dto.setDivisionId(entity.getPoliceStation().getDistrict().getDivision().getId());
        dto.setDivisionName(entity.getPoliceStation().getDistrict().getDivision().getName());

        dto.setCountryId(entity.getPoliceStation().getDistrict().getDivision().getCountry().getId());
        dto.setCountryName(entity.getPoliceStation().getDistrict().getDivision().getCountry().getName());
        dto.setCountryCode(entity.getPoliceStation().getDistrict().getDivision().getCountry().getCode());

        return dto;
    }


}
