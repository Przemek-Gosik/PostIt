package com.example.backend.mapper;

import com.example.backend.dto.NoteDto;
import com.example.backend.model.Note;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * Class for mapper of Note and NoteDto
 */
@Mapper
public interface NoteMapper {

    NoteDto toDto(Note note);
    List<NoteDto> toDto(Collection<Note> notes);
    Note fromDto(NoteDto noteDto);

}
