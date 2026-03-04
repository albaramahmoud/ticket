package com.albara.ticket.controllers;

import com.albara.ticket.domain.dtos.request.TicketValidationRequestDto;
import com.albara.ticket.domain.dtos.response.TicketValidationResponseDto;
import com.albara.ticket.services.TicketValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/ticket-validations")
@RequiredArgsConstructor
public class TicketValidationController {

  private final TicketValidationService ticketValidationService;

  @PostMapping
  public ResponseEntity<TicketValidationResponseDto> validateTicket(
      @RequestBody TicketValidationRequestDto ticketValidationRequestDto
  ){
    TicketValidationResponseDto response = ticketValidationService.validateTicket(ticketValidationRequestDto);
    return ResponseEntity.ok(response);
  }

}
