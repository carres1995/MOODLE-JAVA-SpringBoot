package com.riwi.hamilton.model.dto.res;

import java.time.LocalDate;
import java.util.Set;

public record EventResDTO(
        Long id,
        String name,
        LocalDate date,
        String description,
        String venueName,
        Set<String> categoryNames
) {
}
