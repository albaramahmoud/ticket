package com.albara.ticket.controllers;

import com.albara.ticket.domain.dtos.response.GetPublishedEventDetailsResponseDto;
import com.albara.ticket.domain.dtos.response.ListPublishedEventResponseDto;
import com.albara.ticket.repositories.UserRepository;
import com.albara.ticket.services.EventService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PublishedEventController.class)
@AutoConfigureMockMvc(addFilters = false) // Bypasses security filters for this unit test
class PublishedEventControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private org.springframework.security.oauth2.jwt.JwtDecoder jwtDecoder;

    @MockBean
    private com.albara.ticket.config.JwtAuthenticationConverter jwtAuthenticationConverter;

    @MockBean
    private com.albara.ticket.filters.UserProvisioningFilter userProvisioningFilter;
    @MockBean
    private EventService eventService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void shouldReturn200OkWithPublishedEvents() throws Exception {
        // Arrange
        ListPublishedEventResponseDto responseDto = new ListPublishedEventResponseDto();
        Page<ListPublishedEventResponseDto> mockPage = new PageImpl<>(List.of(responseDto));

        when(eventService.listPublishedEvents(any(Pageable.class))).thenReturn(mockPage);

        // Act & Assert
        mockMvc.perform(get("/api/v1/published-events?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
