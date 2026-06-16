package com.riwi.hamilton.mapper;

import com.riwi.hamilton.model.Category;
import com.riwi.hamilton.model.Event;
import com.riwi.hamilton.model.dto.req.EventCreateDTO;
import com.riwi.hamilton.model.dto.req.EventUpdateDTO;
import com.riwi.hamilton.model.dto.res.EventResDTO;
import org.mapstruct.*;


import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",//le indica a mapstruct que añada esta implementacion como un component de spring añade anotacion @Component,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE //indica que cuando un campo en el origen es null no debe sobrescribir el valor existente
)
public interface MapperEvent {
    @Mapping(source = "venue.name", target = "venueName")
    @Mapping(source = "categories", target = "categoryNames", qualifiedByName = "mapCategoriesToNames")
    EventResDTO toResponseDTO(Event event);

    @Named("mapCategoriesToNames")
    default Set<String> mapCategoriesToNames(Set<Category> categories){
        if(categories == null) return null;
        return categories.stream().map(Category::getName).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "venue", ignore = true)
    @Mapping(target = "categories", ignore = true)
    Event toEntity(EventCreateDTO dto);

    void updateEntityFromDTO(EventUpdateDTO dto, @MappingTarget Event event);
}
