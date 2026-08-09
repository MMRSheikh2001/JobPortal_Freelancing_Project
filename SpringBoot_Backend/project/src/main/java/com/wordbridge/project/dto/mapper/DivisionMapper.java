package com.wordbridge.project.dto.mapper;

import com.wordbridge.project.dto.requestdto.DivisionRequestDTO;
import com.wordbridge.project.dto.responsedto.DivisionResponseDTO;
import com.wordbridge.project.entity.Division;
import com.wordbridge.project.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class DivisionMapper {

    private final CountryRepository countryRepository;

    public Division toEntity(DivisionRequestDTO dto) {

        Division division = new Division();

        division.setCountry(countryRepository.findById(dto.getCountryId()).orElseThrow(() -> new RuntimeException("Country Not Found")));
        division.setName(dto.getDivisionName());

        return division;
    }

    public DivisionResponseDTO toDTO(Division entity) {

        DivisionResponseDTO dto = new DivisionResponseDTO();

        dto.setDivisionId(entity.getId());
        dto.setDivisionName(entity.getName());

        dto.setCountryId(entity.getCountry().getId());
        dto.setCountryName(entity.getCountry().getName());
        dto.setCountryCode(entity.getCountry().getCode());

        return dto;
    }
}
