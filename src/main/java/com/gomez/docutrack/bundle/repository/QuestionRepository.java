package com.gomez.docutrack.bundle.repository;

import com.gomez.docutrack.bundle.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    // Custom method to delete questions by scenario ID
    void deleteByScenarioId(Long scenarioId);
}
