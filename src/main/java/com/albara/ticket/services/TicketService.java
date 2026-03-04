package com.albara.ticket.services;

import com.albara.ticket.domain.dtos.response.GetTicketResponseDto;
import com.albara.ticket.domain.dtos.response.ListTicketResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface TicketService {
  Page<ListTicketResponseDto> listTicketsForUser(UUID userId, Pageable pageable);
  Optional<GetTicketResponseDto> getTicketForUser(UUID userId, UUID ticketId);
}
