package com.wordbridge.project.serviceimpl;

import com.wordbridge.project.dto.mapper.PoliceStationMapper;
import com.wordbridge.project.dto.requestdto.PoliceStationRequestDTO;
import com.wordbridge.project.dto.responsedto.PoliceStationResponseDTO;
import com.wordbridge.project.entity.PoliceStation;
import com.wordbridge.project.repository.PoliceStationRepository;
import com.wordbridge.project.service.PoliceStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PoliceStationServiceImpl implements PoliceStationService {

    private final PoliceStationRepository policeStationRepository;


    private final PoliceStationMapper policeStationMapper;

    @Override
    public PoliceStationResponseDTO save(PoliceStationRequestDTO ps) {
        PoliceStation policeStation = policeStationMapper.toEntity(ps);
        PoliceStation saved = policeStationRepository.save(policeStation);


        return policeStationMapper.toDTO(saved);
    }

    @Override
    public List<PoliceStationResponseDTO> getAll() {
        return policeStationRepository.findAll().stream().map(policeStationMapper::toDTO).toList();
    }

    @Override
    public PoliceStationResponseDTO getById(Long id) {
        PoliceStation policeStation = policeStationRepository.findById(id).orElseThrow(() -> new RuntimeException("No Police Station Found"));

        return policeStationMapper.toDTO(policeStation);
    }

    @Override
    public void delete(Long id) {
        policeStationRepository.deleteById(id);
    }

    @Override
    public PoliceStationResponseDTO update(Long id, PoliceStationRequestDTO dto) {
        PoliceStation ps = policeStationMapper.toEntity(dto);
        ps.setId(id);
        PoliceStation updated = policeStationRepository.save(ps);
        return policeStationMapper.toDTO(updated);
    }


    @Override
    public List<PoliceStationResponseDTO> getPSByDistrictId(Long districtId) {
        List<PoliceStation> list = policeStationRepository.findByDistrictId(districtId);
        return list.stream().map(policeStationMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<PoliceStationResponseDTO> getPSByDistrictName(String districtName) {
        List<PoliceStation> list = policeStationRepository.findByDistrictName(districtName);
        return list.stream().map(policeStationMapper::toDTO).collect(Collectors.toList());
    }


}
