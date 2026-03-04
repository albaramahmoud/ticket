package com.albara.ticket.mappers;

import com.albara.ticket.domain.dtos.response.GetTicketResponseDto;
import com.albara.ticket.domain.dtos.response.ListTicketResponseDto;
import com.albara.ticket.domain.dtos.response.ListTicketTicketTypeResponseDto;
import com.albara.ticket.domain.entities.Event;
import com.albara.ticket.domain.entities.Ticket;
import com.albara.ticket.domain.entities.TicketType;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TicketMapperTest {

    private final TicketMapper mapper = Mappers.getMapper(TicketMapper.class);

    @Test
    void shouldMapTicketToListTicketResponseDto() {
        Ticket ticket = new Ticket();
        ticket.setId(UUID.randomUUID());

        ListTicketResponseDto dto = mapper.toListTicketResponseDto(ticket);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(ticket.getId());
    }

    @Test
    void shouldMapTicketToGetTicketResponseDto() {
        Event event = new Event();
        event.setName("Test Event");
        event.setVenue("Test Venue");
        event.setStart(LocalDateTime.now());
        event.setEnd(LocalDateTime.now().plusHours(2));

        TicketType ticketType = new TicketType();
        ticketType.setPrice(100.0);
        ticketType.setDescription("VIP");
        ticketType.setEvent(event);

        Ticket ticket = new Ticket();
        ticket.setId(UUID.randomUUID());
        ticket.setTicketType(ticketType);

        GetTicketResponseDto dto = mapper.toGetTicketResponseDto(ticket);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(ticket.getId());
        assertThat(dto.getPrice()).isEqualTo(100.0);
        assertThat(dto.getDescription()).isEqualTo("VIP");
        assertThat(dto.getEventName()).isEqualTo("Test Event");
        assertThat(dto.getEventVenue()).isEqualTo("Test Venue");
        assertThat(dto.getEventStart()).isEqualTo(event.getStart());
        assertThat(dto.getEventEnd()).isEqualTo(event.getEnd());
    }

    @Test
    void shouldMapTicketTypeToListTicketTicketTypeResponseDto() {
        TicketType ticketType = new TicketType();
        ticketType.setId(UUID.randomUUID());
        ticketType.setName("VIP");
        ticketType.setPrice(100.0);

        ListTicketTicketTypeResponseDto dto = mapper.toListTicketTicketTypeResponseDto(ticketType);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(ticketType.getId());
        assertThat(dto.getName()).isEqualTo(ticketType.getName());
        assertThat(dto.getPrice()).isEqualTo(ticketType.getPrice());
    }
}
