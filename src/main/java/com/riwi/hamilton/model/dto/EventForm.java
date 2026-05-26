package com.riwi.hamilton.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EventForm {

    @NotBlank(message = "El nombre del evento no puede estar vacío.")
    @Size(min = 4, max = 30, message = "El nombre debe tener entre 4 y 30 caracteres.")
    private String name;

    @NotBlank(message = "La fecha no puede estar vacía.")
    private String date;

    @NotNull(message = "Debe seleccionar un venue.")
    private Long venueId;

    public EventForm() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }
}
