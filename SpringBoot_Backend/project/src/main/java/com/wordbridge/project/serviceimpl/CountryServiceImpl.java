package com.wordbridge.project.serviceimpl;

import com.wordbridge.project.dto.mapper.CountryMapper;
import com.wordbridge.project.dto.requestdto.CountryRequestDTO;
import com.wordbridge.project.dto.responsedto.CountryResponseDTO;
import com.wordbridge.project.entity.Country;
import com.wordbridge.project.repository.CountryRepository;
import com.wordbridge.project.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;


    private final CountryMapper countryMapper;


    @Override
    public CountryResponseDTO save(CountryRequestDTO c) {
        Country country = countryMapper.toEntity(c);
        Country savedCountry = countryRepository.save(country);


        return countryMapper.toDTO(savedCountry);
    }

    @Override
    public List<CountryResponseDTO> getAll() {
        return countryRepository.findAll().stream().map(countryMapper::toDTO).toList();
    }

    @Override
    public CountryResponseDTO findById(Long id) {
        Country country = countryRepository.findById(id).orElseThrow(() -> new RuntimeException("Country Not found with this id"));
        return countryMapper.toDTO(country);
    }

    @Override
    public void delete(Long id) {
        countryRepository.deleteById(id);
    }

    @Override
    public CountryResponseDTO update(Long id, CountryRequestDTO dto) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Country Not Found"));

        country.setName(dto.getCountryName());
        country.setCode(dto.getCountryCode());

        Country updatedCountry = countryRepository.save(country);

        return countryMapper.toDTO(updatedCountry);
    }
}
