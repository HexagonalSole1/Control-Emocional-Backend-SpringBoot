package com.example.APISkeleton.services;
import com.example.APISkeleton.persistance.entities.Note.NoteType;
import com.example.APISkeleton.web.dtos.notes.CreateNoteRequest;
import com.example.APISkeleton.web.dtos.responses.BaseResponse;
import java.time.LocalDate;

public interface INoteService {
    BaseResponse createNote(Long userId, CreateNoteRequest request);
    BaseResponse getNotesByDate(Long userId, LocalDate date);
    BaseResponse getNotesByDateRange(Long userId, LocalDate startDate, LocalDate endDate);
    BaseResponse getNotesByType(Long userId, NoteType type, LocalDate date);
    BaseResponse updateNote(Long userId, Long noteId, CreateNoteRequest request);
    BaseResponse deleteNote(Long userId, Long noteId);
    BaseResponse checkDailyNoteStatus(Long userId);
}

