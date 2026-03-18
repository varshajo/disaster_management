package com.varsha.disastermanagement.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.varsha.disastermanagement.disaster.DisasterEvent;
import com.varsha.disastermanagement.disaster.RescueTask;
import com.varsha.disastermanagement.services.AlertService;
import com.varsha.disastermanagement.services.DisasterEventService;
import com.varsha.disastermanagement.services.RescueTaskService;

@RestController
@RequestMapping("/api/admin/disasters")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AlertService alertService;

    @Autowired
    private DisasterEventService disasterEventService;

    @Autowired
    private RescueTaskService rescueTaskService;

    @GetMapping("/pending")
    public ResponseEntity<List<DisasterEvent>> getPendingDisasters() {
        List<DisasterEvent> pending = disasterEventService.getPendingDisasters();
        return ResponseEntity.ok(pending);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<DisasterEvent> approveDisaster(@PathVariable Long id, Authentication auth) {
        String approvedBy = auth.getName();
        DisasterEvent approved = disasterEventService.approveDisaster(id, approvedBy);
        
        // Create and broadcast alert
        String message = "Alert: " + approved.getTitle() + " in " + approved.getLocationName() + ". Severity: " + approved.getSeverity();
        alertService.createAlert(id, message, approved.getLocationName(), approved.getSeverity());
        
        return ResponseEntity.ok(approved);
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<DisasterEvent> rejectDisaster(@PathVariable Long id) {
        DisasterEvent rejected = disasterEventService.rejectDisaster(id);
        return ResponseEntity.ok(rejected);
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        long pending = disasterEventService.getPendingDisasters().size();
        long verified = disasterEventService.getActiveDisasters(null, null).size();
        return ResponseEntity.ok(Map.of("pending", pending, "verified", verified));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDisaster(@PathVariable Long id) {
        disasterEventService.deleteDisaster(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/rescue/assign")
    public ResponseEntity<RescueTask> assignTask(@RequestBody Map<String, Object> request) {
        Long disasterId = ((Number) request.get("disasterId")).longValue();
        Long responderId = ((Number) request.get("responderId")).longValue();
        String description = (String) request.get("description");

        RescueTask task = rescueTaskService.assignTask(disasterId, responderId, description);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/rescue/all")
    public ResponseEntity<List<RescueTask>> getAllTasks() {
        List<RescueTask> tasks = rescueTaskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }
}