package com.riwi.hamilton;

import com.riwi.hamilton.controller.ui.EventUIController;
import com.riwi.hamilton.model.dto.req.EventCreateDTO;
import com.riwi.hamilton.model.dto.res.EventResDTO;
import com.riwi.hamilton.service.CategoryService;
import com.riwi.hamilton.service.EventService;
import com.riwi.hamilton.service.VenueService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventUIController.class)
class EventUIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService service;

    @MockBean
    private VenueService venueService;

    @MockBean
    private CategoryService categoryService;

    private Slice<EventResDTO> buildSlice() {
        EventResDTO dto = mock(EventResDTO.class);

        return new SliceImpl<>(
                List.of(dto),
                PageRequest.of(0, 5),
                false
        );
    }

    @Test
    @DisplayName("Debe listar todos los eventos sin filtros")
    void shouldShowAllEvents() throws Exception {

        when(service.getAll(0))
                .thenReturn(buildSlice());

        mockMvc.perform(get("/admin/events"))
                .andExpect(status().isOk())
                .andExpect(view().name("events"))
                .andExpect(model().attributeExists("events"))
                .andExpect(model().attributeExists("eventForm"))
                .andExpect(model().attributeExists("venues"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeExists("cities"));

        verify(service).getAll(0);
    }

    @Test
    @DisplayName("Debe buscar por ciudad y categoría")
    void shouldSearchByCityAndCategory() throws Exception {

        when(service.searchByCityAndCategory(
                "Medellin",
                "Music",
                0
        )).thenReturn(buildSlice());

        mockMvc.perform(
                        get("/admin/events")
                                .param("city", "Medellin")
                                .param("category", "Music")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("events"));

        verify(service)
                .searchByCityAndCategory(
                        "Medellin",
                        "Music",
                        0
                );
    }

    @Test
    @DisplayName("Debe buscar solo por ciudad")
    void shouldSearchByCity() throws Exception {

        when(service.searchByCity(
                "Bogota",
                0
        )).thenReturn(buildSlice());

        mockMvc.perform(
                        get("/admin/events")
                                .param("city", "Bogota")
                )
                .andExpect(status().isOk());

        verify(service)
                .searchByCity(
                        "Bogota",
                        0
                );
    }

    @Test
    @DisplayName("Debe buscar solo por categoría")
    void shouldSearchByCategory() throws Exception {

        when(service.searchByCategory(
                "Tech",
                0
        )).thenReturn(buildSlice());

        mockMvc.perform(
                        get("/admin/events")
                                .param("category", "Tech")
                )
                .andExpect(status().isOk());

        verify(service)
                .searchByCategory(
                        "Tech",
                        0
                );
    }

    @Test
    @DisplayName("Debe buscar por rango de fechas")
    void shouldSearchByDateRange() throws Exception {

        when(service.searchByDateRange(
                "2026-01-01",
                "2026-02-01",
                0
        )).thenReturn(buildSlice());

        mockMvc.perform(
                        get("/admin/events")
                                .param("startDate", "2026-01-01")
                                .param("endDate", "2026-02-01")
                )
                .andExpect(status().isOk());

        verify(service)
                .searchByDateRange(
                        "2026-01-01",
                        "2026-02-01",
                        0
                );
    }

    @Test
    @DisplayName("Debe redireccionar cuando el formulario es válido")
    void shouldRedirectWhenFormIsValid() throws Exception {

        when(service.getAll(anyInt()))
                .thenReturn(
                        new SliceImpl<>(List.of())
                );

        when(venueService.findAllVenues())
                .thenReturn(List.of());

        when(categoryService.findAllCategories())
                .thenReturn(List.of());

        mockMvc.perform(
                        post("/admin/events/save")

                                .param(
                                        "name",
                                        "Evento prueba"
                                )

                                .param(
                                        "date",
                                        LocalDate.now().toString()
                                )

                                .param(
                                        "Description",
                                        "Descripcion"
                                )

                                .param(
                                        "venueId",
                                        "1"
                                )

                                .param(
                                        "categoriesId",
                                        "1"
                                )
                )

                .andExpect(
                        status()
                                .is3xxRedirection()
                )

                .andExpect(
                        redirectedUrl(
                                "/admin/events"
                        ));
    }

    @Test
    @DisplayName("Debe retornar la vista cuando hay errores")
    void shouldReturnViewWhenValidationFails() throws Exception {

        when(service.getAll(anyInt()))
                .thenReturn(buildSlice());

        mockMvc.perform(
                        post("/admin/events/save")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("events"))
                .andExpect(model().attributeExists("events"))
                .andExpect(model().attributeExists("venues"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeExists("cities"));
    }
}