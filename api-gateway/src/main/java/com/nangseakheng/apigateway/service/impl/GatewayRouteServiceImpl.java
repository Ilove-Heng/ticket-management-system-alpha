package com.nangseakheng.apigateway.service.impl;

import com.nangseakheng.apigateway.service.GatewayRouteService;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class GatewayRouteServiceImpl implements GatewayRouteService {

    private final ApplicationEventPublisher applicationEventPublisher;

    // Constructor injection
    public GatewayRouteServiceImpl(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void refreshRoutes() {
        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }
}