package com.example.backend.controller;

import com.example.backend.dto.NoteDto;
import com.example.backend.service.NoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.List;
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

        resultActions.andExpect(status().isBadRequest());

    }
}
