package com.example.APISkeleton.persistance.entities;


import com.example.APISkeleton.persistance.entities.enums.JobStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    private String location;
    private Double salary;

    @ManyToOne
    @JoinColumn(name = "employer_id", nullable = false)
    private User employer;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobLike> likes = new ArrayList<>(); // 🔹 Se inicializa la lista vacía

    // 🔹 Método para obtener el número de likes sin riesgo de `null`
    public int getLikeCount() {
        return likes != null ? likes.size() : 0;
    }
}
