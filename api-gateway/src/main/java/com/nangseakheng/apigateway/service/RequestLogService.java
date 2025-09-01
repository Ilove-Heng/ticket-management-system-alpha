package com.nangseakheng.apigateway.service;

import com.nangseakheng.apigateway.entity.RequestLog;
import reactor.core.publisher.Mono;


public interface RequestLogService {

    Mono<RequestLog> save(RequestLog requestLog);

}
