package com.wordbridge.project.ai;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AIInterviewSessionRepository extends JpaRepository<AIInterviewSession, Long> {

    AIInterviewSession findByApplicationId(Long applicationId);


}
