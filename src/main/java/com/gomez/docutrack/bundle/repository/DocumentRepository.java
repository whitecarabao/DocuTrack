package com.gomez.docutrack.bundle.repository;

import com.gomez.docutrack.bundle.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    @Query("SELECT d FROM Document d LEFT JOIN FETCH d.history WHERE d.id = :id")
    Optional<Document> findByIdWithHistory(@Param("id") Long id);
}
