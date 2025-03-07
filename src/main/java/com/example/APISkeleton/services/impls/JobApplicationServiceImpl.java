package com.example.APISkeleton.services.impls;

import com.example.APISkeleton.persistance.entities.Job;
import com.example.APISkeleton.persistance.entities.JobApplication;
import com.example.APISkeleton.persistance.entities.User;
import com.example.APISkeleton.persistance.entities.enums.ApplicationStatus;
import com.example.APISkeleton.persistance.repositories.IJobApplicationRepository;
import com.example.APISkeleton.persistance.repositories.IJobRepository;
import com.example.APISkeleton.persistance.repositories.IUserRepository;
import com.example.APISkeleton.services.IJobApplicationService;
import com.example.APISkeleton.web.dtos.requests.CreateJobApplicationRequest;
import com.example.APISkeleton.web.dtos.requests.UpdateApplicationStatusRequest;
import com.example.APISkeleton.web.dtos.responses.BaseResponse;
import com.example.APISkeleton.web.dtos.responses.JobApplicationResponse;
import com.example.APISkeleton.web.dtos.responses.JobApplicationResponseDTO;
import com.example.APISkeleton.web.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobApplicationServiceImpl implements IJobApplicationService {

    private final IJobApplicationRepository jobApplicationRepository;
    private final IJobRepository jobRepository;
    private final IUserRepository userRepository;
    private final FirebaseMessagingService firebaseMessagingService;

    public JobApplicationServiceImpl(IJobApplicationRepository jobApplicationRepository, IJobRepository jobRepository, IUserRepository userRepository, FirebaseMessagingService firebaseMessagingService) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.firebaseMessagingService = firebaseMessagingService;
    }
    @Override
    public BaseResponse updateApplicationStatus(UpdateApplicationStatusRequest request) {
        JobApplication application = jobApplicationRepository.findById(request.getApplicationId())
                .orElseThrow(() -> new ResourceNotFoundException(JobApplication.class));

        application.setStatus(request.getStatus());
        application.setAppliedAt(LocalDateTime.now());
        jobApplicationRepository.save(application);

        if (request.getStatus().equals("ACCEPTED")) {
            firebaseMessagingService.sendNotification("Te Aceptaron en una chamba","Felicades ponte a chambear",application.getApplicant().getFCM());
            System.out.println(application.getApplicant().getFCM());
        }

        // Convertir la entidad a DTO antes de devolver la respuesta
        JobApplicationResponseDTO responseDTO = new JobApplicationResponseDTO(
                application.getId(),
                application.getApplicant().getId(),
                application.getApplicant().getEmail(),
                application.getJob().getId(),
                application.getJob().getTitle(),
                application.getStatus(),
                application.getAppliedAt()
        );

        return BaseResponse.builder()
                .data(responseDTO)
                .message("Application status updated successfully")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Override
    public BaseResponse getApplicationsByUserAndStatus(Long userId, ApplicationStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(User.class));

        List<JobApplicationResponseDTO> applications = jobApplicationRepository.findByApplicantAndStatus(user, status)
                .stream()
                .map(application -> new JobApplicationResponseDTO(
                        application.getId(),
                        application.getApplicant().getId(),
                        application.getApplicant().getEmail(),
                        application.getJob().getId(),
                        application.getJob().getTitle(),
                        application.getStatus(),
                        application.getAppliedAt()
                ))
                .toList();

        return BaseResponse.builder()
                .data(applications)
                .message("Applications retrieved successfully")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
    }


    @Override
    public BaseResponse applyForJob(CreateJobApplicationRequest request) {
        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException(Job.class));

        User applicant = userRepository.findById(request.getApplicantId())
                .orElseThrow(() -> new ResourceNotFoundException(User.class));

        Optional<JobApplication> existingApplication = jobApplicationRepository.findByJobAndApplicant(job, applicant);
        if (existingApplication.isPresent()) {
            return BaseResponse.builder()
                    .message("You have already applied for this job")
                    .success(false)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }

        JobApplication jobApplication = new JobApplication();
        jobApplication.setJob(job);
        jobApplication.setApplicant(applicant);
        jobApplication.setStatus(ApplicationStatus.PENDING);
        jobApplication.setAppliedAt(LocalDateTime.now());


        jobApplicationRepository.save(jobApplication);

    firebaseMessagingService.sendNotification("Persona Quiere Chamba","Hay un Postulante para tu obra",job.getEmployer().getFCM());

        return BaseResponse.builder()
                .message("Job application submitted successfully")
                .success(true)
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    @Override
    public BaseResponse getApplicationsByJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException(Job.class));

        List<JobApplicationResponse> applications = jobApplicationRepository.findByJob(job)
                .stream()
                .map(application -> new JobApplicationResponse(
                        application.getId(),
                        application.getJob().getId(),
                        application.getJob().getTitle(),
                        application.getApplicant().getId(),
                        application.getApplicant().getName(),
                        application.getStatus(),
                        application.getAppliedAt()
                ))
                .collect(Collectors.toList());

        return BaseResponse.builder()
                .data(applications)
                .message("Job applications retrieved successfully")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Override
    public BaseResponse getApplicationsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(User.class));

        List<JobApplicationResponse> applications = jobApplicationRepository.findByApplicant(user)
                .stream()
                .map(application -> new JobApplicationResponse(
                        application.getId(),
                        application.getJob().getId(),
                        application.getJob().getTitle(),
                        application.getApplicant().getId(),
                        application.getApplicant().getName(),
                        application.getStatus(),
                        application.getAppliedAt()
                ))
                .collect(Collectors.toList());

        return BaseResponse.builder()
                .data(applications)
                .message("User job applications retrieved successfully")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Override
    public BaseResponse withdrawApplication(Long applicationId) {
        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException(JobApplication.class));

        jobApplicationRepository.delete(application);

        return BaseResponse.builder()
                .message("Job application withdrawn successfully")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
    }
}
