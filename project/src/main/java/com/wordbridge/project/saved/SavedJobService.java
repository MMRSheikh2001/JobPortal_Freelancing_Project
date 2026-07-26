package com.wordbridge.project.saved;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SavedJobService {
    SavedJobResponseDTO saveJob(Long userId, Long jobId);

    void unsaveJob(Long userId, Long jobId);

    List<SavedJobResponseDTO> getSavedJobs(Long userId);

    boolean isJobSaved(Long userId, Long jobId);

    Long countByUserId(Long userId);
}
