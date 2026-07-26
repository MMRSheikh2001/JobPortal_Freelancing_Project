package com.wordbridge.project.repository;

import com.wordbridge.project.entity.District;
import com.wordbridge.project.entity.Division;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {

    List<District> findByDivisionId(Long divisionId);


    //ekhane JPQL use kore sobsomoy findBy dite hobe.
    //tarpor Country theke Id khuje nibe

    //Find all divisions by Country Name
    List<District> findByDivisionName(String divisionName);
}
