package com.wordbridge.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "districts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    private Division division;

    @OneToMany
    private List<PoliceStation> policeStations;

    @OneToMany
    private List<Address> addresses;

}
