package com.nangseakheng.order.service.impl;

import com.nangseakheng.common.constant.ApiConstant;
import com.nangseakheng.common.dto.EmptyObject;
import com.nangseakheng.common.exception.ResponseErrorTemplate;
import com.nangseakheng.order.client.PaymentClient;
import com.nangseakheng.order.client.UserClient;
import com.nangseakheng.order.enumz.OrderConfirmedEvent;
//import com.nangseakheng.order.dto.OrderConfirmedEvent;
import com.nangseakheng.order.dto.OrderRequest;
import com.nangseakheng.order.dto.PaymentRequest;
import com.nangseakheng.order.entity.Order;
import com.nangseakheng.order.enumz.OrderStatus;
import com.nangseakheng.order.enumz.PaymentMethod;
import com.nangseakheng.order.mapper.OrderMapper;
import com.nangseakheng.order.producer.OrderConfirmedKafkaProducer;
import com.nangseakheng.order.repository.OrderRepository;
import com.nangseakheng.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Optional;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserClient userClient;
    private final OrderMapper orderMapper;
    private  final PaymentClient paymentClient;
    private final OrderConfirmedKafkaProducer orderConfirmedKafkaProducer;

    public OrderServiceImpl(OrderRepository orderRepository, UserClient userClient, OrderMapper orderMapper, PaymentClient paymentClient, OrderConfirmedKafkaProducer orderConfirmedKafkaProducer) {
        this.orderRepository = orderRepository;
        this.userClient = userClient;
        this.orderMapper = orderMapper;
        this.paymentClient = paymentClient;
        this.orderConfirmedKafkaProducer = orderConfirmedKafkaProducer;
    }

    @Override
    public ResponseErrorTemplate createOrder(OrderRequest orderRequest, HttpServletRequest httpServletRequest) {
        // Check user authentication and authorization
        String username = handleUnauthorized(httpServletRequest);
        if(!StringUtils.hasText(username)) {
            return new ResponseErrorTemplate(
                    ApiConstant.UN_AUTHORIZATION.getDescription(),
                    ApiConstant.UN_AUTHORIZATION.getKey(),
                    new EmptyObject(),
                    true);
        }
        // Payment processing logic would go here

        // Create order in the database
        Order order = orderMapper.toEntity(orderRequest);
        order.setEventId(orderRequest.getEventId()); // need to check event service
        order.setTicketId(orderRequest.getTicketId()); // need to check ticket service
        order.setUsername(username);
        order.setOrderStatus(OrderStatus.PROCESSING);
        order.setOrderDate(LocalDateTime.now());

        orderRepository.save(order);

        // Process payment: can move to new method or service
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setOrderId(order.getId());
        paymentRequest.setUsername(username);
        paymentRequest.setAmount(orderRequest.getAmount());
        paymentRequest.setCurrency("USD");
        paymentRequest.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        paymentRequest.setDescription("Payment for order ID: " + order.getId());

        ResponseErrorTemplate paymentResponse = paymentClient.processingPayment(paymentRequest)
                .block();

        if(paymentResponse == null || paymentResponse.isError()) {
            log.error("Payment processing failed for order ID: {}", order.getId());
            return new ResponseErrorTemplate(
                    ApiConstant.PAYMENT_FAILED.getDescription(),
                    ApiConstant.PAYMENT_FAILED.getKey(),
                    new EmptyObject(),
                    true);
        }

        order.setOrderStatus(OrderStatus.COMPLETED);
        Integer paymentId = (Integer) ((LinkedHashMap<?, ?>) paymentResponse.data()).get("paymentId");
        order.setPaymentId(Long.valueOf(paymentId));
        orderRepository.save(order);

        // Send order confirmed event to Kafka
        OrderConfirmedEvent orderConfirmedEvent = new OrderConfirmedEvent();
        orderConfirmedEvent.setOrderId(order.getId());
        orderConfirmedEvent.setUsername(order.getUsername());
        orderConfirmedEvent.setEmail("codestorykh@gmail.com"); // need to get from user service
        orderConfirmedEvent.setPhoneNumber("0123456789"); // need to get from user service
        orderConfirmedEvent.setEventTitle(orderRequest.getEventId().toString()); // need to get from event service
        orderConfirmedEvent.setEventLocation(orderRequest.getEventId().toString()); // need to get from event service
        orderConfirmedEvent.setEventDate(LocalDateTime.now());
        orderConfirmedEvent.setQuantity(orderRequest.getQuantity());
        orderConfirmedEvent.setAmount(orderRequest.getAmount());

        orderConfirmedKafkaProducer.send(orderConfirmedEvent);

        return new ResponseErrorTemplate(
                ApiConstant.SUCCESS.getDescription(),
                ApiConstant.SUCCESS.getKey(),
                orderMapper.toResponse(order),
                false);
    }

    private static OrderConfirmedEvent getOrderConfirmedEvent(OrderRequest orderRequest, Order order) {
        OrderConfirmedEvent orderConfirmedEvent = new OrderConfirmedEvent();
        orderConfirmedEvent.setOrderId(order.getId());
        orderConfirmedEvent.setUsername(order.getUsername());
        orderConfirmedEvent.setEmail("dudu@example.com");
        orderConfirmedEvent.setPhoneNumber("0123456789");
        orderConfirmedEvent.setEventTitle(orderRequest.getEventId().toString());
        orderConfirmedEvent.setEventLocation(orderRequest.getEventId().toString());
        orderConfirmedEvent.setQuantity(orderRequest.getQuantity());
        orderConfirmedEvent.setAmount(orderRequest.getAmount());
        return orderConfirmedEvent;
    }

    @Override
    public ResponseErrorTemplate getOrderById(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        return order.map(value -> new ResponseErrorTemplate(
                ApiConstant.SUCCESS.getDescription(),
                ApiConstant.SUCCESS.getKey(),
                orderMapper.toResponse(value),
                false
        )).orElse(null);
    }

    @Override
    public ResponseErrorTemplate cancelOrder(Long orderId, HttpServletRequest httpServletRequest) {
        // Check user authentication and authorization
        String username = handleUnauthorized(httpServletRequest);
        Optional<Order> order = orderRepository.findById(orderId);
        if (!order.isPresent()) {
            return new ResponseErrorTemplate(
                    ApiConstant.DATA_NOT_FOUND.getDescription(),
                    ApiConstant.DATA_NOT_FOUND.getKey(),
                    new EmptyObject(),
                    true
                    );
        }

        order.get().setOrderStatus(OrderStatus.CANCELLED);
        order.get().setUpdatedBy(username);
        orderRepository.save(order.get());

        return new ResponseErrorTemplate(
                ApiConstant.DATA_NOT_FOUND.getDescription(),
                ApiConstant.DATA_NOT_FOUND.getKey(),
                new EmptyObject(),
                true
        );
    }

    private String handleUnauthorized(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        String token = authHeader.substring(7);
        var user = userClient.verifyToken(token)
                .blockOptional()
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        if(user != null && "TOKEN_VALID".equalsIgnoreCase(user.getCode())) {
            return user.getData().get("username").toString();
        }
        log.warn("Invalid token provided");
        return null;
    }
}
