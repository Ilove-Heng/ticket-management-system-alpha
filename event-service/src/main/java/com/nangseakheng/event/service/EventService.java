package com.nangseakheng.event.service;

import com.nangseakheng.common.exception.ResponseErrorTemplate;
import com.nangseakheng.event.dto.EventRequest;

public interface EventService {

    ResponseErrorTemplate create(EventRequest request);
    ResponseErrorTemplate update(Long id, EventRequest request);
    ResponseErrorTemplate getById(Long id);
    void delete(Long id);
}