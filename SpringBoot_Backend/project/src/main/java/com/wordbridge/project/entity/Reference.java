package com.wordbridge.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_references")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String organization;
    private String designation;

    private String phone;
    private String email;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String address;


    private String relation;

    @ManyToOne
    private UserProfile userProfile;

}
