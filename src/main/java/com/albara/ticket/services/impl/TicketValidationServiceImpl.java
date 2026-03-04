package com.albara.ticket.services.impl;

import com.albara.ticket.domain.entities.*;
import com.albara.ticket.domain.dtos.request.TicketValidationRequestDto;
import com.albara.ticket.domain.dtos.response.TicketValidationResponseDto;
import com.albara.ticket.mappers.TicketValidationMapper;
import com.albara.ticket.exceptions.QrCodeNotFoundException;
import com.albara.ticket.exceptions.TicketNotFoundException;
import com.albara.ticket.repositories.QrCodeRepository;
import com.albara.ticket.repositories.TicketRepository;
import com.albara.ticket.repositories.TicketValidationRepository;
import com.albara.ticket.services.TicketValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketValidationServiceImpl implements TicketValidationService {

  private final QrCodeRepository qrCodeRepository;
  private final TicketValidationRepository ticketValidationRepository;
  private final TicketRepository ticketRepository;
  private final TicketValidationMapper ticketValidationMapper;

  @Override
  public TicketValidationResponseDto validateTicket(TicketValidationRequestDto requestDto) {
    TicketValidation ticketValidation;
    if (TicketValidationMethod.MANUAL.equals(requestDto.getMethod())) {
      Ticket ticket = ticketRepository.findById(requestDto.getId())
          .orElseThrow(TicketNotFoundException::new);
      ticketValidation = validateTicket(ticket, TicketValidationMethod.MANUAL);
    } else {
      QrCode qrCode = qrCodeRepository.findByIdAndStatus(requestDto.getId(), QrCodeStatusEnum.ACTIVE)
          .orElseThrow(() -> new QrCodeNotFoundException(
              String.format("QR Code with ID %s was not found", requestDto.getId())
          ));
      Ticket ticket = qrCode.getTicket();
      ticketValidation = validateTicket(ticket, TicketValidationMethod.QR_SCAN);
    }

    return ticketValidationMapper.toTicketValidationResponseDto(ticketValidation);
  }

  private TicketValidation validateTicket(Ticket ticket,
      TicketValidationMethod ticketValidationMethod) {
    TicketValidation ticketValidation = new TicketValidation();
    ticketValidation.setTicket(ticket);
    ticketValidation.setValidationMethod(ticketValidationMethod);

    TicketValidationStatusEnum ticketValidationStatus = ticket.getValidations().stream()
        .filter(v -> TicketValidationStatusEnum.VALID.equals(v.getStatus()))
        .findFirst()
        .map(v -> TicketValidationStatusEnum.INVALID)
        .orElse(TicketValidationStatusEnum.VALID);

    ticketValidation.setStatus(ticketValidationStatus);

    return ticketValidationRepository.save(ticketValidation);
  }

  
}
