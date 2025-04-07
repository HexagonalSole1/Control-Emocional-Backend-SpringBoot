
package com.example.APISkeleton.services.impls;

import com.example.APISkeleton.persistance.entities.Emotion;
import com.example.APISkeleton.persistance.repositories.IEmotionRepository;
import com.example.APISkeleton.services.IEmotionService;
import com.example.APISkeleton.web.dtos.emotions.CreateEmotionRequest;
import com.example.APISkeleton.web.dtos.emotions.EmotionDTO;
import com.example.APISkeleton.web.dtos.responses.BaseResponse;
import com.example.APISkeleton.web.exceptions.ConflictException;
import com.example.APISkeleton.web.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmotionServiceImpl implements IEmotionService {

    private final IEmotionRepository emotionRepository;

    public EmotionServiceImpl(IEmotionRepository emotionRepository) {
        this.emotionRepository = emotionRepository;
    }

    @Override
    public BaseResponse getAllEmotions() {
        List<Emotion> emotions = emotionRepository.findAllByOrderByNameAsc();

        List<EmotionDTO> emotionDTOs = emotions.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return BaseResponse.builder()
                .data(emotionDTOs)
                .message("Emociones obtenidas con éxito")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Override
    public BaseResponse getEmotionById(Long id) {
        Emotion emotion = emotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Emotion.class));

        return BaseResponse.builder()
                .data(mapToDTO(emotion))
                .message("Emoción obtenida con éxito")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Override
    public BaseResponse createEmotion(CreateEmotionRequest request) {
        // Verificar si ya existe una emoción con el mismo nombre
        Optional<Emotion> existingEmotion = emotionRepository.findByName(request.getName());
        if (existingEmotion.isPresent()) {
            throw new ConflictException("Ya existe una emoción con el nombre '" + request.getName() + "'");
        }

        Emotion emotion = new Emotion();
        emotion.setName(request.getName());
        emotion.setDescription(request.getDescription());
        emotion.setColor(request.getColor());
        emotion.setIcon(request.getIcon());

        emotion = emotionRepository.save(emotion);

        return BaseResponse.builder()
                .data(mapToDTO(emotion))
                .message("Emoción creada con éxito")
                .success(true)
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    @Override
    public BaseResponse updateEmotion(Long id, CreateEmotionRequest request) {
        Emotion emotion = emotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Emotion.class));

        // Verificar si hay otra emoción con el mismo nombre
        Optional<Emotion> existingEmotion = emotionRepository.findByName(request.getName());
        if (existingEmotion.isPresent() && !existingEmotion.get().getId().equals(id)) {
            throw new ConflictException("Ya existe otra emoción con el nombre '" + request.getName() + "'");
        }

        emotion.setName(request.getName());
        emotion.setDescription(request.getDescription());
        emotion.setColor(request.getColor());
        emotion.setIcon(request.getIcon());

        emotion = emotionRepository.save(emotion);

        return BaseResponse.builder()
                .data(mapToDTO(emotion))
                .message("Emoción actualizada con éxito")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Override
    public BaseResponse deleteEmotion(Long id) {
        Emotion emotion = emotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Emotion.class));

        emotionRepository.delete(emotion);

        return BaseResponse.builder()
                .message("Emoción eliminada con éxito")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    // Método auxiliar para mapear una Emotion a un EmotionDTO
    private EmotionDTO mapToDTO(Emotion emotion) {
        EmotionDTO dto = new EmotionDTO();
        dto.setId(emotion.getId());
        dto.setName(emotion.getName());
        dto.setDescription(emotion.getDescription());
        dto.setColor(emotion.getColor());
        dto.setIcon(emotion.getIcon());
        return dto;
    }
}