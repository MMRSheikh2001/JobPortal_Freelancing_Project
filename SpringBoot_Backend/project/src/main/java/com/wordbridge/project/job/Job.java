package com.wordbridge.project.job;

import com.wordbridge.project.entity.Category;
import com.wordbridge.project.entity.CompanyProfile;
import com.wordbridge.project.entity.PoliceStation;
import com.wordbridge.project.enums.EmploymentType;
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

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String jobDescription;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String jobResponsibilities;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String educationalRequirements;


    @Lob
    @Column(columnDefinition = "TEXT")
    private String experienceRequirements;
    private Integer minExperience;
    private Integer maxExperience;


    @Lob
    @Column(columnDefinition = "TEXT")
    private String additionalRequirements;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String benefits;

    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private Boolean isNegotiable;


    private LocalDate applicationDeadline;
    private Boolean isActive;

    private Integer vacancy;

    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType;

    @Enumerated(EnumType.STRING)
    private WorkPlaceType workPlaceType;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "company_profile_id")
    private CompanyProfile companyProfile;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "police_station_id")
    private PoliceStation jobLocation;

    //AI Integration
    private Boolean aiScreeningEnabled;

    private Boolean aiCvScreeningEnabled;

    private Boolean aiInterviewEnabled;

    private Integer aiMatchThreshold;

    private Integer aiQuestionCount;

    private Integer aiShortlistCount;

    private Integer aiDeadlineDays;

}
