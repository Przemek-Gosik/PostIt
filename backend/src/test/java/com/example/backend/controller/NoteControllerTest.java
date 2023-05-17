package com.example.backend.controller;

import com.example.backend.dto.NoteDto;
import com.example.backend.service.NoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static java.util.stream.Stream.generate;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@WebMvcTest(NoteController.class)
public class NoteControllerTest {

    private final static String NOTE_CRUD_ENDPOINT = "/api/note";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;

    @Test
    void getAllNotes_givenEmptyBase_Get204Response() throws Exception {
        List<NoteDto> notes = new ArrayList<>();
        given(noteService.getAllNotes()).willReturn(notes);

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
}
