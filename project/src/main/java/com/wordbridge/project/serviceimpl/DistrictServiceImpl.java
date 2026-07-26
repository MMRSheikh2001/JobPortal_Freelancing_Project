package com.wordbridge.project.serviceimpl;

import com.wordbridge.project.dto.mapper.DistrictMapper;
import com.wordbridge.project.dto.requestdto.DistrictRequestDTO;
import com.wordbridge.project.dto.responsedto.DistrictResponseDTO;
import com.wordbridge.project.entity.District;
import com.wordbridge.project.repository.DistrictRepository;
import com.wordbridge.project.service.DistrictService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DistrictServiceImpl implements DistrictService {

    private final DistrictRepository districtRepository;


    private final DistrictMapper districtMapper;


    @Override
    public DistrictResponseDTO save(DistrictRequestDTO d) {
        District district = districtMapper.toEntity(d);
        District savedDistrict = districtRepository.save(district);
        return districtMapper.toDTO(savedDistrict);
    }

    @Override
    public List<DistrictResponseDTO> getAll() {
        return districtRepository.findAll().stream().map(districtMapper::toDTO).toList();
    }

    @Override
    public DistrictResponseDTO findById(Long id) {
        District district = districtRepository.findById(id).orElseThrow(() -> new RuntimeException("No District Found"));
        return districtMapper.toDTO(district);
    }

    @Override
    public void delete(Long id) {
        districtRepository.deleteById(id);
    }

    @Override
    public DistrictResponseDTO update(Long id, DistrictRequestDTO d) {
        District district = districtMapper.toEntity(d);
        district.setId(id);
        District updatedDistrict = districtRepository.save(district);
        return districtMapper.toDTO(updatedDistrict);
    }

    @Override
    public List<DistrictResponseDTO> getDistrictByDivisionId(Long divisionId) {
        List<District> list = districtRepository.findByDivisionId(divisionId);
        return list.stream().map(districtMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<DistrictResponseDTO> getDistrictByDivisionName(String divisionName) {
        List<District> list = districtRepository.findByDivisionName(divisionName);
        return list.stream().map(districtMapper::toDTO).collect(Collectors.toList());
    }


}
