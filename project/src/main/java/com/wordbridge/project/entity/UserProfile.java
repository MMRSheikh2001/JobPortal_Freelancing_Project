package com.wordbridge.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordbridge.project.enums.GenderType;
import com.wordbridge.project.enums.JobType;
import com.wordbridge.project.enums.WorkPlaceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "userprofiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String phone;

    private String image;


    @Lob
    @Column(columnDefinition = "TEXT")
    private String headline;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String professionalSummary;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String bio;

    private LocalDate dateOfBirth;

    private GenderType gender;
    private String nationality;
    private String religion;
    private String maritalStatus;

    private String fatherName;
    private String motherName;

    private String nidNumber;


    private String passportNumber;

    private String githubLink;
    private String linkedinLink;
    private String portfolioWebsite;

    private BigDecimal expectedSalary;
    private BigDecimal currentSalary;

    @Enumerated(EnumType.STRING)
    private JobType preferredJobType;

    @Enumerated(EnumType.STRING)
    private WorkPlaceType preferredWorkplace;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String careerObjective;

    private String freelancerTitle;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "present_address_id",nullable = true)
    private Address presentAddress;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "permanent_address_id", nullable = true)
    private Address permanentAddress;

    private Boolean profileCompleted;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "userProfile")
    @JsonIgnore
    private List<UserSkill> userSkills;

    @OneToMany
    @JsonIgnore
    private List<UserLanguage> userLanguages;

    @OneToMany
    @JsonIgnore
    private List<Education> educations;

    @OneToMany
    @JsonIgnore
    private List<Experience> experiences;

    @OneToMany
    @JsonIgnore
    private List<Training> trainings;

    @OneToMany
    @JsonIgnore
    private List<Portfolio> portfolios;

    @OneToMany
    @JsonIgnore
    private List<Reference> references;

    @OneToMany
    @JsonIgnore
    private List<Extracurricular> extracurriculars;

}
