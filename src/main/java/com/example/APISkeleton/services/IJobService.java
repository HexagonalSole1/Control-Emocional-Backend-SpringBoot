package com.example.APISkeleton.services;

import com.example.APISkeleton.persistance.entities.enums.ApplicationStatus;
import com.example.APISkeleton.web.dtos.requests.CreateJobRequest;
import com.example.APISkeleton.web.dtos.responses.BaseResponse;

public interface IJobService {
    // 🔹 Obtener trabajo por ID con BaseResponse
    BaseResponse getById(Long jobId);

    // 🔹 Obtener todos los trabajos con BaseResponse
    BaseResponse getAll();

    // 🔹 Crear un nuevo trabajo desde un DTO con validación
    BaseResponse createJob(CreateJobRequest request);

    // 🔹 Agregar "Me gusta" a un trabajo
    BaseResponse likeJob(Long jobId, Long userId);

    // 🔹 Quitar "Me gusta" de un trabajo
    BaseResponse unlikeJob(Long jobId, Long userId);

    BaseResponse getAllByIdUser( Long userId);
    BaseResponse getAllByIdUserApplied( Long userId);


    BaseResponse updateApplicationStatus(Long applicationId, ApplicationStatus status);
}
