package com.albara.ticket.services;

import com.albara.ticket.domain.entities.QrCode;
import com.albara.ticket.exceptions.QrCodeNotFoundException;
import com.albara.ticket.repositories.QrCodeRepository;
import com.albara.ticket.services.impl.QrCodeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QrCodeServiceImplTest {

    @Mock
    private QrCodeRepository qrCodeRepository;

    @InjectMocks
    private QrCodeServiceImpl qrCodeService;

    private UUID userId;
    private UUID ticketId;
    private QrCode mockQrCode;

    // A valid Base64 string for a tiny 1x1 pixel PNG image
    private final String validBase64Image = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDwAEhQGAhKmMIQAAAABJRU5ErkJggg==";

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        ticketId = UUID.randomUUID();

        mockQrCode = new QrCode();
        mockQrCode.setId(UUID.randomUUID());
        mockQrCode.setValue(validBase64Image);
    }

    @Test
    void getQrCodeImageForUserAndTicket_Success() {
        when(qrCodeRepository.findByTicketIdAndTicketPurchaserId(ticketId, userId)).thenReturn(Optional.of(mockQrCode));

        byte[] result = qrCodeService.getQrCodeImageForUserAndTicket(userId, ticketId);

        assertNotNull(result);
        assertTrue(result.length > 0);
        verify(qrCodeRepository, times(1)).findByTicketIdAndTicketPurchaserId(ticketId, userId);
    }

    @Test
    void getQrCodeImageForUserAndTicket_ThrowsQrCodeNotFoundException_WhenMissingInDatabase() {
        when(qrCodeRepository.findByTicketIdAndTicketPurchaserId(ticketId, userId)).thenReturn(Optional.empty());

        assertThrows(QrCodeNotFoundException.class, () -> 
            qrCodeService.getQrCodeImageForUserAndTicket(userId, ticketId)
        );
    }

    @Test
    void getQrCodeImageForUserAndTicket_ThrowsQrCodeNotFoundException_WhenBase64IsMalformed() {
        mockQrCode.setValue("not-valid-base-64-!!@#");
        when(qrCodeRepository.findByTicketIdAndTicketPurchaserId(ticketId, userId)).thenReturn(Optional.of(mockQrCode));

        assertThrows(QrCodeNotFoundException.class, () -> 
            qrCodeService.getQrCodeImageForUserAndTicket(userId, ticketId)
        );
    }
}
