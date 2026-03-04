package com.albara.ticket.controllers;

import com.albara.ticket.domain.dtos.request.CreateEventRequestDto;
import com.albara.ticket.domain.dtos.request.UpdateEventRequestDto;
import com.albara.ticket.domain.dtos.response.CreateEventResponseDto;
import com.albara.ticket.domain.dtos.response.GetEventDetailsResponseDto;
import com.albara.ticket.domain.dtos.response.ListEventResponseDto;
import com.albara.ticket.domain.dtos.response.UpdateEventResponseDto;
import com.albara.ticket.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.albara.ticket.util.JwtUtil.parseUserId;

@RestController
@RequestMapping(path = "/api/v1/events")
@RequiredArgsConstructor
public class EventController {

  private final EventService eventService;

  @PostMapping
  public ResponseEntity<CreateEventResponseDto> createEvent(
      @AuthenticationPrincipal Jwt jwt,
      @Valid @RequestBody CreateEventRequestDto createEventRequestDto) {
    UUID userId = parseUserId(jwt);

    CreateEventResponseDto createEventResponseDto = eventService.createEvent(userId, createEventRequestDto);
    return new ResponseEntity<>(createEventResponseDto, HttpStatus.CREATED);
  }

  @PutMapping(path = "/{eventId}")
  public ResponseEntity<UpdateEventResponseDto> updateEvent(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable UUID eventId,
      @Valid @RequestBody UpdateEventRequestDto updateEventRequestDto) {
    UUID userId = parseUserId(jwt);

    UpdateEventResponseDto updateEventResponseDto = eventService.updateEventForOrganizer(
      userId, eventId, updateEventRequestDto
    );

    return ResponseEntity.ok(updateEventResponseDto);
  }

  @GetMapping
  public ResponseEntity<Page<ListEventResponseDto>> listEvents(
      @AuthenticationPrincipal Jwt jwt, Pageable pageable
  ) {
    UUID userId = parseUserId(jwt);
    Page<ListEventResponseDto> events = eventService.listEventsForOrganizer(userId, pageable);
    return ResponseEntity.ok(events);
  }

  @GetMapping(path = "/{eventId}")
  public ResponseEntity<GetEventDetailsResponseDto> getEvent(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable UUID eventId
  ) {
    UUID userId = parseUserId(jwt);
    return eventService.getEventForOrganizer(userId, eventId)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping(path = "/{eventId}")
  public ResponseEntity<Void> deleteEvent(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable UUID eventId
  ) {
    UUID userId = parseUserId(jwt);
    eventService.deleteEventForOrganizer(userId, eventId);
    return ResponseEntity.noContent().build();
  }
}
