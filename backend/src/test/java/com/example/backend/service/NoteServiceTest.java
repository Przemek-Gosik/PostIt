package com.example.backend.service;

import com.example.backend.dto.NoteDto;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.mapper.NoteMapper;
import com.example.backend.model.Note;
import com.example.backend.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Optional;

@ExtendWith({MockitoExtension.class})
public class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private NoteMapper noteMapper;

    @InjectMocks
    private NoteService noteService;

    private Note note1;


    @BeforeEach
    public void init(){
        note1 = new Note(1L,"text1");

    }

    @Test
    public void getNoteById_GivenValidId_GetNote(){
        Long id = note1.getId();
        NoteDto noteDto = new NoteDto(id,note1.getText());
        when(noteRepository.findById(id)).thenReturn(Optional.of(note1));
        when(noteMapper.toDto(note1)).thenReturn(noteDto);
        assertEquals(noteDto,noteService.getNoteById(id));
    }

    @Test
    public void getNoteById_GivenInvalidId_ResourceNotFoundExceptionThrown(){
        Long id = 2L;
        when(noteRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,()->noteService.getNoteById(id));
    }

    @Test
    public void deleteNoteById_GivenValidId_NoteDeleted(){
        Long id=note1.getId();
        when(noteRepository.findById(id)).thenReturn(Optional.of(note1));
        noteService.deleteNote(id);
        verify(noteRepository,times(1)).delete(note1);

    }

    @Test
    public void deleteNoteById_GivenInvalidId_ResourceNotFoundExceptionThrown(){
        Long id = 2L;
        when(noteRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,()->noteService.deleteNote(id));
    }

}
