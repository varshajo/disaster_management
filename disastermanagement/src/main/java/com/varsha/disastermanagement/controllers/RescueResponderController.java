package com.varsha.disastermanagement.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.varsha.disastermanagement.alert.Alert;
import com.varsha.disastermanagement.disaster.RescueTask;
import com.varsha.disastermanagement.disaster.RescueTaskStatus;
import com.varsha.disastermanagement.services.AlertService;
import com.varsha.disastermanagement.services.RescueTaskService;

@RestController
@RequestMapping("/api/responder/tasks")
@PreAuthorize("hasRole('RESPONDER')")
public class RescueResponderController {

    @Autowired
    private RescueTaskService rescueTaskService;

    @Autowired
    private AlertService alertService;

    @GetMapping("/{responderId}")
    public ResponseEntity<List<RescueTask>> getTasksByResponderId(@PathVariable Long responderId) {
        List<RescueTask> tasks = rescueTaskService.getTasksByResponderId(responderId);
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<RescueTask> updateTaskStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String statusString = request.get("status");
        RescueTaskStatus status = RescueTaskStatus.valueOf(statusString);
        RescueTask task = rescueTaskService.updateTaskStatus(id, status);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/alerts/{alertId}/acknowledge")
    public ResponseEntity<Alert> acknowledgeAlert(@PathVariable Long alertId, org.springframework.security.core.Authentication auth) {
        String acknowledgedBy = auth.getName();
        Alert alert = alertService.acknowledgeAlert(alertId, acknowledgedBy);
        return ResponseEntity.ok(alert);
    }
}