package com.nangseakheng.ticket.dto;

import com.nangseakheng.ticket.entity.TicketType;
import com.nangseakheng.ticket.enumz.TicketStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TicketResponse {

    private Long id;

    private Long eventId;

    private String seatNumber;

    private BigDecimal price;

    private TicketType ticketType;

    private TicketStatus ticketStatus;

    private LocalDateTime lockedUntil;

    private String lockedBy;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime updatedAt;

    private String updatedBy;
}
