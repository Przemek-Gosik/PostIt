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

@AllArgsConstructor
@Service
@Slf4j
public class NoteService {

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;

    public NoteDto createNote(NoteDto noteDto){
        Note note = noteMapper.fromDto(noteDto);
        log.info(noteDto.getText());
        note = noteRepository.save(note);
        return noteMapper.toDto(note);

    }

    public NoteDto editNote(NoteDto noteDto){
        Note note = noteRepository.findById(noteDto.getId())
                .orElseThrow(()->new ResourceNotFoundException("Note not found for id "+noteDto.getId()));
        note.setText(noteDto.getText());
        note = noteRepository.save(note);
        return noteMapper.toDto(note);
    }

    public void deleteNote(Long id){
        Note note = noteRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Note not found for id "+id));
        noteRepository.delete(note);
    }

    public List<NoteDto> getAllNotes(){
        return noteMapper.toDto(noteRepository.findAll());
    }

    public NoteDto getNoteById(Long id){
        Note note = noteRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Note not found for id "+id));
        return noteMapper.toDto(note);
    }

}
