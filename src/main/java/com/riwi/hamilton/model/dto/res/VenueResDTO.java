package com.riwi.hamilton.model.dto.res;

import com.riwi.hamilton.utils.Cities;

public record VenueResDTO(
        Long id,
        String name,
        Cities city
) {
}
