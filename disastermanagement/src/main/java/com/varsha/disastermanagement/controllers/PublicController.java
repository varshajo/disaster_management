package com.varsha.disastermanagement.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.varsha.disastermanagement.alert.Alert;
import com.varsha.disastermanagement.disaster.DisasterEvent;
import com.varsha.disastermanagement.disaster.DisasterType;
import com.varsha.disastermanagement.services.AlertService;
import com.varsha.disastermanagement.services.DisasterEventService;

@RestController
@RequestMapping("/api/disasters")
public class PublicController {

    @Autowired
    private DisasterEventService disasterEventService;

    @Autowired
    private AlertService alertService;

    @GetMapping("/active")
    public ResponseEntity<List<DisasterEvent>> getActiveDisasters(
            @RequestParam(required = false) DisasterType disasterType,
            @RequestParam(required = false) String location) {
        List<DisasterEvent> active = disasterEventService.getActiveDisasters(disasterType, location);
        return ResponseEntity.ok(active);
    }

    @GetMapping("/region/{region}")
    public ResponseEntity<List<Alert>> getAlertsByRegion(@PathVariable String region) {
        List<Alert> alerts = alertService.getAlertsByRegion(region);
        return ResponseEntity.ok(alerts);
    }
}