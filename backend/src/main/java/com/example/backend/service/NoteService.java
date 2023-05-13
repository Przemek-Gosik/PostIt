package com.example.backend.service;

import com.example.backend.dto.NoteDto;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.NoteMapper;
import com.example.backend.model.Note;
import com.example.backend.model.User;
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
    private final UserService userService;
    private final NoteMapper noteMapper;

    public NoteDto createNote(NoteDto noteDto){
        User user = userService.getUserFromAuthentication();
        Note note = noteMapper.fromDto(noteDto);
        note.setUser(user);
        note = noteRepository.save(note);
        return noteMapper.toDto(note);

    }

    public NoteDto editNote(NoteDto noteDto){
        User user = userService.getUserFromAuthentication();
        Note note = findNote(noteDto.getId(),user);
        note.setText(noteDto.getText());
        note = noteRepository.save(note);
        return noteMapper.toDto(note);
    }

    public void deleteNote(Long id){
        User user = userService.getUserFromAuthentication();
        Note note = findNote(id,user);
        noteRepository.delete(note);
    }

    public List<Note> getAllNotes(){
        User user = userService.getUserFromAuthentication();
        return noteRepository.findAllByUser(user);
    }

    private Note findNote(Long id,User user){
        return  noteRepository.findByIdAndUser(id,user)
                .orElseThrow(()->new ResourceNotFoundException("Note not found for id "+id));
    }
}
