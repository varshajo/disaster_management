package com.varsha.disastermanagement.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.varsha.disastermanagement.alert.AlertRepository;
import com.varsha.disastermanagement.disaster.DisasterEventRepository;
import com.varsha.disastermanagement.disaster.DisasterType;
import com.varsha.disastermanagement.disaster.RescueTaskRepository;
import com.varsha.disastermanagement.disaster.RescueTaskStatus;
import com.varsha.disastermanagement.user.UserRepository;

@RestController
@RequestMapping("/api/admin/analytics")
@PreAuthorize("hasRole('ADMIN')")
public class AnalyticsController {

    @Autowired
    private DisasterEventRepository disasterEventRepository;

    @Autowired
    private RescueTaskRepository rescueTaskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AlertRepository alertRepository;

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getAnalyticsSummary() {
        Map<String, Object> summary = new HashMap<>();

        // Total disasters
        long totalDisasters = disasterEventRepository.count();

        // Critical alerts (CRITICAL severity)
        long criticalAlerts = alertRepository.findAll().stream()
                .filter(alert -> alert.getSeverity().toString().equals("CRITICAL"))
                .count();

        // Active rescue tasks (not completed)
        long activeRescueTasks = rescueTaskRepository.findAll().stream()
                .filter(task -> task.getStatus() != RescueTaskStatus.COMPLETED)
                .count();

        // Total users
        long totalUsers = userRepository.count();

        summary.put("totalDisasters", totalDisasters);
        summary.put("criticalAlerts", criticalAlerts);
        summary.put("activeRescueTasks", activeRescueTasks);
        summary.put("totalUsers", totalUsers);

        return ResponseEntity.ok(summary);
    }

    @GetMapping("/type-distribution")
    public ResponseEntity<Map<DisasterType, Long>> getDisasterTypeDistribution() {
        Map<DisasterType, Long> distribution = new HashMap<>();

        List<DisasterType> allTypes = List.of(DisasterType.FLOOD, DisasterType.CYCLONE, DisasterType.EARTHQUAKE, DisasterType.FIRE, DisasterType.STORM);

        for (DisasterType type : allTypes) {
            long count = disasterEventRepository.findAll().stream()
                    .filter(disaster -> disaster.getDisasterType() == type)
                    .count();
            distribution.put(type, count);
        }

        return ResponseEntity.ok(distribution);
    }

    @GetMapping("/monthly-trends")
    public ResponseEntity<Map<String, Long>> getMonthlyTrends() {
        Map<String, Long> trends = new HashMap<>();
        // Simple implementation - in real app, use proper date grouping
        long thisMonth = disasterEventRepository.findAll().stream()
                .filter(d -> d.getCreatedAt().getMonth() == java.time.LocalDate.now().getMonth())
                .count();
        long lastMonth = disasterEventRepository.findAll().stream()
                .filter(d -> d.getCreatedAt().getMonth() == java.time.LocalDate.now().minusMonths(1).getMonth())
                .count();
        trends.put("thisMonth", thisMonth);
        trends.put("lastMonth", lastMonth);
        return ResponseEntity.ok(trends);
    }
}