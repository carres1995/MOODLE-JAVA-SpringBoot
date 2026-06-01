package com.riwi.hamilton.model.dto;

import com.riwi.hamilton.utils.Cities;

import java.util.List;

public record EventVenueDTO(String eventName, String eventDate, String venueName, Cities city,
                            List<String> categories, Long venueId, List<Long> categoryIds) {
}
