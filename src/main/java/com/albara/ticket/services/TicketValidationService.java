package com.albara.ticket.services;

import com.albara.ticket.domain.dtos.request.TicketValidationRequestDto;
import com.albara.ticket.domain.dtos.response.TicketValidationResponseDto;

public interface TicketValidationService {
  TicketValidationResponseDto validateTicket(TicketValidationRequestDto requestDto);
}
