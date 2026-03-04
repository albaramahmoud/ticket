package com.albara.ticket.services.impl;

import com.albara.ticket.domain.dtos.request.CreateEventRequestDto;
import com.albara.ticket.domain.dtos.request.UpdateEventRequestDto;
import com.albara.ticket.domain.dtos.request.UpdateTicketTypeRequestDto;
import com.albara.ticket.domain.entities.Event;
import com.albara.ticket.domain.entities.EventStatusEnum;
import com.albara.ticket.domain.entities.TicketType;
import com.albara.ticket.domain.entities.User;
import com.albara.ticket.domain.dtos.response.CreateEventResponseDto;
import com.albara.ticket.domain.dtos.response.GetEventDetailsResponseDto;
import com.albara.ticket.domain.dtos.response.ListEventResponseDto;
import com.albara.ticket.domain.dtos.response.ListPublishedEventResponseDto;
import com.albara.ticket.domain.dtos.response.GetPublishedEventDetailsResponseDto;
import com.albara.ticket.domain.dtos.response.UpdateEventResponseDto;
import com.albara.ticket.mappers.EventMapper;
import com.albara.ticket.exceptions.EventNotFoundException;
import com.albara.ticket.exceptions.EventUpdateException;
import com.albara.ticket.exceptions.TicketTypeNotFoundException;
import com.albara.ticket.exceptions.UserNotFoundException;
import com.albara.ticket.repositories.EventRepository;
import com.albara.ticket.repositories.UserRepository;
import com.albara.ticket.services.EventService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

  private final UserRepository userRepository;
  private final EventRepository eventRepository;
  private final EventMapper eventMapper;

  @Override
  @Transactional
  public CreateEventResponseDto createEvent(UUID organizerId, CreateEventRequestDto event) {
    User organizer = userRepository.findById(organizerId)
        .orElseThrow(() -> new UserNotFoundException(
            String.format("User with ID '%s' not found", organizerId))
        );

    Event eventToCreate = new Event();

    List<TicketType> ticketTypesToCreate = event.getTicketTypes().stream().map(
        ticketType -> {
          TicketType ticketTypeToCreate = new TicketType();
          ticketTypeToCreate.setName(ticketType.getName());
          ticketTypeToCreate.setPrice(ticketType.getPrice());
          ticketTypeToCreate.setDescription(ticketType.getDescription());
          ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
          ticketTypeToCreate.setEvent(eventToCreate);
          return ticketTypeToCreate;
        }).toList();

    eventToCreate.setName(event.getName());
    eventToCreate.setStart(event.getStart());
    eventToCreate.setEnd(event.getEnd());
    eventToCreate.setVenue(event.getVenue());
    eventToCreate.setSalesStart(event.getSalesStart());
    eventToCreate.setSalesEnd(event.getSalesEnd());
    eventToCreate.setStatus(event.getStatus());
    eventToCreate.setOrganizer(organizer);
    eventToCreate.setTicketTypes(ticketTypesToCreate);

    Event saved = eventRepository.save(eventToCreate);
    return eventMapper.toDto(saved);
  }

  @Override
  public Page<ListEventResponseDto> listEventsForOrganizer(UUID organizerId, Pageable pageable) {
    return eventRepository.findByOrganizerId(organizerId, pageable)
        .map(eventMapper::toListEventResponseDto);
  }

  @Override
  public Optional<GetEventDetailsResponseDto> getEventForOrganizer(UUID organizerId, UUID id) {
    return eventRepository.findByIdAndOrganizerId(id, organizerId)
        .map(eventMapper::toGetEventDetailsResponseDto);
  }

  @Override
  @Transactional
  public UpdateEventResponseDto updateEventForOrganizer(UUID organizerId, UUID id, UpdateEventRequestDto event) {
    if (null == event.getId()) {
      throw new EventUpdateException("Event ID cannot be null");
    }

    if (!id.equals(event.getId())) {
      throw new EventUpdateException("Cannot update the ID of an event");
    }

    Event existingEvent = eventRepository
        .findByIdAndOrganizerId(id, organizerId)
        .orElseThrow(() -> new EventNotFoundException(
            String.format("Event with ID '%s' does not exist", id))
        );

    existingEvent.setName(event.getName());
    existingEvent.setStart(event.getStart());
    existingEvent.setEnd(event.getEnd());
    existingEvent.setVenue(event.getVenue());
    existingEvent.setSalesStart(event.getSalesStart());
    existingEvent.setSalesEnd(event.getSalesEnd());
    existingEvent.setStatus(event.getStatus());

    Set<UUID> requestTicketTypeIds = event.getTicketTypes()
        .stream()
        .map(UpdateTicketTypeRequestDto::getId)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());

    existingEvent.getTicketTypes().removeIf(existingTicketType ->
        !requestTicketTypeIds.contains(existingTicketType.getId())
    );

    Map<UUID, TicketType> existingTicketTypesIndex = existingEvent.getTicketTypes().stream()
        .collect(Collectors.toMap(TicketType::getId, Function.identity()));

    for (UpdateTicketTypeRequestDto ticketType : event.getTicketTypes()) {
      if (null == ticketType.getId()) {
        // Create
        TicketType ticketTypeToCreate = new TicketType();
        ticketTypeToCreate.setName(ticketType.getName());
        ticketTypeToCreate.setPrice(ticketType.getPrice());
        ticketTypeToCreate.setDescription(ticketType.getDescription());
        ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
        ticketTypeToCreate.setEvent(existingEvent);
        existingEvent.getTicketTypes().add(ticketTypeToCreate);

      } else if (existingTicketTypesIndex.containsKey(ticketType.getId())) {
        // Update
        TicketType existingTicketType = existingTicketTypesIndex.get(ticketType.getId());
        existingTicketType.setName(ticketType.getName());
        existingTicketType.setPrice(ticketType.getPrice());
        existingTicketType.setDescription(ticketType.getDescription());
        existingTicketType.setTotalAvailable(ticketType.getTotalAvailable());
      } else {
        throw new TicketTypeNotFoundException(String.format(
            "Ticket type with ID '%s' does not exist", ticketType.getId()
        ));
      }
    }

    Event saved = eventRepository.save(existingEvent);
    return eventMapper.toUpdateEventResponseDto(saved);
  }

  @Override
  @Transactional
  public void deleteEventForOrganizer(UUID organizerId, UUID id) {

//    eventRepository.deleteById(id);
    getEventForOrganizer(organizerId, id).ifPresent(eventDto -> eventRepository.deleteById(eventDto.getId()));
  }

  @Override
  public Page<ListPublishedEventResponseDto> listPublishedEvents(Pageable pageable) {
    return eventRepository.findByStatus(EventStatusEnum.PUBLISHED, pageable)
        .map(eventMapper::toListPublishedEventResponseDto);
  }

  @Override
  public Page<ListPublishedEventResponseDto> searchPublishedEvents(String query, Pageable pageable) {
    return eventRepository.searchEvents(query, pageable)
        .map(eventMapper::toListPublishedEventResponseDto);
  }

  @Override
  public Optional<GetPublishedEventDetailsResponseDto> getPublishedEvent(UUID id) {
    return eventRepository.findByIdAndStatus(id, EventStatusEnum.PUBLISHED)
        .map(eventMapper::toGetPublishedEventDetailsResponseDto);
  }


}
