package com.varsha.disastermanagement.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsha.disastermanagement.disaster.RescueTask;
import com.varsha.disastermanagement.disaster.RescueTaskRepository;
import com.varsha.disastermanagement.disaster.RescueTaskStatus;

@Service
public class RescueTaskService {

    @Autowired
    private RescueTaskRepository rescueTaskRepository;

    public RescueTask assignTask(Long disasterId, Long responderId, String description) {
        RescueTask task = new RescueTask();
        task.setDisasterId(disasterId);
        task.setResponderId(responderId);
        task.setDescription(description);
        task.setStatus(RescueTaskStatus.PENDING);
        task.setAssignedAt(LocalDateTime.now());
        return rescueTaskRepository.save(task);
    }

    public List<RescueTask> getAllTasks() {
        return rescueTaskRepository.findAll();
    }

    public List<RescueTask> getTasksByResponderId(Long responderId) {
        return rescueTaskRepository.findByResponderId(responderId);
    }

    public RescueTask updateTaskStatus(Long taskId, RescueTaskStatus status) {
        RescueTask task = rescueTaskRepository.findById(taskId).orElseThrow();
        task.setStatus(status);
        if (status == RescueTaskStatus.COMPLETED) {
            task.setCompletedAt(LocalDateTime.now());
        }
        return rescueTaskRepository.save(task);
    }

    public List<RescueTask> getTasksByStatus(RescueTaskStatus status) {
        return rescueTaskRepository.findByStatus(status);
    }
}