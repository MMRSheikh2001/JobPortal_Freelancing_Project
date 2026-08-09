package com.wordbridge.project.job;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {

    //Basic CRUD
    List<Job> findByCompanyProfileId(Long companyProfileId);

    List<Job> findByCompanyProfileUserId(Long userId);

    Long countByCompanyProfileId(Long companyProfileId);

    void deleteByCompanyProfileId(Long companyProfileId);


    //Active Jobs
    List<Job> findByIsActiveTrue();


    //Active Company Job
    List<Job> findByCompanyProfileIdAndIsActiveTrue(
            Long companyProfileId
    );

    //Latest Jobs for Home page
    List<Job> findTop10ByIsActiveTrueOrderByCreatedAtDesc();

    List<Job> findTop20ByIsActiveTrueOrderByCreatedAtDesc();


    //Company Jobs Count
    Long countByCompanyProfileIdAndIsActiveTrue(
            Long companyProfileId
    );

    Long countByCompanyProfileIdAndIsActiveFalse(
            Long companyProfileId
    );

    Long countByIsActiveTrue();
    Long countByIsActiveFalse();


}




