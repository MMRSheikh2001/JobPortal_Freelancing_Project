package com.wordbridge.project.repository;

import com.wordbridge.project.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio,Long> {

    List<Portfolio> findByUserProfileId(Long userProfileId);

    Long countByUserProfileId(Long userProfileId);

    void deleteByUserProfileId(Long userProfileId);


}
