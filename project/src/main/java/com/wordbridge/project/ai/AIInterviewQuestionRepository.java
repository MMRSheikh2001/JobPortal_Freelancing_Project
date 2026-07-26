package com.wordbridge.project.ai;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AIInterviewQuestionRepository extends JpaRepository<AIInterviewQuestion,Long> {
    List<AIInterviewQuestion> findBySessionId(Long sessionId);
}
