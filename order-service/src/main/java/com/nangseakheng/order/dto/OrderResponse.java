package com.nangseakheng.order.dto;

import com.nangseakheng.order.enumz.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderResponse {

    private Long eventId;
    private Long ticketId;
    private Integer quantity;
    private BigDecimal amount;
    private OrderStatus orderStatus;
    private LocalDateTime orderDate;
    private String paymentId;
}
