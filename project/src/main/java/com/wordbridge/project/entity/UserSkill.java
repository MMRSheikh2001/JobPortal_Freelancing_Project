package com.wordbridge.project.entity;

import com.wordbridge.project.enums.ProficiencyLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "userskills")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ProficiencyLevel proficiencyLevel;

    private Integer yearsOfExperience;

    @CreationTimestamp
    private LocalDateTime createdAt;


    @ManyToOne
    private UserProfile userProfile;

    @ManyToOne
    private Skill skill;
}
