package com.wordbridge.project.serviceimpl;

import com.wordbridge.project.dto.mapper.ExtracurricularMapper;
import com.wordbridge.project.dto.requestdto.ExtracurricularRequestDTO;
import com.wordbridge.project.dto.responsedto.ExtracurricularResponseDTO;
import com.wordbridge.project.entity.Extracurricular;
import com.wordbridge.project.repository.ExtracurricularRepository;
import com.wordbridge.project.service.ExtracurricularService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExtracurricularServiceImpl implements ExtracurricularService {
    private final ExtracurricularRepository extracurricularRepository;
    private final ExtracurricularMapper extracurricularMapper;

    @Override
    public ExtracurricularResponseDTO save(ExtracurricularRequestDTO dto) {
        Extracurricular extracurricular = extracurricularMapper.toEntity(dto);
        return extracurricularMapper.toDTO(extracurricularRepository.save(extracurricular));
    }

    @Override
    public List<ExtracurricularResponseDTO> getAll() {
        return extracurricularRepository.findAll().stream().map(extracurricularMapper::toDTO).toList();
    }

    @Override
    public ExtracurricularResponseDTO findById(Long id) {
        Extracurricular extracurricular = extracurricularRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Extracurricular Found"));
        return extracurricularMapper.toDTO(extracurricular);
    }

    @Override
    public ExtracurricularResponseDTO update(Long id, ExtracurricularRequestDTO dto) {

        Extracurricular existing = extracurricularRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User Extracurricular Not Found"));

        Extracurricular extracurricular = extracurricularMapper.toEntity(dto);
        extracurricular.setId(existing.getId());

        return extracurricularMapper.toDTO(extracurricularRepository.save(extracurricular));
    }

    @Override
    public void delete(Long id) {
        Extracurricular extracurricular = extracurricularRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Extracurricular Found"));
        extracurricularRepository.delete(extracurricular);

    }

    @Override
    public List<ExtracurricularResponseDTO> findByUserProfileId(Long userProfileId) {
        return extracurricularRepository.findByUserProfileId(userProfileId).stream().map(extracurricularMapper::toDTO).toList();
    }

    @Override
    public Long countByUserProfileId(Long userProfileId) {
        return extracurricularRepository.countByUserProfileId(userProfileId);
    }
}
