package com.nangseakheng.ticket.service;

import com.nangseakheng.common.exception.ResponseErrorTemplate;
import com.nangseakheng.ticket.dto.TicketLockRequest;
import com.nangseakheng.ticket.dto.TicketRequest;

public interface TicketService {

    ResponseErrorTemplate createTicket(TicketRequest ticketRequest);

    ResponseErrorTemplate getTicketById(Long ticketId);

    ResponseErrorTemplate lockTicket(TicketLockRequest ticketLockRequest);

    void unlockTicket(Long eventId, Integer quantity);

}
