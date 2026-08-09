package com.wordbridge.project.serviceimpl;


import com.wordbridge.project.dto.mapper.DivisionMapper;
import com.wordbridge.project.dto.requestdto.DivisionRequestDTO;
import com.wordbridge.project.dto.responsedto.DivisionResponseDTO;

import com.wordbridge.project.entity.Division;
import com.wordbridge.project.repository.DivisionRepository;
import com.wordbridge.project.service.DivisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DivisionServiceImpl implements DivisionService {

    private final DivisionRepository divisionRepository;



    private final DivisionMapper divisionMapper;


    @Override
    public DivisionResponseDTO save(DivisionRequestDTO dto) {
        Division division = divisionMapper.toEntity(dto);
        Division savedDivision = divisionRepository.save(division);
        return divisionMapper.toDTO(savedDivision);
    }

    @Override
    public List<DivisionResponseDTO> getAll() {
        return divisionRepository.findAll().stream().map(divisionMapper::toDTO).toList();
    }

    @Override
    public DivisionResponseDTO getById(Long id) {
        Division division = divisionRepository.findById(id).orElseThrow(() -> new RuntimeException("No Division Found"));
        return divisionMapper.toDTO(division);
    }

    @Override
    public DivisionResponseDTO update(Long id, DivisionRequestDTO dto) {
        Division division = divisionMapper.toEntity(dto);
        division.setId(id);
        Division updatedDivision = divisionRepository.save(division);
        return divisionMapper.toDTO(updatedDivision);
    }

    @Override
    public void delete(Long id) {
        divisionRepository.deleteById(id);
    }


    @Override
    public List<DivisionResponseDTO> getDivisionByCountryId(Long countryId) {
        List<Division> list = divisionRepository.findByCountryId(countryId);
        return list.stream().map(divisionMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<DivisionResponseDTO> getDivisionByCountryName(String countryName) {
        List<Division> list = divisionRepository.findByCountryName(countryName);
        return list.stream().map(divisionMapper::toDTO).collect(Collectors.toList());
    }


}
