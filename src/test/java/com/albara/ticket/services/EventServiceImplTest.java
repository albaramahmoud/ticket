package com.albara.ticket.services;

import com.albara.ticket.domain.dtos.request.CreateEventRequestDto;
import com.albara.ticket.domain.dtos.request.UpdateEventRequestDto;
import com.albara.ticket.domain.dtos.request.UpdateTicketTypeRequestDto;
import com.albara.ticket.domain.dtos.response.CreateEventResponseDto;
import com.albara.ticket.domain.dtos.response.GetEventDetailsResponseDto;
import com.albara.ticket.domain.dtos.response.ListEventResponseDto;
import com.albara.ticket.domain.dtos.response.ListPublishedEventResponseDto;
import com.albara.ticket.domain.dtos.response.GetPublishedEventDetailsResponseDto;
import com.albara.ticket.domain.dtos.response.UpdateEventResponseDto;
import com.albara.ticket.domain.entities.Event;
import com.albara.ticket.domain.entities.EventStatusEnum;
import com.albara.ticket.domain.entities.User;
import com.albara.ticket.mappers.EventMapper;
import com.albara.ticket.repositories.EventRepository;
import com.albara.ticket.repositories.UserRepository;
import com.albara.ticket.services.impl.EventServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    void shouldCreateEventSuccessfully() {
        // Arrange
        UUID organizerId = UUID.randomUUID();
        CreateEventRequestDto request = new CreateEventRequestDto();
        request.setTicketTypes(new ArrayList<>());
        
        User mockUser = new User();
        Event savedEvent = new Event();
        CreateEventResponseDto expectedResponse = new CreateEventResponseDto();

        when(userRepository.findById(organizerId)).thenReturn(Optional.of(mockUser));
        when(eventRepository.save(any(Event.class))).thenReturn(savedEvent);
        when(eventMapper.toDto(savedEvent)).thenReturn(expectedResponse);

        // Act
        CreateEventResponseDto actualResponse = eventService.createEvent(organizerId, request);

        // Assert
        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void shouldReturnPaginatedEventsForOrganizer() {
        // Arrange
        UUID organizerId = UUID.randomUUID();
        Pageable pageable = Pageable.unpaged();

        Event event = new Event();
        Page<Event> eventPage = new PageImpl<>(List.of(event));

        when(eventRepository.findByOrganizerId(eq(organizerId), any(Pageable.class))).thenReturn(eventPage);

        ListEventResponseDto responseDto = new ListEventResponseDto();
        when(eventMapper.toListEventResponseDto(event)).thenReturn(responseDto);

        // Act
        Page<ListEventResponseDto> responsePage = eventService.listEventsForOrganizer(organizerId, pageable);

        // Assert
        assertThat(responsePage).isNotNull();
        assertThat(responsePage.getTotalElements()).isEqualTo(1);
        assertThat(responsePage.getContent().get(0)).isEqualTo(responseDto);
    }

    @Test
    void shouldReturnEventDetailsForOrganizer() {
        // Arrange
        UUID organizerId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        Event event = new Event();
        GetEventDetailsResponseDto responseDto = new GetEventDetailsResponseDto();

        when(eventRepository.findByIdAndOrganizerId(eventId, organizerId)).thenReturn(Optional.of(event));
        when(eventMapper.toGetEventDetailsResponseDto(event)).thenReturn(responseDto);

        // Act
        Optional<GetEventDetailsResponseDto> result = eventService.getEventForOrganizer(organizerId, eventId);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(responseDto);
    }

    @Test
    void shouldUpdateEventSuccessfully() {
        // Arrange
        UUID organizerId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();

        Event existingEvent = new Event();
        existingEvent.setId(eventId);
        existingEvent.setTicketTypes(new ArrayList<>());

        UpdateEventRequestDto updateRequest = new UpdateEventRequestDto();
        updateRequest.setId(eventId);
        updateRequest.setName("New Name");
        updateRequest.setTicketTypes(new ArrayList<>());

        UpdateEventResponseDto expectedResponse = new UpdateEventResponseDto();
        expectedResponse.setName("New Name");

        when(eventRepository.findByIdAndOrganizerId(eventId, organizerId)).thenReturn(Optional.of(existingEvent));
        when(eventRepository.save(existingEvent)).thenReturn(existingEvent);
        when(eventMapper.toUpdateEventResponseDto(existingEvent)).thenReturn(expectedResponse);

        // Act
        UpdateEventResponseDto actualResponse = eventService.updateEventForOrganizer(organizerId, eventId, updateRequest);

        // Assert
        assertThat(actualResponse.getName()).isEqualTo("New Name");
        verify(eventRepository).save(existingEvent);
    }

    @Test
    void shouldDeleteEventSuccessfully() {
        // Arrange
        UUID organizerId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        Event event = new Event();
        event.setId(eventId);

        when(eventRepository.findByIdAndOrganizerId(eventId, organizerId)).thenReturn(Optional.of(event));

        // Act
        eventService.deleteEventForOrganizer(organizerId, eventId);

        // Assert
        verify(eventRepository).deleteById(eventId);
    }

    @Test
    void shouldListPublishedEvents() {
        // Arrange
        Pageable pageable = Pageable.unpaged();

        Event event = new Event();
        event.setStatus(EventStatusEnum.PUBLISHED);
        Page<Event> eventPage = new PageImpl<>(List.of(event));

        when(eventRepository.findByStatus(eq(EventStatusEnum.PUBLISHED), any(Pageable.class))).thenReturn(eventPage);

        ListPublishedEventResponseDto responseDto = new ListPublishedEventResponseDto();
        when(eventMapper.toListPublishedEventResponseDto(event)).thenReturn(responseDto);

        // Act
        Page<ListPublishedEventResponseDto> result = eventService.listPublishedEvents(pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0)).isEqualTo(responseDto);
    }

    @Test
    void shouldReturnPublishedEventById() {
        // Arrange
        UUID eventId = UUID.randomUUID();

        Event event = new Event();
        event.setId(eventId);
        event.setStatus(EventStatusEnum.PUBLISHED);

        GetPublishedEventDetailsResponseDto responseDto = new GetPublishedEventDetailsResponseDto();
        responseDto.setId(eventId);
        responseDto.setName("Public Festival");

        when(eventRepository.findByIdAndStatus(eventId, EventStatusEnum.PUBLISHED))
                .thenReturn(Optional.of(event));

        when(eventMapper.toGetPublishedEventDetailsResponseDto(event)).thenReturn(responseDto);

        // Act
        Optional<GetPublishedEventDetailsResponseDto> actualResponse = eventService.getPublishedEvent(eventId);

        // Assert
        assertThat(actualResponse).isPresent();
        assertThat(actualResponse.get().getName()).isEqualTo("Public Festival");
    }

    @Test
    void shouldReturnEmptyWhenPublishedEventNotFoundOrIsDraft() {
        // Arrange
        UUID eventId = UUID.randomUUID();

        when(eventRepository.findByIdAndStatus(eventId, EventStatusEnum.PUBLISHED))
                .thenReturn(Optional.empty());

        // Act
        Optional<GetPublishedEventDetailsResponseDto> actualResponse = eventService.getPublishedEvent(eventId);

        // Assert
        assertThat(actualResponse).isEmpty();
    }
}
