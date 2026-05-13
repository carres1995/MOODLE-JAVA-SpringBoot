package com.riwi.hamilton;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.riwi.hamilton.controller.EventController;
import com.riwi.hamilton.model.Event;
import com.riwi.hamilton.service.EventService;


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

public class EventControllerTest {
    @Autowired
    private MockMvc mockMvc;// Cliente para realizar peticiones HTTP simuladas
    @Autowired
    private ObjectMapper objectMapper; // Para convertir objetos a JSON y viceversa

    @MockBean
    private EventService eventService; // Simulamos el servicio para no depender de la DB o lógica real

    @Test
    void testGetAllEvents() throws Exception {
        // Configuramos el comportamiento del mock
        Event event1 = new Event(1L, "Concierto", "2024-05-12");
        Event event2 = new Event(2L, "Teatro", "2024-06-15");
        when(eventService.getAll()).thenReturn(Arrays.asList(event1, event2));

        // Realizamos la petición y verificamos resultados
        mockMvc.perform(get("/events/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Concierto"));
    }

    @Test
    void testGetEventById() throws Exception {
        Event event = new Event(1L, "Concierto", "2024-05-12");
        when(eventService.getById(1L)).thenReturn(Optional.of(event));

        mockMvc.perform(get("/events/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Concierto"));
    }

    @Test
    void testSaveEvent() throws Exception {
        Event event = new Event(null, "Nuevo Evento", "2024-07-20");
        when(eventService.saveEvent(any(Event.class))).thenReturn(true);

        mockMvc.perform(post("/events/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testUpdateEvent() throws Exception {
        Event event = new Event(1L, "Evento Actualizado", "2024-08-10");
        when(eventService.update(eq(1L), any(Event.class))).thenReturn(true);

        mockMvc.perform(put("/events/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}