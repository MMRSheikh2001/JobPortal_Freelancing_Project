package com.wordbridge.project.repository;

import com.wordbridge.project.entity.Division;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DivisionRepository extends JpaRepository<Division, Long> {
    //Find all divisions by Country Id
    List<Division> findByCountryId(Long countryId);


    //ekhane JPQL use kore sobsomoy findBy dite hobe.
    //tarpor Country theke Id khuje nibe

    //Find all divisions by Country Name
    List<Division> findByCountryName(String countryName);
}
