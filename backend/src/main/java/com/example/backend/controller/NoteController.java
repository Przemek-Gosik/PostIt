package com.example.backend.controller;

import com.example.backend.dto.NoteDto;
import com.example.backend.service.NoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(
        path = "/api/note",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class NoteController {

    private final NoteService noteService;

    @PostMapping
    public ResponseEntity<NoteDto> createNote(@Valid @RequestBody NoteDto noteDto){
        NoteDto createdNote = noteService.createNote(noteDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNote);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteDto> getNoteById(@PathVariable("id") Long id){
        return ResponseEntity.ok(noteService.getNoteById(id));
    }

    @GetMapping()
    public ResponseEntity<List<NoteDto>> getAllNotes(){
        List<NoteDto> notes =  noteService.getAllNotes();
        if(notes.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }else {
            return ResponseEntity.ok(notes);
        }
    }

    @PutMapping("")
    public ResponseEntity<NoteDto> editNote(@Valid @RequestBody NoteDto noteDto){
        return ResponseEntity.ok(noteService.editNote(noteDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable("id") Long id){
        noteService.deleteNote(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
