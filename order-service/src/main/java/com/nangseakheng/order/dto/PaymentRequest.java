package com.nangseakheng.order.dto;

import com.nangseakheng.order.enumz.PaymentMethod;
import com.nangseakheng.order.enumz.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

    private Long orderId;

    private String username;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private BigDecimal amount;

    private String currency;

    private String description;
}
