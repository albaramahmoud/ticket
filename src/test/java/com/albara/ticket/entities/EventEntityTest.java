package com.albara.ticket.entities;

import com.albara.ticket.domain.entities.Event;
import com.albara.ticket.domain.entities.EventStatusEnum;
import com.albara.ticket.domain.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnableJpaAuditing
class EventEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldSaveAndLoadEventWithAuditing() {
        // Arrange
        User organizer = new User();
        organizer.setId(UUID.randomUUID());
        organizer.setName("albara");
        organizer.setEmail("albara@test.com");
        organizer.setOrganizedEvents(new ArrayList<>());
        organizer.setAttendingEvents(new ArrayList<>());
        organizer.setStaffingEvents(new ArrayList<>());
        organizer.setCreatedAt(LocalDateTime.now());
        organizer.setUpdatedAt(LocalDateTime.now());

        entityManager.persist(organizer);

        Event event = new Event();
        event.setName("Event Name");
        event.setStart(LocalDateTime.now().plusDays(1));
        event.setEnd(LocalDateTime.now().plusDays(1).plusHours(2));
        event.setVenue("Amman");
        event.setSalesStart(LocalDateTime.now());
        event.setSalesEnd(LocalDateTime.now().plusHours(5));
        event.setStatus(EventStatusEnum.DRAFT);
        event.setOrganizer(organizer);
        event.setCreatedAt(LocalDateTime.now());
        event.setUpdatedAt(LocalDateTime.now());

        entityManager.persist(event);

        entityManager.flush();
        entityManager.clear();

        // Act
        Event foundEvent = entityManager.find(Event.class, event.getId());

        // Assert
        assertThat(foundEvent).isNotNull();
        assertThat(foundEvent.getId()).isNotNull();
        assertThat(foundEvent.getName()).isEqualTo("Event Name");
        assertThat(foundEvent.getCreatedAt()).isNotNull();
        assertThat(foundEvent.getUpdatedAt()).isNotNull();
        assertThat(foundEvent.getOrganizer()).isNotNull();
        assertThat(foundEvent.getOrganizer().getId()).isEqualTo(organizer.getId());
    }
}
