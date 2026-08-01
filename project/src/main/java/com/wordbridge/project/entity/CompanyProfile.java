package com.wordbridge.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordbridge.project.job.Job;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "companyprofiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String phone;

    private String companyEmail;

    private String image;




    @Column(columnDefinition = "TEXT")
    private String companyDescription;
    private String companyWebsite;
    private String industry;

    private String foundedYear;

    private String tradeLicenseNumber;


    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private Address location;

    @OneToMany(mappedBy = "companyProfile")
    @JsonIgnore
    private List<Job> jobs;



}
