package com.varsha.disastermanagement.disaster;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RescueTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long disasterId;

    private Long responderId;

    private String description;

    @Enumerated(EnumType.STRING)
    private RescueTaskStatus status;

    private LocalDateTime assignedAt;

    private LocalDateTime completedAt;
}