package com.albara.ticket.services.impl;

import com.albara.ticket.domain.dtos.response.GetTicketResponseDto;
import com.albara.ticket.domain.dtos.response.ListTicketResponseDto;
import com.albara.ticket.domain.entities.Ticket;
import com.albara.ticket.mappers.TicketMapper;
import com.albara.ticket.repositories.TicketRepository;
import com.albara.ticket.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

  private final TicketRepository ticketRepository;
  private final TicketMapper ticketMapper;

  @Override
  @Transactional(readOnly = true) // <-- Add this annotation
  public Page<ListTicketResponseDto> listTicketsForUser(UUID userId, Pageable pageable) {
    return ticketRepository.findByPurchaserId(userId, pageable)
        .map(ticketMapper::toListTicketResponseDto);
  }

  @Override
  @Transactional(readOnly = true) // <-- Add this annotation
  public Optional<GetTicketResponseDto> getTicketForUser(UUID userId, UUID ticketId) {
    return ticketRepository.findByIdAndPurchaserId(ticketId, userId)
        .map(ticketMapper::toGetTicketResponseDto);
  }
}
