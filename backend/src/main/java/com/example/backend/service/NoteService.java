package com.example.backend.service;

import com.example.backend.dto.NoteDto;
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

    public void createNote(NoteDto noteDto){

    }

    public void editNote(NoteDto noteDto){

    }

    public void deleteNote(Long id){
        Note note = noteRepository.findById(id).orElseThrow();
        noteRepository.delete(note);
    }

    public List<Note> getAllNotes(){
        return noteRepository.findAll();
    }
}
