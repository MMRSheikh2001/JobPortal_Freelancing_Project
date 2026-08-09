package com.wordbridge.project.repository;

import com.wordbridge.project.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByPoliceStationDistrictDivisionCountryId(Long id);

    List<Address> findByPoliceStationDistrictDivisionCountryName(String name);


    List<Address> findByPoliceStationDistrictDivisionId(Long id);

    List<Address> findByPoliceStationDistrictDivisionName(String name);

    List<Address> findByPoliceStationDistrictId(Long id);

    List<Address> findByPoliceStationDistrictName(String name);

    List<Address> findByPoliceStationId(Long id);

    List<Address> findByPoliceStationName(String name);

}
