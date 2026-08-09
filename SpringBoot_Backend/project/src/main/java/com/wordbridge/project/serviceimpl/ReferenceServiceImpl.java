package com.wordbridge.project.serviceimpl;

import com.wordbridge.project.dto.mapper.ReferenceMapper;
import com.wordbridge.project.dto.requestdto.ReferenceRequestDTO;
import com.wordbridge.project.dto.responsedto.ReferenceResponseDTO;
import com.wordbridge.project.entity.Reference;
import com.wordbridge.project.repository.ReferenceRepository;
import com.wordbridge.project.service.ReferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReferenceServiceImpl implements ReferenceService {

    private final ReferenceRepository referenceRepository;
    private final ReferenceMapper referenceMapper;


    @Override
    public ReferenceResponseDTO save(ReferenceRequestDTO dto) {
        Reference reference = referenceMapper.toEntity(dto);
        return referenceMapper.toDTO(referenceRepository.save(reference));
    }

    @Override
    public List<ReferenceResponseDTO> getAll() {
        return referenceRepository.findAll().stream().map(referenceMapper::toDTO).toList();
    }

    @Override
    public ReferenceResponseDTO findById(Long id) {
        Reference reference = referenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Reference found"));
        return referenceMapper.toDTO(reference);
    }

    @Override
    public ReferenceResponseDTO update(Long id, ReferenceRequestDTO dto) {

        Reference existing = referenceRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User Reference Not Found"));

        Reference reference = referenceMapper.toEntity(dto);
        reference.setId(existing.getId());

        return referenceMapper.toDTO(referenceRepository.save(reference));
    }

    @Override
    public void delete(Long id) {
        Reference reference = referenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Reference found"));
        referenceRepository.delete(reference);

    }

    @Override
    public List<ReferenceResponseDTO> findByUserProfileId(Long userProfileId) {
        return referenceRepository.findByUserProfileId(userProfileId).stream().map(referenceMapper::toDTO).toList();
    }

    @Override
    public Long countByUserProfileId(Long userProfileId) {
        return referenceRepository.countByUserProfileId(userProfileId);
    }
}
