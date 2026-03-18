package com.varsha.disastermanagement.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.varsha.disastermanagement.disaster.DisasterEvent;
import com.varsha.disastermanagement.disaster.DisasterEventRepository;
import com.varsha.disastermanagement.disaster.DisasterType;
import com.varsha.disastermanagement.disaster.SeverityLevel;
import com.varsha.disastermanagement.disaster.Status;

@Service
public class DisasterEventService {

    @Autowired
    private DisasterEventRepository disasterEventRepository;

    @Autowired
    private AlertService alertService;

    public DisasterEvent saveDisasterEvent(DisasterEvent disasterEvent) {
        // Set createdBy if not set
        if (disasterEvent.getCreatedBy() == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName() != null) {
                disasterEvent.setCreatedBy(auth.getName());
            }
        }
        return disasterEventRepository.save(disasterEvent);
    }

    public List<DisasterEvent> getPendingDisasters() {
        return disasterEventRepository.findByStatus(Status.PENDING);
    }

    public DisasterEvent approveDisaster(Long id, String approvedBy) {
        DisasterEvent disaster = disasterEventRepository.findById(id).orElseThrow();
        disaster.setStatus(Status.VERIFIED);
        disaster.setApprovedBy(approvedBy);
        disaster.setApprovedAt(LocalDateTime.now());
        disaster.setUpdatedAt(LocalDateTime.now());
        DisasterEvent saved = disasterEventRepository.save(disaster);

        // Create alert
        alertService.createAlert(saved.getId(), "Disaster Alert: " + saved.getTitle(), saved.getLocationName(), saved.getSeverity());

        return saved;
    }

    public DisasterEvent rejectDisaster(Long id) {
        DisasterEvent disaster = disasterEventRepository.findById(id).orElseThrow();
        disaster.setStatus(Status.REJECTED);
        disaster.setUpdatedAt(LocalDateTime.now());
        return disasterEventRepository.save(disaster);
    }

    public List<DisasterEvent> getActiveDisasters(DisasterType disasterType, String location) {
        return disasterEventRepository.findActiveDisasters(disasterType, location);
    }

    @Scheduled(fixedRate = 60000) // Every minute
    public void autoVerifyCriticalDisasters() {
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
        List<DisasterEvent> pendingCritical = disasterEventRepository.findAll().stream()
                .filter(d -> d.getStatus() == Status.PENDING)
                .filter(d -> d.getSeverity() == SeverityLevel.CRITICAL)
                .filter(d -> d.getCreatedAt().isBefore(fiveMinutesAgo))
                .toList();

        for (DisasterEvent disaster : pendingCritical) {
            disaster.setStatus(Status.VERIFIED);
            disaster.setApprovedBy("AUTO");
            disaster.setApprovedAt(LocalDateTime.now());
            disaster.setUpdatedAt(LocalDateTime.now());
            DisasterEvent saved = disasterEventRepository.save(disaster);

            // Create alert
            alertService.createAlert(saved.getId(), "Auto-Verified Disaster Alert: " + saved.getTitle(), saved.getLocationName(), saved.getSeverity());
        }
    }

    public void deleteDisaster(Long id) {
        disasterEventRepository.deleteById(id);
    }
}