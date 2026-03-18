package com.varsha.disastermanagement.alert;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByTargetRegion(String targetRegion);
}