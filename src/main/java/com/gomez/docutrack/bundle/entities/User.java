package com.gomez.docutrack.bundle.entities;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "app_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String name;  // Added name field
    private String password;
    private boolean isAdmin;

    @Enumerated(EnumType.STRING)
    private SectionName sectionName;

    private String position;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    // Default constructor
    public User() {}

    // Constructor for custom messages
    public User(String name, String position, SectionName sectionName) {
        this.name = name;
        this.position = position;
        this.sectionName = sectionName;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public SectionName getSectionName() {
        return sectionName;
    }

    public void setSectionName(SectionName sectionName) {
        this.sectionName = sectionName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public enum SectionName {
        RPD("Research and Planning Division"),
        OPD("Operations and Warning Division"),
        ADM("Administration and Finance Division"),
        EXE("Executive Level");
    
        private final String description;
    
        SectionName(String description) {
            this.description = description;
        }
    
        public String getDescription() {
            return description;
        }
    }

    public enum Gender {
        MALE,
        FEMALE,
        OTHER
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // Implement roles/authorities if needed
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
