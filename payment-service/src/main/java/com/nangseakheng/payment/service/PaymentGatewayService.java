package com.nangseakheng.payment.service;

import com.nangseakheng.payment.dto.PaymentRequest;

public interface PaymentGatewayService {
    boolean processPayment(PaymentRequest paymentRequest);
}
