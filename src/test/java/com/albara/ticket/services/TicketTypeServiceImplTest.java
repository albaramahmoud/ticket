package com.albara.ticket.services;

import com.albara.ticket.domain.entities.Ticket;
import com.albara.ticket.domain.entities.TicketStatusEnum;
import com.albara.ticket.domain.entities.TicketType;
import com.albara.ticket.domain.entities.User;
import com.albara.ticket.exceptions.TicketTypeNotFoundException;
import com.albara.ticket.exceptions.TicketsSoldOutException;
import com.albara.ticket.exceptions.UserNotFoundException;
import com.albara.ticket.repositories.TicketRepository;
import com.albara.ticket.repositories.TicketTypeRepository;
import com.albara.ticket.repositories.UserRepository;
import com.albara.ticket.services.impl.TicketTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketTypeServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TicketTypeRepository ticketTypeRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private QrCodeService qrCodeService;

    @InjectMocks
    private TicketTypeServiceImpl ticketTypeService;

    private UUID userId;
    private UUID ticketTypeId;
    private User mockUser;
    private TicketType mockTicketType;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        ticketTypeId = UUID.randomUUID();

        mockUser = new User();
        mockUser.setId(userId);

        mockTicketType = new TicketType();
        mockTicketType.setId(ticketTypeId);
        mockTicketType.setTotalAvailable(100);
    }

    @Test
    void purchaseTicket_Success() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(ticketTypeRepository.findByIdWithLock(ticketTypeId)).thenReturn(Optional.of(mockTicketType));
        when(ticketRepository.countByTicketTypeId(ticketTypeId)).thenReturn(Math.toIntExact(50L));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Ticket result = ticketTypeService.purchaseTicket(userId, ticketTypeId);

        // Assert
        assertNotNull(result);
        assertEquals(TicketStatusEnum.PURCHASED, result.getStatus());
        assertEquals(mockUser, result.getPurchaser());
        assertEquals(mockTicketType, result.getTicketType());

        verify(ticketRepository, times(2)).save(any(Ticket.class));
        verify(qrCodeService).generateQrCode(any(Ticket.class));
    }

    @Test
    void purchaseTicket_ThrowsTicketsSoldOutException_WhenAtCapacity() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(ticketTypeRepository.findByIdWithLock(ticketTypeId)).thenReturn(Optional.of(mockTicketType));
        when(ticketRepository.countByTicketTypeId(ticketTypeId)).thenReturn(Math.toIntExact(100L));

        // Act & Assert
        assertThrows(TicketsSoldOutException.class, () -> 
            ticketTypeService.purchaseTicket(userId, ticketTypeId)
        );

        verify(ticketRepository, never()).save(any(Ticket.class));
        verify(qrCodeService, never()).generateQrCode(any(Ticket.class));
    }

    @Test
    void purchaseTicket_ThrowsUserNotFoundException_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> 
            ticketTypeService.purchaseTicket(userId, ticketTypeId)
        );


        verify(ticketRepository, never()).save(any(Ticket.class));
        verify(qrCodeService, never()).generateQrCode(any(Ticket.class));
    }

    @Test
    void purchaseTicket_ThrowsTicketTypeNotFoundException_WhenTicketTypeDoesNotExist() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(ticketTypeRepository.findByIdWithLock(ticketTypeId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TicketTypeNotFoundException.class, () -> 
            ticketTypeService.purchaseTicket(userId, ticketTypeId)
        );
    }
}
