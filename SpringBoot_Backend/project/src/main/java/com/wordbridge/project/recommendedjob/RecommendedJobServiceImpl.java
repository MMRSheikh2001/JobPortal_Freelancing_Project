package com.wordbridge.project.recommendedjob;

import com.wordbridge.project.entity.UserProfile;
import com.wordbridge.project.entity.UserSkill;
import com.wordbridge.project.job.Job;
import com.wordbridge.project.job.JobMapper;
import com.wordbridge.project.job.JobRepository;
import com.wordbridge.project.job.JobResponseDTO;
import com.wordbridge.project.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendedJobServiceImpl implements RecommendedJobService {

    private final UserProfileRepository userProfileRepository;

    private final JobRepository jobRepository;

    private final JobMapper jobMapper;

    @Override
    public List<JobResponseDTO> getRecommendedJobs(Long userProfileId) {


        UserProfile userProfile =
                userProfileRepository.findById(userProfileId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "No User Profile found"
                                ));


        List<Job> jobs =
                jobRepository.findByIsActiveTrue();


        List<JobScore> recommendations =
                new ArrayList<>();


        for (Job job : jobs) {

            int score = calculateScore(
                    userProfile,
                    job
            );


            if (score >= 30) {

                recommendations.add(
                        new JobScore(job, score)
                );

            }

        }


        recommendations.sort(
                Comparator.comparing(
                        JobScore::score
                ).reversed()
        );


        return recommendations
                .stream()
                .limit(10)
                .map(item ->
                        jobMapper.toDTO(item.job())
                )
                .toList();

    }


    private int calculateScore(
            UserProfile userProfile,
            Job job) {

        int score = 0;


        // =========================================
        // Location
        // =========================================

        if (userProfile.getPresentAddress() != null
                && userProfile.getPresentAddress()
                .getPoliceStation() != null
                && job.getJobLocation() != null) {

            Long userPoliceStationId =
                    userProfile
                            .getPresentAddress()
                            .getPoliceStation()
                            .getId();

            Long jobPoliceStationId =
                    job.getJobLocation()
                            .getId();


            if (userPoliceStationId.equals(
                    jobPoliceStationId)) {

                score += 30;

            } else if (
                    userProfile
                            .getPresentAddress()
                            .getPoliceStation()
                            .getDistrict()
                            .getId()
                            .equals(
                                    job.getJobLocation()
                                            .getDistrict()
                                            .getId()
                            )
            ) {

                score += 20;

            }

        }


        // =========================================
        // Employment Type
        // =========================================

        if (userProfile.getPreferredJobType() != null
                && job.getEmploymentType() != null) {

            if (userProfile
                    .getPreferredJobType()
                    .name()
                    .equalsIgnoreCase(
                            job.getEmploymentType().name()
                    )) {

                score += 15;

            }

        }


        // =========================================
        // Workplace Type
        // =========================================

        if (userProfile.getPreferredWorkplace() != null
                && job.getWorkPlaceType() != null) {

            if (userProfile
                    .getPreferredWorkplace()
                    .name()
                    .equalsIgnoreCase(
                            job.getWorkPlaceType().name()
                    )) {

                score += 15;

            }

        }


        // =========================================
        // Skills
        // =========================================

        if (userProfile.getUserSkills() != null) {

            for (UserSkill userSkill :
                    userProfile.getUserSkills()) {

                if (userSkill.getSkill() == null) {

                    continue;

                }

                String skillName =
                        userSkill
                                .getSkill()
                                .getName();

                if (skillName == null
                        || skillName.isBlank()) {

                    continue;

                }


                String jobText =
                        (
                                safe(job.getTitle())
                                        + " "
                                        + safe(job.getJobDescription())
                                        + " "
                                        + safe(job.getJobResponsibilities())
                                        + " "
                                        + safe(job.getEducationalRequirements())
                                        + " "
                                        + safe(job.getExperienceRequirements())
                                        + " "
                                        + safe(job.getAdditionalRequirements())
                        ).toLowerCase();


                if (jobText.contains(
                        skillName.toLowerCase().trim()
                )) {

                    score += 10;

                }

            }

        }


        return Math.min(score, 100);
    }


    private String safe(String value) {

        return value == null
                ? ""
                : value;

    }


    private record JobScore(
            Job job,
            int score
    ) {
    }

}
