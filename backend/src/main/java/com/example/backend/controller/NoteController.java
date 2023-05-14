package com.example.backend.controller;

import com.example.backend.dto.NoteDto;
import com.example.backend.service.NoteService;
import jakarta.validation.Valid;
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
import java.util.List;

/**
 * This controller is for notes CRUD operations
 */
@AllArgsConstructor
@RestController
@RequestMapping(
        path = "/api/note",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class NoteController {

    private final NoteService noteService;

    /**
     * Method to create new note and save in database
     *
     * @param noteDto is object with data to create new note
     * @return is object with data of new note and status code 201
     */
    @PostMapping
    public ResponseEntity<NoteDto> createNote(@Valid @RequestBody NoteDto noteDto){
        NoteDto createdNote = noteService.createNote(noteDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNote);
    }

    /**
     * Method to get note from database by id
     *
     * @param id is identifier of note to get
     * @return object with data of note and status code 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<NoteDto> getNoteById(@PathVariable("id") Long id){
        return ResponseEntity.ok(noteService.getNoteById(id));
    }

    /**
     * Method to get all notes from database
     *
     * @return is list of object with data of notes
     * if list is empty status is 204
     */
    @GetMapping()
    public ResponseEntity<List<NoteDto>> getAllNotes(){
        List<NoteDto> notes =  noteService.getAllNotes();
        if(notes.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }else {
            return ResponseEntity.ok(notes);
        }
    }

    /**
     * Method to edit note and save changes in databse
     *
     * @param noteDto is object with id of note to edit
     * @return is object with new note and status code 200
     */
    @PutMapping("")
    public ResponseEntity<NoteDto> editNote(@Valid @RequestBody NoteDto noteDto){
        return ResponseEntity.ok(noteService.editNote(noteDto));
    }

    /**
     * Method to delete note for database
     *
     * @param id is identifier of note to delete
     * @return is status 200
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable("id") Long id){
        noteService.deleteNote(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
