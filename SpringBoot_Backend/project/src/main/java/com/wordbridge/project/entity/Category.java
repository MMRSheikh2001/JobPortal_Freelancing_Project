package com.wordbridge.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordbridge.project.job.Job;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;


    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany
    @JsonIgnore
    private List<Skill> skills;

    @OneToMany
    @JsonIgnore
    private List<Job> jobs;
}
