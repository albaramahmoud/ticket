package com.albara.ticket.controllers;

import com.albara.ticket.domain.dtos.response.GetTicketResponseDto;
import com.albara.ticket.domain.dtos.response.ListTicketResponseDto;
import com.albara.ticket.repositories.UserRepository;
import com.albara.ticket.services.QrCodeService;
import com.albara.ticket.services.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
@AutoConfigureMockMvc
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private org.springframework.security.oauth2.jwt.JwtDecoder jwtDecoder;

    @MockBean
    private com.albara.ticket.config.JwtAuthenticationConverter jwtAuthenticationConverter;

    @MockBean
    private com.albara.ticket.filters.UserProvisioningFilter userProvisioningFilter;
    @MockBean
    private TicketService ticketService;

    @MockBean
    private QrCodeService qrCodeService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void shouldReturn200OkWithTickets() throws Exception {
        // Arrange
        UUID mockUserId = UUID.randomUUID();
        ListTicketResponseDto responseDto = new ListTicketResponseDto();
        Page<ListTicketResponseDto> mockPage = new PageImpl<>(List.of(responseDto));

        when(ticketService.listTicketsForUser(eq(mockUserId), any(Pageable.class))).thenReturn(mockPage);

        // Act & Assert
        mockMvc.perform(get("/api/v1/tickets?page=0&size=10")
                        .with(jwt().jwt(jwt -> jwt.subject(mockUserId.toString())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
