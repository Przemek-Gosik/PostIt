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

    @Test
    public void createNote_GivenValidDto_GetNewNote(){
        NoteDto noteDto = new NoteDto();
        noteDto.setText(note1.getText());
        when(noteMapper.fromDto(noteDto)).thenReturn(note1);
        when(noteRepository.save(note1)).thenReturn(note1);
        noteService.createNote(noteDto);
        verify(noteRepository,times(1)).save(note1);
    }

    @Test
    public void editNote_GivenValidDto_GetNewNote(){
        String newText = "NewText";
        NoteDto newNoteDto = new NoteDto(note1.getId(), newText);

        when(noteRepository.findById(note1.getId())).thenReturn(Optional.of(note1));
        when(noteRepository.save(note1)).thenReturn(note1);
        when(noteMapper.toDto(note1)).thenReturn(newNoteDto);

        assertEquals(newNoteDto,noteService.editNote(note1.getId(),newNoteDto));
        verify(noteRepository,times(1)).save(note1);
    }

    @Test
    public void editNote_GivenInvalidId_ExceptionThrown(){
        String newText = "NewText";
        Long id = 2L;
        NoteDto newNoteDto = new NoteDto(note1.getId(), newText);

        when(noteRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,()->noteService.editNote(id,newNoteDto));
    }

}
