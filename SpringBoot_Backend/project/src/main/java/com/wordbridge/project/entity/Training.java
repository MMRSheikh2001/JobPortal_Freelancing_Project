package com.wordbridge.project.entity;

import com.wordbridge.project.enums.TrainingType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "trainings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    private String institution;

    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean completed;
    private String duration;

    private String certificateFile;
    private String certificateVerificationUrl;
    private String certificateId;

    @Enumerated(EnumType.STRING)
    private TrainingType trainingType;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


    @ManyToOne
    private UserProfile userProfile;

}
