package com.riwi.hamilton.model.dto;

import com.riwi.hamilton.utils.Cities;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class VenueForm {

    @NotBlank(message = "El nombre del venue no puede estar vacío.")
    @Size(min = 4, max = 30, message = "El nombre debe tener entre 4 y 30 caracteres.")
    private String name;

    @NotNull(message = "La ciudad no puede estar vacía.")
    private Cities city;

    public VenueForm() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Cities getCity() {
        return city;
    }

    public void setCity(Cities city) {
        this.city = city;
    }
}
