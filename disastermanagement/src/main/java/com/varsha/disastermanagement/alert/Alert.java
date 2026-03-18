package com.varsha.disastermanagement.alert;

import java.time.LocalDateTime;

import com.varsha.disastermanagement.disaster.SeverityLevel;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long disasterId;

    private String message;

    private String targetRegion;

    @Enumerated(EnumType.STRING)
    private SeverityLevel severity;

    private LocalDateTime broadcastTime;

    private boolean acknowledged = false;

    private String acknowledgedBy;

    private LocalDateTime acknowledgedAt;
}