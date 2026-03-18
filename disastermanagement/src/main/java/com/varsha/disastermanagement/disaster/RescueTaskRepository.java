package com.varsha.disastermanagement.disaster;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RescueTaskRepository extends JpaRepository<RescueTask, Long> {
    List<RescueTask> findByResponderId(Long responderId);

    List<RescueTask> findByDisasterId(Long disasterId);

    List<RescueTask> findByStatus(RescueTaskStatus status);
}