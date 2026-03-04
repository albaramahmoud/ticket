package com.albara.ticket.services;

import com.albara.ticket.domain.dtos.response.GetTicketResponseDto;
import com.albara.ticket.domain.dtos.response.ListTicketResponseDto;
import com.albara.ticket.domain.entities.Ticket;
import com.albara.ticket.mappers.TicketMapper;
import com.albara.ticket.repositories.TicketRepository;
import com.albara.ticket.services.impl.TicketServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TicketMapper ticketMapper;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private UUID purchaserId;
    private UUID ticketId;
    private Pageable pageable;
    private Ticket mockTicket;
    private GetTicketResponseDto mockDto;
    
    @BeforeEach
    void setUp() {
        purchaserId = UUID.randomUUID();
        pageable = PageRequest.of(0, 10);
        ticketId = UUID.randomUUID();

        mockTicket = new Ticket();
        mockTicket.setId(ticketId);
        
        mockDto = new GetTicketResponseDto();
        mockDto.setId(ticketId);
    }

    @Test
    void listTicketsForUser_ReturnsPagedTickets() {
        // Arrange
        Page<Ticket> tickets = new PageImpl<>(List.of(mockTicket));
        ListTicketResponseDto responseDto = new ListTicketResponseDto();
        responseDto.setId(mockTicket.getId());

        when(ticketRepository.findByPurchaserId(eq(purchaserId), any(Pageable.class))).thenReturn(tickets);
        when(ticketMapper.toListTicketResponseDto(mockTicket)).thenReturn(responseDto);

        // Act
        Page<ListTicketResponseDto> result = ticketService.listTicketsForUser(purchaserId, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(mockTicket.getId(), result.getContent().get(0).getId());
        verify(ticketRepository, times(1)).findByPurchaserId(eq(purchaserId), any(Pageable.class));
        verify(ticketMapper, times(1)).toListTicketResponseDto(mockTicket);
    }

    @Test
    void getTicket_Success() {
        // Arrange
        when(ticketRepository.findByIdAndPurchaserId(ticketId, purchaserId)).thenReturn(Optional.of(mockTicket));
        when(ticketMapper.toGetTicketResponseDto(mockTicket)).thenReturn(mockDto);

        // Act
        Optional<GetTicketResponseDto> result = ticketService.getTicketForUser(purchaserId, ticketId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(mockDto, result.get());
        verify(ticketRepository, times(1)).findByIdAndPurchaserId(ticketId, purchaserId);
        verify(ticketMapper, times(1)).toGetTicketResponseDto(mockTicket);
    }

    @Test
    void getTicket_ReturnsEmptyWhenNotFound() {
        // Arrange
        when(ticketRepository.findByIdAndPurchaserId(ticketId, purchaserId)).thenReturn(Optional.empty());

        // Act
        Optional<GetTicketResponseDto> result = ticketService.getTicketForUser(purchaserId, ticketId);

        // Assert
        assertTrue(result.isEmpty());
        verify(ticketRepository, times(1)).findByIdAndPurchaserId(ticketId, purchaserId);
        verifyNoInteractions(ticketMapper);
    }

}
