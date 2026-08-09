package com.wordbridge.project.saved;

import com.wordbridge.project.entity.User;
import com.wordbridge.project.job.Job;
import com.wordbridge.project.job.JobRepository;
import com.wordbridge.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SavedJobServiceImpl implements SavedJobService {
    private final SavedJobRepository savedJobRepository;
    private final SavedJobMapper savedJobMapper;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;


    @Override
    public SavedJobResponseDTO saveJob(Long userId, Long jobId) {
        if (savedJobRepository.existsByUserIdAndJobId(userId, jobId)) {
            throw new RuntimeException("Job is already saved");
        }
        SavedJob savedJob = new SavedJob();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("No user found"));
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("No Job found"));
        savedJob.setUser(user);
        savedJob.setJob(job);

        return savedJobMapper.toDTO(savedJobRepository.save(savedJob));
    }

    @Override
    public void unsaveJob(Long userId, Long jobId) {
        if (!savedJobRepository.existsByUserIdAndJobId(userId, jobId)) {
            throw new RuntimeException("Job is not saved");
        }
        SavedJob savedJob = savedJobRepository.findByUserIdAndJobId(userId, jobId)
                .orElseThrow(() -> new RuntimeException("No Saved Job found"));
        savedJobRepository.delete(savedJob);

    }

    @Override
    public List<SavedJobResponseDTO> getSavedJobs(Long userId) {
        return savedJobRepository.findByUserIdOrderByCreatedAtDesc(userId).stream().map(savedJobMapper::toDTO).toList();
    }

    @Override
    public boolean isJobSaved(Long userId, Long jobId) {
        return savedJobRepository.existsByUserIdAndJobId(userId, jobId);
    }

    @Override
    public Long countByUserId(Long userId) {
        return savedJobRepository.countByUserId(userId);
    }
}
