package com.example.APISkeleton.services.impls;

import com.example.APISkeleton.persistance.entities.Job;
import com.example.APISkeleton.persistance.entities.JobApplication;
import com.example.APISkeleton.persistance.entities.JobLike;
import com.example.APISkeleton.persistance.entities.User;
import com.example.APISkeleton.persistance.entities.enums.ApplicationStatus;
import com.example.APISkeleton.persistance.repositories.IJobApplicationRepository;
import com.example.APISkeleton.persistance.repositories.IJobLikeRepository;
import com.example.APISkeleton.persistance.repositories.IJobRepository;
import com.example.APISkeleton.persistance.repositories.IUserRepository;
import com.example.APISkeleton.services.IJobService;
import com.example.APISkeleton.web.dtos.requests.CreateJobRequest;
import com.example.APISkeleton.web.dtos.responses.BaseResponse;
import com.example.APISkeleton.web.dtos.responses.JobResponse;
import com.example.APISkeleton.web.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements IJobService {

    private final IJobRepository jobRepository;
    private final IJobLikeRepository jobLikeRepository;
    private final IUserRepository userRepository;
    private final IJobApplicationRepository jobApplicationRepository;
    private final FirebaseMessagingService firebaseMessagingService;
    public JobServiceImpl(IJobRepository jobRepository, IJobLikeRepository jobLikeRepository,
                          IUserRepository userRepository, IJobApplicationRepository jobApplicationRepository, FirebaseMessagingService firebaseMessagingService) {
        this.jobRepository = jobRepository;
        this.jobLikeRepository = jobLikeRepository;
        this.userRepository = userRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.firebaseMessagingService = firebaseMessagingService;
    }

    // 🔹 Obtener trabajo por ID con conteo de postulaciones y likes
    @Override
    public BaseResponse getById(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException(Job.class));

        Long applicationCount = jobApplicationRepository.countByJob(job);
        Long likeCount = jobLikeRepository.countByJob(job);

        JobResponse jobResponse = new JobResponse(job, applicationCount, likeCount);

        return BaseResponse.builder()
                .data(jobResponse)
                .message("Job retrieved successfully")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    // 🔹 Obtener todos los trabajos con conteo de postulaciones y likes
    @Override
    public BaseResponse getAll() {
        List<JobResponse> jobs = jobRepository.findAll().stream()
                .map(job -> new JobResponse(
                        job,
                        jobApplicationRepository.countByJob(job),
                        jobLikeRepository.countByJob(job)
                ))
                .collect(Collectors.toList());

        return BaseResponse.builder()
                .data(jobs)
                .message("Jobs retrieved successfully")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    // 🔹 Crear un nuevo trabajo con validación
    @Override
    public BaseResponse createJob(CreateJobRequest request) {
        User employer = userRepository.findById(request.getEmployerId())
                .orElseThrow(() -> new ResourceNotFoundException(User.class));
        Job job = new Job();
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setLocation(request.getLocation());
        job.setSalary(request.getSalary());
        job.setEmployer(employer);

        jobRepository.save(job);

// 🔹 Construir mensaje con los detalles del trabajo
        String message = String.format(
                "📢 ¡Nuevo trabajo publicado! 🏢\n\n" +
                        "🔹 *Título*: %s\n" +
                        "📍 *Ubicación*: %s\n" +
                        "💰 *Salario*: $%.2f MXN\n\n" +
                        "📲 Postúlate ahora en la app!",
                job.getTitle(),
                job.getLocation() != null ? job.getLocation() : "Ubicación no especificada",
                job.getSalary() != null ? job.getSalary() : 0.0
        );

// 🔹 Enviar notificación masiva
        firebaseMessagingService.sendNotificationToAll(
                "🚀 Se publicó un nuevo trabajo",
                message
        );


        return BaseResponse.builder()
                .data("Job created successfully")
                .message("Job created successfully")
                .success(true)
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    // 🔹 Dar like a un trabajo con respuesta
    @Override
    public BaseResponse likeJob(Long jobId, Long userId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException(Job.class));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(User.class));

        Optional<JobLike> existingLike = jobLikeRepository.findByJobAndUser(job, user);
        if (existingLike.isPresent()) {
            throw new IllegalStateException("You already liked this job");
        }

        JobLike jobLike = new JobLike();
        jobLike.setJob(job);
        jobLike.setUser(user);
        jobLikeRepository.save(jobLike);

        return BaseResponse.builder()
                .message("Job liked successfully")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    // 🔹 Quitar like de un trabajo con respuesta
    @Override
    public BaseResponse unlikeJob(Long jobId, Long userId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException(Job.class));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(User.class));

        JobLike jobLike = jobLikeRepository.findByJobAndUser(job, user)
                .orElseThrow(() -> new IllegalStateException("You haven't liked this job yet"));

        jobLikeRepository.delete(jobLike);

        return BaseResponse.builder()
                .message("Job unliked successfully")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Override
    public BaseResponse getAllByIdUser(Long userId) {
        List<JobResponse> jobs = jobRepository.findAllJobsNotAppliedByUser(userId).stream()
                .map(job ->  new JobResponse(
                        job,
                        jobApplicationRepository.countByJob(job),
                        jobLikeRepository.countByJob(job)
                ))
                .collect(Collectors.toList());

        return BaseResponse.builder()
                .data(jobs)
                .message("Jobs retrieved successfully")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
    }
    @Override
    public BaseResponse getAllByIdUserApplied(Long userId) {
        List<JobResponse> jobs = jobRepository.findAllJobsAppliedByUser(userId).stream()
                .map(job ->  new JobResponse(
                        job,
                        jobApplicationRepository.countByJob(job),
                        jobLikeRepository.countByJob(job)
                ))
                .collect(Collectors.toList());

        return BaseResponse.builder()
                .data(jobs)
                .message("Jobs retrieved successfully")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Override
    public BaseResponse updateApplicationStatus(Long applicationId, ApplicationStatus status) {
        // 🔹 Buscar la postulación
        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException(JobApplication.class));

        // 🔹 Actualizar el estado y fecha de actualización
        application.setStatus(status);
        application.setAppliedAt(LocalDateTime.now());
        jobApplicationRepository.save(application);

        return BaseResponse.builder()
                .data(application)
                .message("Application status updated successfully")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
    }
}
