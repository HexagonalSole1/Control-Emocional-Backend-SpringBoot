package com.example.APISkeleton.services.impls;


import com.example.APISkeleton.persistance.entities.Emotion;
import com.example.APISkeleton.persistance.entities.Note;
import com.example.APISkeleton.persistance.entities.Note.NoteType;
import com.example.APISkeleton.persistance.entities.User;
import com.example.APISkeleton.persistance.repositories.IEmotionRepository;
import com.example.APISkeleton.persistance.repositories.INoteRepository;
import com.example.APISkeleton.persistance.repositories.IUserRepository;
import com.example.APISkeleton.services.INoteService;
import com.example.APISkeleton.web.dtos.emotions.EmotionDTO;
import com.example.APISkeleton.web.dtos.notes.CreateNoteRequest;
import com.example.APISkeleton.web.dtos.notes.NoteDTO;
import com.example.APISkeleton.web.dtos.responses.BaseResponse;
import com.example.APISkeleton.web.exceptions.AccessDeniedException;
import com.example.APISkeleton.web.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements INoteService {

    private final INoteRepository noteRepository;
    private final IEmotionRepository emotionRepository;
    private final IUserRepository userRepository;

    public NoteServiceImpl(INoteRepository noteRepository,
                           IEmotionRepository emotionRepository,
                           IUserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.emotionRepository = emotionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public BaseResponse createNote(Long userId, CreateNoteRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(User.class));

        Note note = new Note();
        note.setUser(user);
        note.setContent(request.getContent());
        note.setNoteDate(LocalDate.now());
        note.setType(request.getType());

        // Asociar emoción si se proporciona
        if (request.getEmotionId() != null) {
            Emotion emotion = emotionRepository.findById(request.getEmotionId())
                    .orElseThrow(() -> new ResourceNotFoundException(Emotion.class));
            note.setEmotion(emotion);
        }

        note = noteRepository.save(note);

        return BaseResponse.builder()
                .data(mapToDTO(note))
                .message("Nota creada con éxito")
                .success(true)
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    @Override
    public BaseResponse getNotesByDate(Long userId, LocalDate date) {
        List<Note> notes = noteRepository.findByUserIdAndNoteDateOrderByCreatedAtDesc(userId, date);

        List<NoteDTO> noteDTOs = notes.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return BaseResponse.builder()
                .data(noteDTOs)
                .message("Notas obtenidas con éxito")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Override
    public BaseResponse getNotesByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        List<Note> notes = noteRepository.findByUserIdAndNoteDateBetweenOrderByNoteDateDescCreatedAtDesc(
                userId, startDate, endDate);

        List<NoteDTO> noteDTOs = notes.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return BaseResponse.builder()
                .data(noteDTOs)
                .message("Notas obtenidas con éxito")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Override
    public BaseResponse getNotesByType(Long userId, NoteType type, LocalDate date) {
        List<Note> notes = noteRepository.findByUserIdAndTypeAndNoteDateOrderByCreatedAtDesc(userId, type, date);

        List<NoteDTO> noteDTOs = notes.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return BaseResponse.builder()
                .data(noteDTOs)
                .message("Notas obtenidas con éxito")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Override
    public BaseResponse updateNote(Long userId, Long noteId, CreateNoteRequest request) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException(Note.class));

        // Verificar que la nota pertenezca al usuario
        if (!note.getUser().getId().equals(userId)) {
            throw new AccessDeniedException();
        }

        note.setContent(request.getContent());
        note.setType(request.getType());

        // Actualizar emoción si se proporciona
        if (request.getEmotionId() != null) {
            Emotion emotion = emotionRepository.findById(request.getEmotionId())
                    .orElseThrow(() -> new ResourceNotFoundException(Emotion.class));
            note.setEmotion(emotion);
        } else {
            note.setEmotion(null);
        }

        note = noteRepository.save(note);

        return BaseResponse.builder()
                .data(mapToDTO(note))
                .message("Nota actualizada con éxito")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Override
    public BaseResponse deleteNote(Long userId, Long noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException(Note.class));

        // Verificar que la nota pertenezca al usuario
        if (!note.getUser().getId().equals(userId)) {
            throw new AccessDeniedException();
        }

        noteRepository.delete(note);

        return BaseResponse.builder()
                .message("Nota eliminada con éxito")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Override
    public BaseResponse checkDailyNoteStatus(Long userId) {
        LocalDate today = LocalDate.now();
        boolean hasCreatedNote = noteRepository.existsByUserIdAndNoteDate(userId, today);

        Map<String, Object> response = new HashMap<>();
        response.put("hasCreatedNote", hasCreatedNote);
        response.put("date", today);

        return BaseResponse.builder()
                .data(response)
                .message("Estado de creación de nota obtenido con éxito")
                .success(true)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    // Método auxiliar para mapear una Note a un NoteDTO
    private NoteDTO mapToDTO(Note note) {
        NoteDTO dto = new NoteDTO();
        dto.setId(note.getId());
        dto.setContent(note.getContent());
        dto.setNoteDate(note.getNoteDate());
        dto.setType(note.getType());
        dto.setCreatedAt(note.getCreatedAt());

        // Mapear la emoción si existe
        if (note.getEmotion() != null) {
            EmotionDTO emotionDTO = new EmotionDTO();
            emotionDTO.setId(note.getEmotion().getId());
            emotionDTO.setName(note.getEmotion().getName());
            emotionDTO.setDescription(note.getEmotion().getDescription());
            emotionDTO.setColor(note.getEmotion().getColor());
            emotionDTO.setIcon(note.getEmotion().getIcon());

            dto.setEmotion(emotionDTO);
        }

        return dto;
    }
}