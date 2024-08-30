package com.gomez.docutrack.bundle.repository;

import com.gomez.docutrack.bundle.entities.Question;
import com.gomez.docutrack.bundle.entities.TrainingScenario; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TrainingScenarioRepository extends JpaRepository<TrainingScenario, Long> {

    @Query("SELECT s FROM TrainingScenario s ORDER BY RAND() LIMIT 1")
    TrainingScenario findRandomScenario();

    // Find questions by scenario ID - Corrected
    @Query("SELECT q FROM Question q WHERE q.scenario.id = :scenarioId")
    List<Question> findQuestionsByScenarioId(Long scenarioId);

    // Delete questions by scenario ID (useful when deleting a scenario)
    @Transactional
    @Modifying
    @Query("DELETE FROM Question q WHERE q.scenario.id = :scenarioId")
    void deleteQuestionsByScenarioId(Long scenarioId);

    // Find a scenario by its ID
    Optional<TrainingScenario> findById(Long id);

    // Find all scenarios
    List<TrainingScenario> findAll();

    // Save a scenario (for both creating and updating)
    <S extends TrainingScenario> S save(S entity);
}