package com.wordbridge.project.recommendedjob;

import com.wordbridge.project.job.JobResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RecommendedJobService {

    List<JobResponseDTO> getRecommendedJobs(Long userProfileId);


}
