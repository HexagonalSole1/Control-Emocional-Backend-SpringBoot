package com.example.APISkeleton.web.controllers;

import com.example.APISkeleton.persistance.entities.Note.NoteType;
import com.example.APISkeleton.security.entities.UseDetailsImpl;
import com.example.APISkeleton.services.INoteService;
import com.example.APISkeleton.web.dtos.notes.CreateNoteRequest;
import com.example.APISkeleton.web.dtos.responses.BaseResponse;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final INoteService noteService;

    public NoteController(INoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<BaseResponse> createNote(
            @AuthenticationPrincipal UseDetailsImpl userDetails,
            @Valid @RequestBody CreateNoteRequest request) {
        Long userId = userDetails.getUser().getId();
        BaseResponse response = noteService.createNote(userId, request);
        return response.buildResponseEntity();
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<BaseResponse> getNotesByDate(
            @AuthenticationPrincipal UseDetailsImpl userDetails,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Long userId = userDetails.getUser().getId();
        BaseResponse response = noteService.getNotesByDate(userId, date);
        return response.buildResponseEntity();
    }

    @GetMapping("/today")
    public ResponseEntity<BaseResponse> getTodayNotes(
            @AuthenticationPrincipal UseDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getId();
        LocalDate today = LocalDate.now();
        BaseResponse response = noteService.getNotesByDate(userId, today);
        return response.buildResponseEntity();
    }

    @GetMapping("/range")
    public ResponseEntity<BaseResponse> getNotesByDateRange(
            @AuthenticationPrincipal UseDetailsImpl userDetails,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Long userId = userDetails.getUser().getId();
        BaseResponse response = noteService.getNotesByDateRange(userId, startDate, endDate);
        return response.buildResponseEntity();
    }

    @GetMapping("/type")
    public ResponseEntity<BaseResponse> getNotesByType(
            @AuthenticationPrincipal UseDetailsImpl userDetails,
            @RequestParam NoteType type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Long userId = userDetails.getUser().getId();
        if (date == null) {
            date = LocalDate.now();
        }
        BaseResponse response = noteService.getNotesByType(userId, type, date);
        return response.buildResponseEntity();
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<BaseResponse> updateNote(
            @AuthenticationPrincipal UseDetailsImpl userDetails,
            @PathVariable Long noteId,
            @Valid @RequestBody CreateNoteRequest request) {
        Long userId = userDetails.getUser().getId();
        BaseResponse response = noteService.updateNote(userId, noteId, request);
        return response.buildResponseEntity();
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<BaseResponse> deleteNote(
            @AuthenticationPrincipal UseDetailsImpl userDetails,
            @PathVariable Long noteId) {
        Long userId = userDetails.getUser().getId();
        BaseResponse response = noteService.deleteNote(userId, noteId);
        return response.buildResponseEntity();
    }

    @GetMapping("/status")
    public ResponseEntity<BaseResponse> checkDailyNoteStatus(
            @AuthenticationPrincipal UseDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getId();
        BaseResponse response = noteService.checkDailyNoteStatus(userId);
        return response.buildResponseEntity();
    }
}