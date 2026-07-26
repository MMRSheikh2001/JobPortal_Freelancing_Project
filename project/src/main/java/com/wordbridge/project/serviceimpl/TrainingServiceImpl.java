package com.wordbridge.project.serviceimpl;

import com.wordbridge.project.dto.mapper.TrainingMapper;
import com.wordbridge.project.dto.requestdto.TrainingRequestDTO;
import com.wordbridge.project.dto.responsedto.TrainingResponseDTO;
import com.wordbridge.project.entity.Training;
import com.wordbridge.project.repository.TrainingRepository;
import com.wordbridge.project.service.TrainingService;
import com.wordbridge.project.util.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    private final TrainingRepository trainingRepository;
    private final TrainingMapper trainingMapper;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public TrainingResponseDTO save(TrainingRequestDTO dto, MultipartFile file) {
        Training training = trainingMapper.toEntity(dto);

        if (file != null && !file.isEmpty()) {
            String fileName = fileStorageService.uploadFile(file,
                    training.getUserProfile().getUser().getEmail(),
                    "trainings");
            training.setCertificateFile(fileName);

        }

        return trainingMapper.toDTO(trainingRepository.save(training));
    }

    @Override
    public List<TrainingResponseDTO> getAll() {
        return trainingRepository.findAll().stream().map(trainingMapper::toDTO).toList();
    }

    @Override
    public TrainingResponseDTO findById(Long id) {
        Training training = trainingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Training Found by this id"));
        return trainingMapper.toDTO(training);
    }

    @Override
    @Transactional
    public TrainingResponseDTO update(Long id, TrainingRequestDTO dto, MultipartFile file) {
        Training exist = trainingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Training Found by this id"));

        Training training = trainingMapper.toEntity(dto);

        if (file != null && !file.isEmpty()) {


            String fileName = fileStorageService.uploadFile(file,
                    training.getUserProfile().getUser().getEmail(),
                    "trainings");
            training.setCertificateFile(fileName);

            if (exist.getCertificateFile() != null) {
                fileStorageService.deleteFile(
                        "trainings",
                        exist.getCertificateFile()
                );
            }

        } else {
            training.setCertificateFile(exist.getCertificateFile());
        }
        training.setId(exist.getId());
        training.setCreatedAt(exist.getCreatedAt());

        return trainingMapper.toDTO(trainingRepository.save(training));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Training training = trainingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No training found"));

        if (training.getCertificateFile() != null) {
            fileStorageService.deleteFile(
                    "trainings",
                    training.getCertificateFile()
            );
        }

        trainingRepository.delete(training);
    }

    @Override
    @Transactional
    public void deleteFile(Long id) {
        Training training = trainingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Training Not Found"));

        if (training.getCertificateFile() != null) {

            fileStorageService.deleteFile("trainings",
                    training.getCertificateFile()

            );

            training.setCertificateFile(null);

            trainingRepository.save(training);
        }
    }

    @Override
    public List<TrainingResponseDTO> findByUserProfileId(Long userProfileId) {
        return trainingRepository.findByUserProfileId(userProfileId).stream().map(trainingMapper::toDTO).toList();
    }

    @Override
    public Long countByUserProfileId(Long userProfileId) {
        return trainingRepository.countByUserProfileId(userProfileId);
    }
}
