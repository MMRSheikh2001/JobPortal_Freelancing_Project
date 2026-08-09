package com.wordbridge.project.job;

import com.wordbridge.project.dto.requestdto.UserSkillRequestDTO;
import com.wordbridge.project.dto.responsedto.UserSkillResponseDTO;
import com.wordbridge.project.enums.EmploymentType;
import com.wordbridge.project.enums.WorkPlaceType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface JobService {

    JobResponseDTO save(JobRequestDTO dto);

    List<JobResponseDTO> getAll();

    JobResponseDTO findById(Long id);

    JobResponseDTO update(
            Long id,
            JobRequestDTO dto
    );

    void delete(Long id);


    //All Repository Methods


    //Basic CRUD
    List<JobResponseDTO> findByCompanyProfileId(Long companyProfileId);

    List<JobResponseDTO> findByCompanyProfileUserId(Long userId);

    Long countByCompanyProfileId(Long companyProfileId);

    void deleteByCompanyProfileId(Long companyProfileId);


    //Active Jobs
    List<JobResponseDTO> findByIsActiveTrue();


    //Active Company Job
    List<JobResponseDTO> findByCompanyProfileIdAndIsActiveTrue(
            Long companyProfileId
    );

    //Latest Jobs for Home page
    List<JobResponseDTO> findTop10ByIsActiveTrueOrderByCreatedAtDesc();

    List<JobResponseDTO> findTop20ByIsActiveTrueOrderByCreatedAtDesc();


    //Company Jobs Count
    Long countByCompanyProfileIdAndIsActiveTrue(
            Long companyProfileId
    );

    Long countByCompanyProfileIdAndIsActiveFalse(
            Long companyProfileId
    );

    //Search method
    List<JobResponseDTO> search(JobSearchRequestDTO dto);

    //Deactivate/activate a job
    JobResponseDTO changeJobStatus(Long id);

    Long countByIsActiveTrue();
    Long countByIsActiveFalse();


}
