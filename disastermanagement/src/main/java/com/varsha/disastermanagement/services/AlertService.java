package com.varsha.disastermanagement.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.varsha.disastermanagement.alert.Alert;
import com.varsha.disastermanagement.alert.AlertRepository;
import com.varsha.disastermanagement.disaster.SeverityLevel;

@Service
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;

    public Alert createAlert(Long disasterId, String message, String targetRegion, SeverityLevel severity) {
        Alert alert = new Alert();
        alert.setDisasterId(disasterId);
        alert.setMessage(message);
        alert.setTargetRegion(targetRegion);
        alert.setSeverity(severity);
        alert.setBroadcastTime(LocalDateTime.now());
        return alertRepository.save(alert);
    }

    public List<Alert> getAlertsByRegion(String region) {
        return alertRepository.findByTargetRegion(region);
    }

    public Alert acknowledgeAlert(Long alertId, String acknowledgedBy) {
        Alert alert = alertRepository.findById(alertId).orElseThrow(() -> new RuntimeException("Alert not found"));
        alert.setAcknowledged(true);
        alert.setAcknowledgedBy(acknowledgedBy);
        alert.setAcknowledgedAt(LocalDateTime.now());
        return alertRepository.save(alert);
    }
}