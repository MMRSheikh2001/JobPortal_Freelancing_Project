package com.wordbridge.project.entity;

import com.wordbridge.project.enums.EducationLevel;
import com.wordbridge.project.enums.ResultType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "educations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EducationLevel educationLevel;

    private String board;

    private String institution;

    private String fieldOfStudy;

    @Enumerated(EnumType.STRING)
    private ResultType resultType;
    private Double result;
    private Double outOf;
    private String gradeOrDivision;

    private LocalDate startDate;
    private LocalDate endDate;

    private Boolean currentlyStudying;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    private UserProfile userProfile;


}
