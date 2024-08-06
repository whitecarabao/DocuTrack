package com.gomez.docutrack.bundle.repository;

import com.gomez.docutrack.bundle.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByName(String name);
}

