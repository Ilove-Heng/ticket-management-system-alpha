package com.nangseakheng.apigateway.service.impl;

import com.nangseakheng.apigateway.entity.RequestLog;
import com.nangseakheng.apigateway.repository.RequestLogRepository;
import com.nangseakheng.apigateway.service.RequestLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
public class RequestLogServiceImpl implements RequestLogService {

    private final RequestLogRepository requestLogRepository;

    public RequestLogServiceImpl(RequestLogRepository requestLogRepository) {
        this.requestLogRepository = requestLogRepository;
    }

    @Override
    public Mono<RequestLog> save(RequestLog requestLog) {
        if (requestLog.getCreatedAt() == null) {
            requestLog.setCreatedAt(LocalDateTime.now());
        }
        return requestLogRepository.save(requestLog)
                .doOnSuccess(saved -> log.debug("Saved request log with ID: {}", saved.getId()))
                .doOnError(error -> log.error("Error saving request log: {}", error.getMessage()));
    }
}
