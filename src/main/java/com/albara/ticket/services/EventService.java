package com.albara.ticket.services;

import com.albara.ticket.domain.dtos.request.CreateEventRequestDto;
import com.albara.ticket.domain.dtos.request.UpdateEventRequestDto;
import com.albara.ticket.domain.dtos.response.CreateEventResponseDto;
import com.albara.ticket.domain.dtos.response.GetEventDetailsResponseDto;
import com.albara.ticket.domain.dtos.response.ListEventResponseDto;
import com.albara.ticket.domain.dtos.response.ListPublishedEventResponseDto;
import com.albara.ticket.domain.dtos.response.GetPublishedEventDetailsResponseDto;
import com.albara.ticket.domain.dtos.response.UpdateEventResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface EventService {

  CreateEventResponseDto createEvent(UUID organizerId, CreateEventRequestDto event);

  Page<ListEventResponseDto> listEventsForOrganizer(UUID organizerId, Pageable pageable);

  Optional<GetEventDetailsResponseDto> getEventForOrganizer(UUID organizerId, UUID id);

  UpdateEventResponseDto updateEventForOrganizer(UUID organizerId, UUID id, UpdateEventRequestDto event);

  void deleteEventForOrganizer(UUID organizerId, UUID id);

  Page<ListPublishedEventResponseDto> listPublishedEvents(Pageable pageable);

  Page<ListPublishedEventResponseDto> searchPublishedEvents(String query, Pageable pageable);

  Optional<GetPublishedEventDetailsResponseDto> getPublishedEvent(UUID id);
}
