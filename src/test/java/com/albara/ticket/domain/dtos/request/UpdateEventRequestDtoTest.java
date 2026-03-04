package com.albara.ticket.domain.dtos.request;

import com.albara.ticket.domain.entities.EventStatusEnum;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateEventRequestDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldValidateValidDto() {
        UpdateTicketTypeRequestDto ticketType = new UpdateTicketTypeRequestDto(UUID.randomUUID(), "VIP", 100.0, "VIP Access", 50);
        UpdateEventRequestDto dto = new UpdateEventRequestDto(
                UUID.randomUUID(),
                "My Concert",
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(5).plusHours(3),
                "Stadium",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(4),
                EventStatusEnum.DRAFT,
                List.of(ticketType)
        );

        Set<ConstraintViolation<UpdateEventRequestDto>> violations = validator.validate(dto);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldFailValidationWhenIdIsNull() {
        UpdateTicketTypeRequestDto ticketType = new UpdateTicketTypeRequestDto(UUID.randomUUID(), "VIP", 100.0, "VIP Access", 50);
        UpdateEventRequestDto dto = new UpdateEventRequestDto(
                null,
                "My Concert",
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(5).plusHours(3),
                "Stadium",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(4),
                EventStatusEnum.DRAFT,
                List.of(ticketType)
        );

        Set<ConstraintViolation<UpdateEventRequestDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Event ID must be provided");
    }

    @Test
    void shouldFailValidationWhenNameIsBlank() {
        UpdateTicketTypeRequestDto ticketType = new UpdateTicketTypeRequestDto(UUID.randomUUID(), "VIP", 100.0, "VIP Access", 50);
        UpdateEventRequestDto dto = new UpdateEventRequestDto(
                UUID.randomUUID(),
                "",
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(5).plusHours(3),
                "Stadium",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(4),
                EventStatusEnum.DRAFT,
                List.of(ticketType)
        );

        Set<ConstraintViolation<UpdateEventRequestDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Event name is required");
    }

    @Test
    void shouldFailValidationWhenVenueIsBlank() {
        UpdateTicketTypeRequestDto ticketType = new UpdateTicketTypeRequestDto(UUID.randomUUID(), "VIP", 100.0, "VIP Access", 50);
        UpdateEventRequestDto dto = new UpdateEventRequestDto(
                UUID.randomUUID(),
                "My Concert",
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(5).plusHours(3),
                "",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(4),
                EventStatusEnum.DRAFT,
                List.of(ticketType)
        );

        Set<ConstraintViolation<UpdateEventRequestDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Venue information is required");
    }

    @Test
    void shouldFailValidationWhenStatusIsNull() {
        UpdateTicketTypeRequestDto ticketType = new UpdateTicketTypeRequestDto(UUID.randomUUID(), "VIP", 100.0, "VIP Access", 50);
        UpdateEventRequestDto dto = new UpdateEventRequestDto(
                UUID.randomUUID(),
                "My Concert",
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(5).plusHours(3),
                "Stadium",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(4),
                null,
                List.of(ticketType)
        );

        Set<ConstraintViolation<UpdateEventRequestDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Event status must be provided");
    }

    @Test
    void shouldFailValidationWhenTicketTypesIsEmpty() {
        UpdateEventRequestDto dto = new UpdateEventRequestDto(
                UUID.randomUUID(),
                "My Concert",
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(5).plusHours(3),
                "Stadium",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(4),
                EventStatusEnum.DRAFT,
                Collections.emptyList()
        );

        Set<ConstraintViolation<UpdateEventRequestDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("At least one ticket type is required");
    }
}
