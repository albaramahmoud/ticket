package com.albara.ticket.mappers;

import com.albara.ticket.domain.dtos.response.TicketValidationResponseDto;
import com.albara.ticket.domain.entities.Ticket;
import com.albara.ticket.domain.entities.TicketValidation;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TicketValidationMapperTest {

    private final TicketValidationMapper mapper = Mappers.getMapper(TicketValidationMapper.class);

    @Test
    void shouldMapTicketValidationToTicketValidationResponseDto() {
        Ticket ticket = new Ticket();
        ticket.setId(UUID.randomUUID());

        TicketValidation ticketValidation = new TicketValidation();
        ticketValidation.setTicket(ticket);

        TicketValidationResponseDto dto = mapper.toTicketValidationResponseDto(ticketValidation);

        assertThat(dto).isNotNull();
        assertThat(dto.getTicketId()).isEqualTo(ticket.getId());
    }
}
