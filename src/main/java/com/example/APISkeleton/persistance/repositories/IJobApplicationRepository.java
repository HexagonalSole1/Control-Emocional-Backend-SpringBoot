package com.example.APISkeleton.persistance.repositories;

import com.example.APISkeleton.persistance.entities.Job;
import com.example.APISkeleton.persistance.entities.JobApplication;
import com.example.APISkeleton.persistance.entities.User;
import com.example.APISkeleton.persistance.entities.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IJobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByJob(Job job);
    List<JobApplication> findByApplicant(User applicant);
    Optional<JobApplication> findByJobAndApplicant(Job job, User applicant);

    @Query("SELECT COUNT(j) FROM JobApplication j WHERE j.job = :job")
    Long countByJob(Job job);

    List<JobApplication> findByApplicantAndStatus(User applicant, ApplicationStatus status);


}
