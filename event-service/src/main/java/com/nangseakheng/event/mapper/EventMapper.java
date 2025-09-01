package com.nangseakheng.event.mapper;

import com.nangseakheng.event.dto.EventRequest;
import com.nangseakheng.event.dto.EventResponse;
import com.nangseakheng.event.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    Event toEntity(EventRequest request);

    EventResponse toResponse(Event event);

    List<EventResponse> toResponseList(List<Event> events);
}
