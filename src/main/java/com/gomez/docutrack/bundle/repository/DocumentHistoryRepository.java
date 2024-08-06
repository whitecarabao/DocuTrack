package com.gomez.docutrack.bundle.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gomez.docutrack.bundle.entities.DocumentHistory;

public interface DocumentHistoryRepository extends JpaRepository<DocumentHistory, Long> {
}