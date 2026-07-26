package com.wordbridge.project.repository;

import com.wordbridge.project.entity.UserProfile;
import com.wordbridge.project.enums.JobType;
import com.wordbridge.project.enums.WorkPlaceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUserId(Long id);

    boolean existsByUserId(Long userId);

    List<UserProfile> findByPresentAddressPoliceStationId(Long id);

    List<UserProfile> findByPresentAddressPoliceStationDistrictId(Long id);


    List<UserProfile> findByPermanentAddressPoliceStationId(Long id);

    List<UserProfile> findByPermanentAddressPoliceStationDistrictId(Long id);


    @Query("""
            SELECT u
            FROM UserProfile u
            WHERE
            
            (:keyword IS NULL OR
            LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(u.user.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(u.phone) LIKE LOWER(CONCAT('%', :keyword, '%')))
            
            AND (:countryId IS NULL OR
            u.presentAddress.policeStation.district.division.country.id = :countryId)
            
            AND (:divisionId IS NULL OR
            u.presentAddress.policeStation.district.division.id = :divisionId)
            
            AND (:districtId IS NULL OR
            u.presentAddress.policeStation.district.id = :districtId)
            
            AND (:policeStationId IS NULL OR
            u.presentAddress.policeStation.id = :policeStationId)
            
            AND (:jobType IS NULL OR
            u.preferredJobType = :jobType)
            
            AND (:workPlaceType IS NULL OR
            u.preferredWorkplace = :workPlaceType)
            
            AND (:gender IS NULL OR
            u.gender = :gender)
            """)
    List<UserProfile> filterUsers(

            @Param("keyword") String keyword,

            @Param("countryId") Long countryId,

            @Param("divisionId") Long divisionId,

            @Param("districtId") Long districtId,

            @Param("policeStationId") Long policeStationId,

            @Param("jobType") JobType jobType,

            @Param("workPlaceType") WorkPlaceType workPlaceType,

            @Param("gender") String gender
    );

}
