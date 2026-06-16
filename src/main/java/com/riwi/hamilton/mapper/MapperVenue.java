package com.riwi.hamilton.mapper;

import com.riwi.hamilton.model.Venue;
import com.riwi.hamilton.model.dto.req.VenueCreateDTO;
import com.riwi.hamilton.model.dto.req.VenueUpdateDTO;
import com.riwi.hamilton.model.dto.res.VenueResDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MapperVenue{
    VenueResDTO toResponse(Venue venue);

    @Mapping(target = "id", ignore = true)
    Venue toEntity(VenueCreateDTO dto);

    void updateEntity(VenueUpdateDTO dto, @MappingTarget Venue venue);
}
