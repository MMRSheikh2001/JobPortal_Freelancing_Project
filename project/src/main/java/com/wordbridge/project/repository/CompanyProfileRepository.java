package com.wordbridge.project.repository;

import com.wordbridge.project.entity.CompanyProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyProfileRepository extends JpaRepository<CompanyProfile, Long>,
        JpaSpecificationExecutor<CompanyProfile> {

    Optional<CompanyProfile> findByUserId(Long id);

    boolean existsByUserId(Long userId);

    List<CompanyProfile> findByLocationPoliceStationId(Long id);

    List<CompanyProfile> findByLocationPoliceStationDistrictId(Long id);

    List<CompanyProfile> findByNameContainingIgnoreCase(String keyword);

}
