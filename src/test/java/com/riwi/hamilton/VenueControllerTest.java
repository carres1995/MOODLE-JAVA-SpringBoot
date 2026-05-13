package com.riwi.hamilton;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.riwi.hamilton.controller.VenueController;
import com.riwi.hamilton.model.Venue;
import com.riwi.hamilton.service.VenueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VenueController.class)
public class VenueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private VenueService service;

    @Test
    void GetAllVenues() throws Exception {
        Venue venue = new Venue(1L, "Riwi", "Medellin");
        Venue venue2 = new Venue(2L, "Staf", "Cali");

        when(service.findAllVenues()).thenReturn(Arrays.asList(venue, venue2));

        mockMvc.perform(get("/venues/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Riwi"));
    }

    @Test
    void saveVenues() throws Exception {
        Venue venue = new Venue(1L, "Riwi", "Medellin");

        when(service.createVenue(any(Venue.class))).thenReturn(true);

        mockMvc.perform(post("/venues/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(venue)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void getById() throws Exception {
        Venue venue = new Venue(1L, "Riwi", "Medellin");

        when(service.getById(1L)).thenReturn(Optional.of(venue));

        mockMvc.perform(get("/venues/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Riwi"));
    }

    @Test
    void updateVenue() throws Exception {
        Venue venue = new Venue(1L, "Riwi", "Medellin");

        when(service.update(eq(1L), any(Venue.class))).thenReturn(true);

        mockMvc.perform(put("/venues/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(venue)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}
