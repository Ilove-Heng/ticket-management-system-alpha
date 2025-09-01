package com.nangseakheng.apigateway.service;

import com.nangseakheng.apigateway.dto.RouteApiRequest;
import com.nangseakheng.apigateway.dto.RouteApiResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ApiRouteService {
    //  NOTE: mono is for single object, flux is for multiple objects
    Mono<RouteApiResponse> create(RouteApiRequest request);
    Mono<RouteApiResponse> update(Long id, RouteApiRequest request);
    Mono<RouteApiResponse> findById(Long id);
    Flux<RouteApiResponse> findAll();
    Mono<Void> deleteById(Long id);
    Mono<Void> deleteAll();

}
