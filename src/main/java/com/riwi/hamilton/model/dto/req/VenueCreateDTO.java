package com.riwi.hamilton.model.dto.req;

import com.riwi.hamilton.utils.Cities;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VenueCreateDTO(
        @NotBlank(message = "nombre del lugar no puede estar vacio")
        @Size(min = 2, max = 255, message = "El titulo debe tener entre 2 y 255 caracteres")
        String name,
        @NotBlank(message = "Nombre de la ciudad no puede estar vacio")
        @Size(min = 2, max = 255, message = "El nombre debe tener entre 2 y 255 caracteres")
        Cities city
) {
}
