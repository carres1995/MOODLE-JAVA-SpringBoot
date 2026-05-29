package com.riwi.hamilton;

import com.riwi.hamilton.controller.ui.EventUIController;
import com.riwi.hamilton.model.dto.EventVenueDTO;
import com.riwi.hamilton.service.EventService;
import com.riwi.hamilton.model.Event;
import com.riwi.hamilton.model.Venue;
import com.riwi.hamilton.service.VenueService;
import com.riwi.hamilton.utils.Cities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * MockMvc tests for EventUIController (/admin/events routes)
 * Verifies:
 * - HTTP status 200 OK
 * - Correct view name ("events")
 * - Expected model attributes (eventForm, venues, events)
 */
@WebMvcTest(EventUIController.class)
class EventUIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @MockBean
    private VenueService venueService;

    @BeforeEach
    void setUp() {
        // Setup mock data - handle any arguments
        List<EventVenueDTO> eventList = new ArrayList<>();
        eventList.add(new EventVenueDTO("Event 1", "2026-06-01", "Venue 1", Cities.BOGOTA));
        Page<EventVenueDTO> eventPage = new PageImpl<>(eventList, Pageable.ofSize(5), 1);
        
        Mockito.when(eventService.getAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(eventPage);
        Mockito.when(venueService.findAllVenues()).thenReturn(new ArrayList<>());
    }

    @Test
    void testShowEventsReturns200OK() throws Exception {
        mockMvc.perform(get("/admin/events"))
                .andExpect(status().isOk());
    }

    @Test
    void testShowEventsReturnsCorrectViewName() throws Exception {
        mockMvc.perform(get("/admin/events"))
                .andExpect(view().name("events"));
    }

    @Test
    void testShowEventsContainsEventFormAttribute() throws Exception {
        mockMvc.perform(get("/admin/events"))
                .andExpect(model().attributeExists("eventForm"));
    }

    @Test
    void testShowEventsContainsEventsAttribute() throws Exception {
        mockMvc.perform(get("/admin/events"))
                .andExpect(model().attributeExists("events"));
    }

    @Test
    void testShowEventsContainsVenuesAttribute() throws Exception {
        mockMvc.perform(get("/admin/events"))
                .andExpect(model().attributeExists("venues"));
    }

    @Test
    void testShowEventsWithPaginationParametersReturns200OK() throws Exception {
        mockMvc.perform(get("/admin/events").param("page", "0").param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void testShowEventsContainsPaginationAttributes() throws Exception {
        mockMvc.perform(get("/admin/events").param("page", "1").param("size", "5"))
                .andExpect(model().attribute("page", 1))
                .andExpect(model().attribute("size", 5));
    }

    @Test
    void testShowEventsModelContainsAllRequiredAttributes() throws Exception {
        mockMvc.perform(get("/admin/events"))
                .andExpect(model().attributeExists("eventForm", "events", "venues", "page", "size"));
    }

    @Test
    void testSaveEventRedirectsAfterSuccessfulSubmit() throws Exception {
        Mockito.when(venueService.createVenue(Mockito.any(Venue.class))).thenReturn(new Venue());
        Mockito.when(eventService.saveEvent(Mockito.any(Event.class))).thenReturn(new Event());

        mockMvc.perform(post("/admin/events/save")
                .param("eventName", "Test Event")
                .param("eventDate", "2026-06-01")
                .param("venueName", "Test Venue")
                .param("city", "Test City"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/events"));
    }

}
