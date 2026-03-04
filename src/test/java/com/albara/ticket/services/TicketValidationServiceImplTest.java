package com.albara.ticket.services;

import com.albara.ticket.domain.dtos.request.TicketValidationRequestDto;
import com.albara.ticket.domain.dtos.response.TicketValidationResponseDto;
import com.albara.ticket.domain.entities.*;
import com.albara.ticket.exceptions.TicketNotFoundException;
import com.albara.ticket.mappers.TicketValidationMapper;
import com.albara.ticket.repositories.QrCodeRepository;
import com.albara.ticket.repositories.TicketRepository;
import com.albara.ticket.repositories.TicketValidationRepository;
import com.albara.ticket.services.impl.TicketValidationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketValidationServiceImplTest {

    @Mock
    private QrCodeRepository qrCodeRepository;

    @Mock
    private TicketValidationRepository ticketValidationRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TicketValidationMapper ticketValidationMapper;

    @InjectMocks
    private TicketValidationServiceImpl ticketValidationService;

    private UUID ticketId;
    private TicketValidationRequestDto requestDto;
    private Ticket mockTicket;

    @BeforeEach
    void setUp() {
        ticketId = UUID.randomUUID();

        requestDto = new TicketValidationRequestDto();
        requestDto.setId(ticketId);
        requestDto.setMethod(TicketValidationMethod.MANUAL);

        mockTicket = new Ticket();
        mockTicket.setId(ticketId);
        mockTicket.setValidations(new ArrayList<>());
    }

    @Test
    void validateTicket_AlreadyValidated_ReturnsInvalidResponse() {
        // Arrange
        TicketValidation existingValidation = new TicketValidation();
        existingValidation.setStatus(TicketValidationStatusEnum.VALID);
        mockTicket.setValidations(List.of(existingValidation));

        TicketValidationResponseDto responseDto = new TicketValidationResponseDto();
        responseDto.setStatus(TicketValidationStatusEnum.INVALID);

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(mockTicket));
        when(ticketValidationRepository.save(any(TicketValidation.class))).thenAnswer(i -> i.getArguments()[0]);
        when(ticketValidationMapper.toTicketValidationResponseDto(any(TicketValidation.class))).thenReturn(responseDto);

        // Act
        TicketValidationResponseDto response = ticketValidationService.validateTicket(requestDto);

        // Assert
        assertEquals(TicketValidationStatusEnum.INVALID, response.getStatus());
        verify(ticketValidationRepository, times(1)).save(any(TicketValidation.class));
    }

    @Test
    void validateTicket_Success_ReturnsValidResponse() {
        // Arrange
        TicketValidationResponseDto responseDto = new TicketValidationResponseDto();
        responseDto.setStatus(TicketValidationStatusEnum.VALID);
        responseDto.setTicketId(ticketId);

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(mockTicket));
        when(ticketValidationRepository.save(any(TicketValidation.class))).thenAnswer(i -> i.getArguments()[0]);
        when(ticketValidationMapper.toTicketValidationResponseDto(any(TicketValidation.class))).thenReturn(responseDto);

        // Act
        TicketValidationResponseDto response = ticketValidationService.validateTicket(requestDto);

        // Assert
        assertEquals(TicketValidationStatusEnum.VALID, response.getStatus());
        assertEquals(ticketId, response.getTicketId());
        verify(ticketValidationRepository, times(1)).save(any(TicketValidation.class));
    }

    @Test
    void validateTicket_ThrowsTicketNotFoundException_WhenTicketDoesNotExist() {
        // Arrange
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TicketNotFoundException.class, () -> {
            ticketValidationService.validateTicket(requestDto);
        });
    }
}
