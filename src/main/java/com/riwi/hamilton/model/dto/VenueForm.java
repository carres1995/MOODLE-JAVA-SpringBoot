package com.riwi.hamilton.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class VenueForm {

    @NotBlank(message = "El nombre del venue no puede estar vacío.")
    @Size(min = 4, max = 30, message = "El nombre debe tener entre 4 y 30 caracteres.")
    private String name;

    @NotBlank(message = "La ciudad no puede estar vacía.")
    private String city;

    public VenueForm() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
