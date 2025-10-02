package com.nangseakheng.order.service;

import com.nangseakheng.common.exception.ResponseErrorTemplate;
import com.nangseakheng.order.dto.OrderRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface OrderService {
    ResponseErrorTemplate createOrder(OrderRequest orderRequest, HttpServletRequest httpServletRequest);
    ResponseErrorTemplate getOrderById(Long orderId);
    ResponseErrorTemplate cancelOrder(Long orderId, HttpServletRequest httpServletRequest);
}
