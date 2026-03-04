package com.albara.ticket.controllers;

import com.albara.ticket.repositories.UserRepository;
import com.albara.ticket.services.TicketTypeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketTypeController.class)
@AutoConfigureMockMvc
class TicketTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketTypeService ticketTypeService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void shouldReturn204NoContentWhenPurchasingTicket() throws Exception {
        // Arrange
        UUID mockUserId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        UUID ticketTypeId = UUID.randomUUID();

        // Act & Assert
        mockMvc.perform(post("/api/v1/events/{eventId}/ticket-types/{ticketTypeId}/tickets", eventId, ticketTypeId)
                        .with(jwt().jwt(jwt -> jwt.subject(mockUserId.toString())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(ticketTypeService).purchaseTicket(mockUserId, ticketTypeId);
    }
}
