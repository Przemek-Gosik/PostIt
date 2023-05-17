package com.example.backend.controller;

import com.example.backend.BackendApplication;
import com.example.backend.dto.NoteDto;
import com.example.backend.model.Note;
import com.example.backend.repository.NoteRepository;
import com.example.backend.service.NoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import static java.util.stream.Stream.generate;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = BackendApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
@EnableAutoConfiguration
@Transactional
public class NoteControllerTest {

    private final static String NOTE_CRUD_ENDPOINT = "/api/note";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteRepository noteRepository;

    @Test
    void getAllNotes_savedNotes_GivenDataInResponse() throws Exception {
        Note note1 = new Note();
        note1.setText("text1");
        Note note2 = new Note();
        note2.setText("text2");
        List<Note> notes = Arrays.asList(note1,note2);
        noteRepository.saveAllAndFlush(notes);

        ResultActions resultActions = mockMvc.perform(get(NOTE_CRUD_ENDPOINT));
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",is(notes.size())))
                .andExpect(jsonPath("$[*].text",containsInAnyOrder(note1.getText(),note2.getText())));


    }

    @Test
    void getAllNotes_givenEmptyBase_Get204Response() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(NOTE_CRUD_ENDPOINT));
        resultActions.andExpect(status().isNoContent());
    }

    @Test
    void createNote_givenNoteWithEmptyText_Get400Response() throws Exception {
        ObjectMapper obj = new ObjectMapper();
        NoteDto noteDto = new NoteDto();
        noteDto.setText("");
        ResultActions resultActions = mockMvc.perform(post(NOTE_CRUD_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(obj.writeValueAsBytes(noteDto)));

        resultActions.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.statusCode",is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message",is("Validation failed for request")))
                .andExpect(jsonPath("$.details[0]",is("Text can't be empty!")));
    }

    @Test
    void createNote_givenNoteWithTextOver200Characters_Get400Response() throws Exception{

        ObjectMapper obj = new ObjectMapper();
        NoteDto noteDto = new NoteDto();
        String text = generate(()->"A")
                .limit(201)
                .collect(Collectors.joining());
        noteDto.setText(text);
        ResultActions resultActions = mockMvc.perform(post(NOTE_CRUD_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(obj.writeValueAsBytes(noteDto)));

        resultActions.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.statusCode",is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message",is("Validation failed for request")))
                .andExpect(jsonPath("$.details[0]",is("Text can't be longer than 200 characters!")))
                .andExpect(jsonPath("$.path",containsString(NOTE_CRUD_ENDPOINT)));
    }

    @Test
    void createNote_notGivenBodyToRequest_Get400Response() throws Exception {

        ResultActions resultActions = mockMvc.perform(post(NOTE_CRUD_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.statusCode",is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message",is("HttpMessageNotReadableException")));
    }

    @Test
    void editNote_GivenNewText_NoteChanged()throws Exception{
        Note note = new Note();
        note.setText("Test text1");
        note = noteRepository.saveAndFlush(note);
        String newText = "Test text2";
        NoteDto newNote = new NoteDto(note.getId(),newText);

        ObjectMapper obj = new ObjectMapper();

        ResultActions resultActions = mockMvc.perform(put(NOTE_CRUD_ENDPOINT.concat("/"+newNote.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(obj.writeValueAsBytes(newNote)));

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id",is(note.getId().intValue())))
                .andExpect(jsonPath("$.text",is(newText)));
    }

    @Test
    void editNote_GivenOver200Characters_Get400Response() throws Exception {
        Note note = new Note();
        note.setText("Test text1");
        note = noteRepository.saveAndFlush(note);
        String newText = generate(()->"A")
                .limit(201)
                .collect(Collectors.joining());
        NoteDto newNote = new NoteDto(note.getId(),newText);

        ObjectMapper obj = new ObjectMapper();

        ResultActions resultActions = mockMvc.perform(put(NOTE_CRUD_ENDPOINT.concat("/"+note.getId().toString()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(obj.writeValueAsBytes(newNote)));

        resultActions.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.statusCode",is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message",is("Validation failed for request")))
                .andExpect(jsonPath("$.details[0]",is("Text can't be longer than 200 characters!")))
                .andExpect(jsonPath("$.path",containsString(NOTE_CRUD_ENDPOINT)));
    }
}
