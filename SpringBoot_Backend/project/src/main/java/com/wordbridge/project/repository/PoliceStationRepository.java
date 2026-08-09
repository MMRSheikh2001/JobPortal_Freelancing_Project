package com.wordbridge.project.repository;

import com.wordbridge.project.entity.PoliceStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PoliceStationRepository extends JpaRepository<PoliceStation, Long> {
    List<PoliceStation> findByDistrictId(Long districtId);

    List<PoliceStation> findByDistrictName(String districtName);

}
