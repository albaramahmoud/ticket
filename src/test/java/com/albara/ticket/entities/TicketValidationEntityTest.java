package com.albara.ticket.entities;

import com.albara.ticket.domain.entities.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnableJpaAuditing
class TicketValidationEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldSaveAndLoadTicketValidation() {
        // Arrange
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        entityManager.persist(user);

        Event event = new Event();
        event.setName("Event Name");
        event.setStart(LocalDateTime.now().plusDays(1));
        event.setEnd(LocalDateTime.now().plusDays(1).plusHours(2));
        event.setVenue("Amman");
        event.setSalesStart(LocalDateTime.now());
        event.setSalesEnd(LocalDateTime.now().plusHours(5));
        event.setStatus(EventStatusEnum.DRAFT);
        event.setOrganizer(user);
        event.setCreatedAt(LocalDateTime.now());
        event.setUpdatedAt(LocalDateTime.now());
        entityManager.persist(event);

        TicketType ticketType = new TicketType();
        ticketType.setName("VIP");
        ticketType.setPrice(100.0);
        ticketType.setTotalAvailable(50);
        ticketType.setEvent(event);
        ticketType.setCreatedAt(LocalDateTime.now());
        ticketType.setUpdatedAt(LocalDateTime.now());
        entityManager.persist(ticketType);

        Ticket ticket = new Ticket();
        ticket.setPurchaser(user);
        ticket.setTicketType(ticketType);
        ticket.setStatus(TicketStatusEnum.PURCHASED);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());
        entityManager.persist(ticket);

        TicketValidation ticketValidation = new TicketValidation();
        ticketValidation.setTicket(ticket);
        ticketValidation.setStatus(TicketValidationStatusEnum.VALID);
        ticketValidation.setValidationMethod(TicketValidationMethod.QR_SCAN);
        ticketValidation.setCreatedAt(LocalDateTime.now());
        ticketValidation.setUpdatedAt(LocalDateTime.now());
        entityManager.persist(ticketValidation);

        entityManager.flush();
        entityManager.clear();

        // Act
        TicketValidation foundValidation = entityManager.find(TicketValidation.class, ticketValidation.getId());

        // Assert
        assertThat(foundValidation).isNotNull();
        assertThat(foundValidation.getId()).isNotNull();
        assertThat(foundValidation.getStatus()).isEqualTo(TicketValidationStatusEnum.VALID);
        assertThat(foundValidation.getValidationMethod()).isEqualTo(TicketValidationMethod.QR_SCAN);
        assertThat(foundValidation.getTicket().getId()).isEqualTo(ticket.getId());
    }
}
