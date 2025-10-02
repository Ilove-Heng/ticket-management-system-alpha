package com.nangseakheng.order.controller;


import com.nangseakheng.common.exception.ResponseErrorTemplate;
import com.nangseakheng.order.dto.OrderRequest;
import com.nangseakheng.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping()
    public ResponseEntity<ResponseErrorTemplate> create(@RequestBody OrderRequest orderRequest, HttpServletRequest httpServletRequest) {
        log.info("Intercept create new order with req: {}", orderRequest);
        return ResponseEntity.ok(orderService.createOrder(orderRequest, httpServletRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseErrorTemplate> getById(@PathVariable Long id) {
        log.info("Intercept get order by id: {}", id);
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<ResponseErrorTemplate> cancelOrder(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        log.info("Intercept lock by event id: {}", id);
        return ResponseEntity.ok(orderService.cancelOrder(id, httpServletRequest));
    }

}
