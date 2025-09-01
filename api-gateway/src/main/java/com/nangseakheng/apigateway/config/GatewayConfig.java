package com.nangseakheng.apigateway.config;

import com.nangseakheng.apigateway.repository.ApiRouteRepository;
import com.nangseakheng.apigateway.service.impl.RouteLocatorDetail;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(ApiRouteRepository apiRouteRepository,
                               RouteLocatorBuilder routeLocatorBuilder) {
        return new RouteLocatorDetail(apiRouteRepository, routeLocatorBuilder);
    }
}
