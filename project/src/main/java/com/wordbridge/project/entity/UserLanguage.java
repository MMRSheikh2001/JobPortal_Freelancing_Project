package com.wordbridge.project.entity;

import com.wordbridge.project.enums.LanguageProficiency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "userlanguages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLanguage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private LanguageProficiency proficiency;

    @ManyToOne
    private Language language;

    @ManyToOne
    private UserProfile userProfile;
}
