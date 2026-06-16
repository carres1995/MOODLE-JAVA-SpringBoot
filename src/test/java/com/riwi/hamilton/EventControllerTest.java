package com.riwi.hamilton;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.riwi.hamilton.controller.EventController;
import com.riwi.hamilton.model.Event;
import com.riwi.hamilton.model.Venue;
import com.riwi.hamilton.model.dto.EventVenueDTO;
import com.riwi.hamilton.model.dto.req.EventCreateDTO;
import com.riwi.hamilton.model.dto.req.EventUpdateDTO;
import com.riwi.hamilton.model.dto.res.EventResDTO;
import com.riwi.hamilton.service.EventService;
import com.riwi.hamilton.utils.Cities;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * DOCUMENTACIÓN DE ANOTACIONES:
 *
 * @WebMvcTest(EventController.class):
 * Se usa para testear la capa de controladores (Spring MVC). Solo carga los
 * componentes
 * necesarios para web (Controladores, Filtros, Advice, etc.) en lugar de todo
 * el contexto.
 * En el parámetro le indicamos qué controlador específico queremos testear.
 *
 * @Autowired:
 *             Permite a Spring inyectar automáticamente las dependencias. Aquí
 *             lo usamos para
 *             inyectar MockMvc y ObjectMapper.
 *
 * @MockBean:
 *            Crea un "mock" (simulacro) de una clase y lo añade al contexto de
 *            Spring.
 *            Sustituye el bean real de EventService por uno falso que podemos
 *            controlar en los tests.
 *
 * @Test:
 *        Indica que el método es un caso de prueba de JUnit 5.
 */
@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventService eventService;



    @Test
    void shouldGetAllEvents() throws Exception {

        EventResDTO dto1 =
                new EventResDTO(
                        1L,
                        "Concierto",
                        LocalDate.of(
                                2024,
                                6,
                                15
                        ),
                        "Staf Theater",
                        "Auditorio Sur",
                        Set.of(
                                "Technology"
                        )
                );


        EventResDTO dto2 =
                new EventResDTO(
                        2L,
                        "Teatro",
                        LocalDate.of(
                                2024,
                                6,
                                5
                        ),
                        "Main Theater",
                        "Estadio Caribe",
                        Set.of(
                                "Art"
                        )
                );


        Slice<EventResDTO> response =

                new SliceImpl<>(

                        List.of(
                                dto1,
                                dto2
                        ),

                        PageRequest.of(
                                0,
                                10
                        ),

                        false
                );


        when(
                eventService.getAll(0)
        ).thenReturn(
                response
        );


        mockMvc.perform(
                        get(
                                "/api/events/"
                        )
                )

                .andExpect(
                        status().isOk()
                )

                .andExpect(
                        jsonPath(
                                "$.events.length()"
                        )
                                .value(
                                        2
                                )
                )

                .andExpect(
                        jsonPath(
                                "$.events[0].name"
                        )
                                .value(
                                        "Concierto"
                                )
                );

    }



    @Test
    void shouldGetEventById()
            throws Exception {


        EventResDTO dto =

                new EventResDTO(

                        1L,

                        "Concierto",

                        LocalDate.of(
                                2024,
                                5,
                                12
                        ),

                        "Arena",

                        "Bogota",

                        Set.of(
                                "Music"
                        )
                );


        when(
                eventService.getById(
                        1L
                )
        )

                .thenReturn(
                        dto
                );


        mockMvc.perform(
                        get(
                                "/api/events/1"
                        )
                )

                .andExpect(
                        status().isOk()
                )

                .andExpect(
                        jsonPath(
                                "$.name"
                        )
                                .value(
                                        "Concierto"
                                )
                );

    }



    @Test
    void shouldSaveEvent()
            throws Exception {


        EventCreateDTO request =

                new EventCreateDTO(
                        1L,

                        "Nuevo Evento",

                        LocalDate.of(
                                2024,
                                7,
                                20
                        ),

                        "Main Arena",

                        1L,

                        Set.of(
                                1L,
                                2L
                        )
                );


        EventResDTO response =

                new EventResDTO(

                        1L,

                        "Nuevo Evento",

                        LocalDate.of(
                                2024,
                                7,
                                20
                        ),

                        "Main Arena",

                        "Bogota",

                        Set.of(
                                "Technology"
                        )
                );


        when(
                eventService.saveEvent(
                        any(
                                EventCreateDTO.class
                        )
                )
        )

                .thenReturn(
                        response
                );


        mockMvc.perform(

                        post(
                                "/api/events/"
                        )

                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )

                                .content(
                                        objectMapper
                                                .writeValueAsString(
                                                        request
                                                )
                                )

                )

                .andExpect(
                        status().isCreated()
                )

                .andExpect(
                        jsonPath(
                                "$.id"
                        )
                                .value(
                                        1
                                )
                )

                .andExpect(
                        jsonPath(
                                "$.name"
                        )
                                .value(
                                        "Nuevo Evento"
                                )
                );

    }



    @Test
    void shouldUpdateEvent()
            throws Exception {


        EventUpdateDTO request =

                new EventUpdateDTO(

                        null,

                        LocalDate.of(
                                2024,
                                8,
                                10
                        ),

                        null,

                        null,
                        null
                );


        EventResDTO response =

                new EventResDTO(

                        1L,

                        "Evento Actualizado",

                        LocalDate.of(
                                2024,
                                8,
                                10
                        ),

                        "Arena",

                        "Bogota",

                        Set.of(
                                "Music"
                        )
                );


        when(
                eventService.update(
                        eq(
                                1L
                        ),

                        any(
                                EventUpdateDTO.class
                        )
                )
        )

                .thenReturn(
                        response
                );


        mockMvc.perform(

                        put(
                                "/api/events/update/1"
                        )

                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )

                                .content(
                                        objectMapper
                                                .writeValueAsString(
                                                        request
                                                )
                                )

                )

                .andExpect(
                        status().isOk()
                )

                .andExpect(
                        jsonPath(
                                "$.name"
                        )
                                .value(
                                        "Evento Actualizado"
                                )
                );

    }

}