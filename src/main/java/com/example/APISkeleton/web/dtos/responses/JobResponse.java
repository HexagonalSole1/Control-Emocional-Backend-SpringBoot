package com.example.APISkeleton.web.dtos.responses;

import com.example.APISkeleton.persistance.entities.Job;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobResponse {
    private Long id;
    private String title;
    private String description;
    private String location;
    private Double salary;
    private Long applicationCount;
    private Long likeCount;

    public JobResponse(Job job, Long applicationCount, Long likeCount) {
        this.id = job.getId();
        this.title = job.getTitle();
        this.description = job.getDescription();
        this.location = job.getLocation();
        this.salary = job.getSalary();
        this.applicationCount = applicationCount;
        this.likeCount = likeCount;
    }
}
