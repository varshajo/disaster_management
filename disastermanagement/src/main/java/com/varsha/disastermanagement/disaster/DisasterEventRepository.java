package com.varsha.disastermanagement.disaster;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DisasterEventRepository extends JpaRepository<DisasterEvent, Long> {
    List<DisasterEvent> findByStatus(Status status);

    @Query("SELECT d FROM DisasterEvent d WHERE d.status = 'VERIFIED' AND (:disasterType IS NULL OR d.disasterType = :disasterType) AND (:location IS NULL OR d.locationName LIKE %:location%)")
    List<DisasterEvent> findActiveDisasters(@Param("disasterType") DisasterType disasterType, @Param("location") String location);

    Optional<DisasterEvent> findByTitleAndLocationName(String title, String locationName);
}