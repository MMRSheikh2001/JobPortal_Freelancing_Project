package com.wordbridge.project.serviceimpl;

import com.wordbridge.project.dto.mapper.SkillMapper;
import com.wordbridge.project.dto.requestdto.SkillRequestDTO;
import com.wordbridge.project.dto.responsedto.SkillResponseDTO;
import com.wordbridge.project.entity.Skill;
import com.wordbridge.project.repository.SkillRepository;
import com.wordbridge.project.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {


    private final SkillRepository skillRepository;

    private final SkillMapper skillMapper;


    @Override
    public SkillResponseDTO save(SkillRequestDTO s) {
        Skill skill = skillMapper.toEntity(s);
        return skillMapper.toDTO(skillRepository.save(skill));
    }

    @Override
    public List<SkillResponseDTO> getAll() {
        return skillRepository.findAll().stream().map(skillMapper::toDTO).toList();
    }

    @Override
    public SkillResponseDTO findById(Long id) {
        Skill skill = skillRepository.findById(id).orElseThrow(() -> new RuntimeException("No Skill Found By This Id"));
        return skillMapper.toDTO(skill);
    }

    @Override
    public void delete(Long id) {
        skillRepository.deleteById(id);
    }

    @Override
    public SkillResponseDTO update(Long id, SkillRequestDTO s) {
        Skill idSearch = skillRepository.findById(id).orElseThrow(() -> new RuntimeException("No Skill Found"));
        Skill update = skillMapper.toEntity(s);
        update.setId(idSearch.getId());

        return skillMapper.toDTO(skillRepository.save(update));
    }

    @Override
    public List<SkillResponseDTO> getSkillByCategoryId(Long categoryId) {
        List<Skill> list = skillRepository.findByCategoryId(categoryId);
        return list.stream().map(skillMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<SkillResponseDTO> getSkillByCategoryName(String categoryName) {
        List<Skill> list = skillRepository.findByCategoryName(categoryName);
        return list.stream().map(skillMapper::toDTO).collect(Collectors.toList());
    }


}
