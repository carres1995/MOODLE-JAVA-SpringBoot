package com.riwi.hamilton.model.dto;

import com.riwi.hamilton.utils.Cities;

public record EventVenueDTO(String eventName, String eventDate, String venueName, Cities city) {
}
