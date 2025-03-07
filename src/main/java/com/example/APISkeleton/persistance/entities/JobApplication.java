package com.example.APISkeleton.persistance.entities;

import com.example.APISkeleton.persistance.entities.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "job_applications")
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "applicant_id", nullable = false)
    private User applicant; // 🔹 Usuario que se postula

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job; // 🔹 Trabajo al que se postula

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status; // 🔹 Estado de la postulación

    private LocalDateTime appliedAt;
}
