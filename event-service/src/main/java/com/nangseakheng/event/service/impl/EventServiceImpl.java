package com.nangseakheng.event.service.impl;

import com.nangseakheng.common.constant.ApiConstant;
import com.nangseakheng.common.dto.EmptyObject;
import com.nangseakheng.common.exception.ResponseErrorTemplate;
import com.nangseakheng.event.dto.EventRequest;
import com.nangseakheng.event.dto.EventResponse;
import com.nangseakheng.event.entity.Event;
import com.nangseakheng.event.mapper.EventMapper;
import com.nangseakheng.event.repository.EventRepository;
import com.nangseakheng.event.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventMapper eventMapper;
    private final EventRepository eventRepository;

    public EventServiceImpl(EventMapper eventMapper, EventRepository eventRepository) {
        this.eventMapper = eventMapper;
        this.eventRepository = eventRepository;
    }

    @Override
    public ResponseErrorTemplate create(EventRequest request) {
        Event event = eventMapper.toEntity(request);
        event.setEventDate(LocalDateTime.now());
        eventRepository.save(event);
        EventResponse eventResponse = eventMapper.toResponse(event);
        return new ResponseErrorTemplate(
                ApiConstant.SUCCESS.getDescription(),
                ApiConstant.SUCCESS.getKey(),
                eventResponse,
                false);
    }

    @Override
    public ResponseErrorTemplate update(Long id, EventRequest request) {
        Optional<Event> event = eventRepository.findById(id);
        if(event.isEmpty()) {
            return new ResponseErrorTemplate(
                    ApiConstant.DATA_NOT_FOUND.getDescription(),
                    ApiConstant.DATA_NOT_FOUND.getKey(),
                    new EmptyObject(),
                    true);
        }
        event.get().setEventDate(request.getEventDate());
        event.get().setTitle(request.getTitle());
        event.get().setDescription(request.getDescription());
        event.get().setLocation(request.getLocation());
        event.get().setEventType(request.getEventType());
        event.get().setStatus(request.getStatus());

        eventRepository.save(event.get());

        return new ResponseErrorTemplate(
                ApiConstant.SUCCESS.getDescription(),
                ApiConstant.SUCCESS.getKey(),
                eventMapper.toResponse(event.get()),
                false);
    }

    @Override
    public ResponseErrorTemplate getById(Long id) {
        Optional<Event> event = eventRepository.findById(id);
        if(event.isEmpty()){
            return new ResponseErrorTemplate(
                    ApiConstant.DATA_NOT_FOUND.getDescription(),
                    ApiConstant.DATA_NOT_FOUND.getKey(),
                    new EmptyObject(),
                    true);
        }
        return new ResponseErrorTemplate(
                ApiConstant.SUCCESS.getDescription(),
                ApiConstant.SUCCESS.getKey(),
                eventMapper.toResponse(event.get()),
                false);

    }

    @Override
    public void delete(Long id) {
        eventRepository.deleteById(id);
    }
}