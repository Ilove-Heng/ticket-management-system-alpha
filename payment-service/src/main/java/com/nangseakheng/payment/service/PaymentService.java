package com.nangseakheng.payment.service;

import com.nangseakheng.common.exception.ResponseErrorTemplate;
import com.nangseakheng.payment.dto.PaymentRequest;

public interface PaymentService {
    ResponseErrorTemplate processPayment(PaymentRequest paymentRequest);

    // get by transaction id
    // get by payment id

    // refund payment
}
