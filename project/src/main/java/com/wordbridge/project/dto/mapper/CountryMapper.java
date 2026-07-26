package com.wordbridge.project.dto.mapper;

import com.wordbridge.project.dto.requestdto.CountryRequestDTO;
import com.wordbridge.project.dto.responsedto.CountryResponseDTO;
import com.wordbridge.project.entity.Country;
import org.springframework.stereotype.Component;

@Component
public class CountryMapper {


    public Country toEntity(CountryRequestDTO dto) {

        Country country = new Country();

        country.setName(dto.getCountryName());
        country.setCode(dto.getCountryCode());

        return country;
    }

    public CountryResponseDTO toDTO(Country country) {

        CountryResponseDTO dto = new CountryResponseDTO();

        dto.setCountryId(country.getId());
        dto.setCountryName(country.getName());
        dto.setCountryCode(country.getCode());

        return dto;
    }
}
