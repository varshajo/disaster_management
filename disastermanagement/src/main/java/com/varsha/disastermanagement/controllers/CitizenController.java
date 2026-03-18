package com.varsha.disastermanagement.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.varsha.disastermanagement.alert.Alert;
import com.varsha.disastermanagement.disaster.DisasterEvent;
import com.varsha.disastermanagement.disaster.DisasterType;
import com.varsha.disastermanagement.disaster.SeverityLevel;
import com.varsha.disastermanagement.disaster.Status;
import com.varsha.disastermanagement.payload.MessageResponse;
import com.varsha.disastermanagement.services.AlertService;
import com.varsha.disastermanagement.services.DisasterEventService;
import com.varsha.disastermanagement.user.User;
import com.varsha.disastermanagement.user.UserRepository;

@RestController
@RequestMapping("/api/citizen")
@PreAuthorize("hasRole('CITIZEN')")
public class CitizenController {

    @Autowired
    private DisasterEventService disasterEventService;

    @Autowired
    private AlertService alertService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/report")
    public ResponseEntity<?> reportDisaster(@RequestBody DisasterReportRequest request, Authentication auth) {
        String reportedBy = auth.getName();

        DisasterEvent disaster = new DisasterEvent();
        disaster.setDisasterType(DisasterType.valueOf(request.getDisasterType()));
        disaster.setLocationName(request.getLocation());
        disaster.setSeverity(SeverityLevel.valueOf(request.getSeverity()));
        disaster.setDescription(request.getDescription());
        disaster.setReportedBy(reportedBy);
        disaster.setReportedAt(LocalDateTime.now());
        disaster.setStatus(Status.PENDING);

        disasterEventService.saveDisasterEvent(disaster);

        return ResponseEntity.ok(new MessageResponse("Disaster reported successfully"));
    }

    @GetMapping("/alerts")
    public ResponseEntity<List<Alert>> getMyAlerts(Authentication auth) {
        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        List<Alert> alerts = alertService.getAlertsByRegion(user.getRegion());
        return ResponseEntity.ok(alerts);
    }

    public static class DisasterReportRequest {
        private String disasterType;
        private String location;
        private String severity;
        private String description;

        // getters and setters
        public String getDisasterType() { return disasterType; }
        public void setDisasterType(String disasterType) { this.disasterType = disasterType; }
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        public String getSeverity() { return severity; }
        public void setSeverity(String severity) { this.severity = severity; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}