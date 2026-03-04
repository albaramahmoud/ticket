package com.albara.ticket.mappers;

import com.albara.ticket.domain.dtos.response.*;
import com.albara.ticket.domain.entities.Event;
import com.albara.ticket.domain.entities.TicketType;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class EventMapperTest {

    private final EventMapper mapper = Mappers.getMapper(EventMapper.class);

    @Test
    void shouldMapEventToCreateEventResponseDto() {
        Event event = new Event();
        event.setId(UUID.randomUUID());
        event.setName("Test Event");

        CreateEventResponseDto dto = mapper.toDto(event);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(event.getId());
        assertThat(dto.getName()).isEqualTo(event.getName());
    }

    @Test
    void shouldMapEventToListEventResponseDto() {
        Event event = new Event();
        event.setId(UUID.randomUUID());
        event.setName("Test Event");

        ListEventResponseDto dto = mapper.toListEventResponseDto(event);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(event.getId());
        assertThat(dto.getName()).isEqualTo(event.getName());
    }

    @Test
    void shouldMapEventToGetEventDetailsResponseDto() {
        Event event = new Event();
        event.setId(UUID.randomUUID());
        event.setName("Test Event");

        GetEventDetailsResponseDto dto = mapper.toGetEventDetailsResponseDto(event);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(event.getId());
        assertThat(dto.getName()).isEqualTo(event.getName());
    }

    @Test
    void shouldMapEventToUpdateEventResponseDto() {
        Event event = new Event();
        event.setId(UUID.randomUUID());
        event.setName("Test Event");

        UpdateEventResponseDto dto = mapper.toUpdateEventResponseDto(event);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(event.getId());
        assertThat(dto.getName()).isEqualTo(event.getName());
    }

    @Test
    void shouldMapEventToListPublishedEventResponseDto() {
        Event event = new Event();
        event.setId(UUID.randomUUID());
        event.setName("Test Event");

        ListPublishedEventResponseDto dto = mapper.toListPublishedEventResponseDto(event);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(event.getId());
        assertThat(dto.getName()).isEqualTo(event.getName());
    }

    @Test
    void shouldMapEventToGetPublishedEventDetailsResponseDto() {
        Event event = new Event();
        event.setId(UUID.randomUUID());
        event.setName("Test Event");

        GetPublishedEventDetailsResponseDto dto = mapper.toGetPublishedEventDetailsResponseDto(event);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(event.getId());
        assertThat(dto.getName()).isEqualTo(event.getName());
    }

    @Test
    void shouldMapTicketTypeToListEventTicketTypeResponseDto() {
        TicketType ticketType = new TicketType();
        ticketType.setId(UUID.randomUUID());
        ticketType.setName("VIP");
        ticketType.setPrice(100.0);

        ListEventTicketTypeResponseDto dto = mapper.toDto(ticketType);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(ticketType.getId());
        assertThat(dto.getName()).isEqualTo(ticketType.getName());
        assertThat(dto.getPrice()).isEqualTo(ticketType.getPrice());
    }

    @Test
    void shouldMapTicketTypeToGetEventDetailsTicketTypesResponseDto() {
        TicketType ticketType = new TicketType();
        ticketType.setId(UUID.randomUUID());
        ticketType.setName("VIP");
        ticketType.setPrice(100.0);

        GetEventDetailsTicketTypesResponseDto dto = mapper.toGetEventDetailsTicketTypesResponseDto(ticketType);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(ticketType.getId());
        assertThat(dto.getName()).isEqualTo(ticketType.getName());
        assertThat(dto.getPrice()).isEqualTo(ticketType.getPrice());
    }

    @Test
    void shouldMapTicketTypeToUpdateTicketTypeResponseDto() {
        TicketType ticketType = new TicketType();
        ticketType.setId(UUID.randomUUID());
        ticketType.setName("VIP");
        ticketType.setPrice(100.0);

        UpdateTicketTypeResponseDto dto = mapper.toUpdateTicketTypeResponseDto(ticketType);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(ticketType.getId());
        assertThat(dto.getName()).isEqualTo(ticketType.getName());
        assertThat(dto.getPrice()).isEqualTo(ticketType.getPrice());
    }

    @Test
    void shouldMapTicketTypeToGetPublishedEventDetailsTicketTypesResponseDto() {
        TicketType ticketType = new TicketType();
        ticketType.setId(UUID.randomUUID());
        ticketType.setName("VIP");
        ticketType.setPrice(100.0);

        GetPublishedEventDetailsTicketTypesResponseDto dto = mapper.toGetPublishedEventDetailsTicketTypesResponseDto(ticketType);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(ticketType.getId());
        assertThat(dto.getName()).isEqualTo(ticketType.getName());
        assertThat(dto.getPrice()).isEqualTo(ticketType.getPrice());
    }
}
