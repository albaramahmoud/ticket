package com.albara.ticket.controllers;

import com.albara.ticket.domain.dtos.request.CreateEventRequestDto;
import com.albara.ticket.domain.dtos.request.CreateTicketTypeRequestDto;
import com.albara.ticket.domain.dtos.request.UpdateEventRequestDto;
import com.albara.ticket.domain.dtos.request.UpdateTicketTypeRequestDto;
import com.albara.ticket.domain.dtos.response.CreateEventResponseDto;
import com.albara.ticket.domain.dtos.response.GetEventDetailsResponseDto;
import com.albara.ticket.domain.dtos.response.ListEventResponseDto;
import com.albara.ticket.domain.dtos.response.UpdateEventResponseDto;
import com.albara.ticket.domain.entities.EventStatusEnum;
import com.albara.ticket.repositories.UserRepository;
import com.albara.ticket.services.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
@AutoConfigureMockMvc
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventService eventService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void shouldReturn201CreatedWhenValidRequest() throws Exception {
        // Arrange
        UUID mockUserId = UUID.randomUUID();
        CreateTicketTypeRequestDto ticketType = new CreateTicketTypeRequestDto("VIP", 100.0, "VIP Access", 50);
        CreateEventRequestDto request = new CreateEventRequestDto(
                "My Concert",
                LocalDateTime.now().plusDays(5), // Future start date
                LocalDateTime.now().plusDays(5).plusHours(3), // Future end date
                "Stadium",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(4),
                EventStatusEnum.DRAFT,
                List.of(ticketType)
        );

        CreateEventResponseDto response = new CreateEventResponseDto();
        response.setId(UUID.randomUUID());

        // Mock the service
        doReturn(response).when(eventService).createEvent(eq(mockUserId), any(CreateEventRequestDto.class));

        // Act & Assert
        mockMvc.perform(post("/api/v1/events")
                        .with(jwt().jwt(jwt -> jwt.subject(mockUserId.toString())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn400BadRequestWhenNameIsBlank() throws Exception {
        // Arrange
        UUID mockUserId = UUID.randomUUID();
        CreateTicketTypeRequestDto ticketType = new CreateTicketTypeRequestDto("VIP", 100.0, "VIP Access", 50);
        CreateEventRequestDto request = new CreateEventRequestDto(
                "", // Blank name to trigger validation failure
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(5).plusHours(3),
                "Stadium",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(4),
                EventStatusEnum.DRAFT,
                List.of(ticketType)
        );

        // Act & Assert
        mockMvc.perform(post("/api/v1/events")
                        .with(jwt().jwt(jwt -> jwt.subject(mockUserId.toString())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn200OkWithPaginatedEvents() throws Exception {
        // Arrange
        UUID mockUserId = UUID.randomUUID();

        // We simulate what the service will return
        ListEventResponseDto responseDto = new ListEventResponseDto();
        Page<ListEventResponseDto> mockPage = new PageImpl<>(List.of(responseDto));

        doReturn(mockPage).when(eventService).listEventsForOrganizer(eq(mockUserId), any());


        // Act & Assert
        mockMvc.perform(get("/api/v1/events?page=0&size=10")
                .with(jwt().jwt(jwt -> jwt.subject(mockUserId.toString())))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn200OkWithEventDetails() throws Exception {
        // Arrange
        UUID mockUserId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();

        GetEventDetailsResponseDto responseDto = new GetEventDetailsResponseDto();
        responseDto.setId(eventId);
        responseDto.setName("My Awesome Event");

        doReturn(Optional.of(responseDto)).when(eventService).getEventForOrganizer(mockUserId, eventId);

        // Act & Assert
        mockMvc.perform(get("/api/v1/events/{eventId}", eventId)
                        .with(jwt().jwt(jwt -> jwt.subject(mockUserId.toString()))))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.name").value("My Awesome Event"));
    }

    @Test
    void shouldReturn200OkWhenUpdatingEvent() throws Exception {
        // Arrange
        UUID mockUserId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();

        UpdateTicketTypeRequestDto ticketType = new UpdateTicketTypeRequestDto(UUID.randomUUID(), "VIP", 100.0, "VIP Access", 50);
        UpdateEventRequestDto request = new UpdateEventRequestDto();
        request.setId(eventId);
        request.setName("Updated Concert");
        request.setVenue("New Stadium");
        request.setStatus(EventStatusEnum.PUBLISHED);
        request.setTicketTypes(List.of(ticketType));

        UpdateEventResponseDto responseDto = new UpdateEventResponseDto();
        responseDto.setId(eventId);
        responseDto.setName("Updated Concert");

        doReturn(responseDto).when(eventService).updateEventForOrganizer(eq(mockUserId), eq(eventId), any(UpdateEventRequestDto.class));

        // Act & Assert
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/v1/events/{eventId}", eventId)
                        .with(jwt().jwt(jwt -> jwt.subject(mockUserId.toString())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
    @Test
    void shouldReturn204NoContentWhenDeletingEvent() throws Exception {
        // Arrange
        UUID mockUserId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();

        // Act & Assert
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/api/v1/events/{eventId}", eventId)
                        .with(jwt().jwt(jwt -> jwt.subject(mockUserId.toString()))))
                .andExpect(status().isNoContent());

        verify(eventService).deleteEventForOrganizer(mockUserId, eventId);
    }
}
