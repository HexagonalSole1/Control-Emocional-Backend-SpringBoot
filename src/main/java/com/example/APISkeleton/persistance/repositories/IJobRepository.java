package com.example.APISkeleton.persistance.repositories;

import com.example.APISkeleton.persistance.entities.Job;
import com.example.APISkeleton.persistance.entities.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IJobRepository extends JpaRepository<Job, Long> {

    @Query("SELECT j FROM Job j " +
            "WHERE j.id NOT IN (SELECT ja.job.id FROM JobApplication ja WHERE ja.applicant.id = :userId)")
    List<Job> findAllJobsNotAppliedByUser(@Param("userId") Long userId);

    @Query("SELECT j FROM Job j " +
            "WHERE j.id  IN (SELECT ja.job.id FROM JobApplication ja WHERE ja.applicant.id = :userId)")
    List<Job> findAllJobsAppliedByUser(@Param("userId") Long userId);
}
