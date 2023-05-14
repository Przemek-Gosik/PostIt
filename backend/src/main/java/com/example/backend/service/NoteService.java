package com.example.backend.service;

import com.example.backend.dto.NoteDto;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.NoteMapper;
import com.example.backend.model.Note;
import com.example.backend.repository.NoteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * This service contains CRUD operations for Notes
 */
@AllArgsConstructor
@Service
@Slf4j
public class NoteService {

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;

    /**
     * Method to create new Note
     *
     * @param noteDto is object with new note text
     * @return is object with saved note
     */
    public NoteDto createNote(NoteDto noteDto){
        Note note = noteMapper.fromDto(noteDto);
        log.info(noteDto.getText());
        note = noteRepository.save(note);
        log.info("Note created with id "+note.getId());
        return noteMapper.toDto(note);
    }

    /**
     * Method to edit existing note
     *
     * @param noteDto is object with changed data and id of note to edit
     * @return is object with edited data
     */
    public NoteDto editNote(NoteDto noteDto){
        Note note = noteRepository.findById(noteDto.getId())
                .orElseThrow(()->new ResourceNotFoundException("Note not found for id "+noteDto.getId()));
        note.setText(noteDto.getText());
        note = noteRepository.save(note);
        log.info("Note edited for id "+note.getId());
        return noteMapper.toDto(note);
    }

    /**
     * Method to delete note
     *
     * @param id is identifier of note to delete
     */
    public void deleteNote(Long id){
        Note note = noteRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Note not found for id "+id));
        noteRepository.delete(note);
        log.info("Note deleted ");
    }

    /**
     * Method to get all notes from database
     *
     * @return is list of objects with notes data
     */
    public List<NoteDto> getAllNotes(){
        return noteMapper.toDto(noteRepository.findAll());
    }

    /**
     * Method to get data of note by id
     *
     * @param id is identifier of note to get
     * @return is object with note data
     */
    public NoteDto getNoteById(Long id){
        Note note = noteRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Note not found for id "+id));
        return noteMapper.toDto(note);
    }

}
