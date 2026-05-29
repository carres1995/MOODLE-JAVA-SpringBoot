package com.riwi.hamilton;

import com.riwi.hamilton.controller.ui.VenueUIController;
import com.riwi.hamilton.model.Venue;
import com.riwi.hamilton.service.VenueService;
import com.riwi.hamilton.utils.Cities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * MockMvc tests for VenueUIController (/admin/venues routes)
 * Verifies:
 * - HTTP status 200 OK
 * - Correct view name ("venues")
 * - Expected model attributes (venueForm, venues)
 */
@WebMvcTest(VenueUIController.class)
class VenueUIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VenueService venueService;

    @BeforeEach
    void setUp() {
        // Setup mock data
        List<Venue> venueList = new ArrayList<>();
        Venue venue = new Venue();
        venue.setId(1L);
        venue.setName("Test Venue");
        venue.setCity(Cities.BOGOTA);
        venueList.add(venue);
        
        Mockito.when(venueService.findAllVenues()).thenReturn(venueList);
    }

    @Test
    void testShowVenuesReturns200OK() throws Exception {
        mockMvc.perform(get("/admin/venues"))
                .andExpect(status().isOk());
    }

    @Test
    void testShowVenuesReturnsCorrectViewName() throws Exception {
        mockMvc.perform(get("/admin/venues"))
                .andExpect(view().name("venues"));
    }

    @Test
    void testShowVenuesContainsVenueFormAttribute() throws Exception {
        mockMvc.perform(get("/admin/venues"))
                .andExpect(model().attributeExists("venueForm"));
    }

    @Test
    void testShowVenuesContainsVenuesListAttribute() throws Exception {
        mockMvc.perform(get("/admin/venues"))
                .andExpect(model().attributeExists("venues"));
    }

    @Test
    void testShowVenuesVenueFormIsNotNull() throws Exception {
        mockMvc.perform(get("/admin/venues"))
                .andExpect(model().attribute("venueForm", org.hamcrest.Matchers.notNullValue()));
    }

    @Test
    void testShowVenuesVenuesListIsNotEmpty() throws Exception {
        mockMvc.perform(get("/admin/venues"))
                .andExpect(model().attribute("venues", org.hamcrest.Matchers.hasSize(1)));
    }

    @Test
    void testShowVenuesModelContainsAllRequiredAttributes() throws Exception {
        mockMvc.perform(get("/admin/venues"))
                .andExpect(model().attributeExists("venueForm", "venues"));
    }

    @Test
    void testSaveVenueRedirectsAfterSuccessfulSubmit() throws Exception {
        Mockito.when(venueService.createVenue(Mockito.any())).thenReturn(new Venue());

        mockMvc.perform(post("/admin/venues/save")
                .param("name", "Nuevo Lugar")
                .param("city", "Ciudad Ejemplo"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/venues"));
    }

}
