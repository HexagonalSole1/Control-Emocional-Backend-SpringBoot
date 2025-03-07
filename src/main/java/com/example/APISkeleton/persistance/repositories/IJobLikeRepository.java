package com.example.APISkeleton.persistance.repositories;

import com.example.APISkeleton.persistance.entities.Job;
import com.example.APISkeleton.persistance.entities.JobLike;
import com.example.APISkeleton.persistance.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IJobLikeRepository extends JpaRepository<JobLike, Long> {
    Optional<JobLike> findByJobAndUser(Job job, User user);
    // 🔹 Contar la cantidad de likes de un trabajo
    @Query("SELECT COUNT(l) FROM JobLike l WHERE l.job = :job")
    Long countByJob(Job job);
}
