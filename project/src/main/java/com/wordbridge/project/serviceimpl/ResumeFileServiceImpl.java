package com.wordbridge.project.serviceimpl;

import com.wordbridge.project.dto.mapper.ResumeMapper;
import com.wordbridge.project.dto.requestdto.ResumeRequestDTO;
import com.wordbridge.project.dto.responsedto.ResumeFileResponseDTO;
import com.wordbridge.project.entity.Resume;
import com.wordbridge.project.entity.UserProfile;
import com.wordbridge.project.repository.ResumeRepository;
import com.wordbridge.project.repository.UserRepository;
import com.wordbridge.project.service.ResumeFileService;
import com.wordbridge.project.util.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeFileServiceImpl implements ResumeFileService {
    private final ResumeRepository resumeRepository;
    private final ResumeMapper resumeMapper;
    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;


    @Override
    @Transactional
    public ResumeFileResponseDTO save(ResumeRequestDTO dto, MultipartFile cv) {
        Resume resume ;
        if (resumeRepository.existsByUserProfileId(dto.getUserProfileId())) {
            resume = resumeRepository.findByUserProfileId(dto.getUserProfileId())
                    .orElseThrow(() -> new RuntimeException("No Resume found"));
        }else {
            resume= resumeMapper.toEntity(dto);
        }



        String email = userRepository.findById(resume.getUserProfile().getUser().getId()).orElseThrow(() -> new RuntimeException("No User Found")).getEmail();
        if (cv != null && !cv.isEmpty()) {
            String fileName = fileStorageService.uploadFile(cv, email, "resumes");
            if (resume.getFileName() != null) {
                fileStorageService.deleteFile("resumes", resume.getFileName());
            }
            resume.setFileName(fileName);

        }
        Resume saved = resumeRepository.save(resume);

        return resumeMapper.toDTO(saved);
    }

    @Override
    public List<ResumeFileResponseDTO> findAll() {
        return resumeRepository.findAll().stream().map(resumeMapper::toDTO).toList();
    }

    @Override
    public ResumeFileResponseDTO getById(Long id) {
        Resume resume=resumeRepository.findById(id)
                .orElseThrow(()->new RuntimeException("No Resume found"));
        return resumeMapper.toDTO(resume);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Resume resume=resumeRepository.findById(id)
                .orElseThrow(()->new RuntimeException("No Resume found"));
        if (resume.getFileName() != null) {
            fileStorageService.deleteFile("resumes", resume.getFileName());
        }
        resumeRepository.delete(resume);


    }

    @Override
    public ResumeFileResponseDTO findByUserProfileId(Long userProfileId) {
      Resume  resume = resumeRepository.findByUserProfileId(userProfileId)
                .orElseThrow(() -> new RuntimeException("No Resume found"));
        return resumeMapper.toDTO(resume);
    }

    @Override
    @Transactional
    public void deleteByUserProfileId(Long userProfileId) {
        Resume resume = resumeRepository.findByUserProfileId(userProfileId)
                .orElseThrow(() -> new RuntimeException("Resume Not Found"));

        if (resume.getFileName() != null) {

            fileStorageService.deleteFile("resumes",
                    resume.getFileName()

            );

            resume.setFileName(null);

            resumeRepository.save(resume);
        }
    }

    @Override
    public Boolean existsByUserProfileId(Long userProfileId) {
        return resumeRepository.existsByUserProfileId(userProfileId);
    }
}
