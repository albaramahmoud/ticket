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

import static org.assertj.core.api.Assertions.assertThat;

class CreateEventRequestDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldValidateValidDto() {
        CreateTicketTypeRequestDto ticketType = new CreateTicketTypeRequestDto("VIP", 100.0, "VIP Access", 50);
        CreateEventRequestDto dto = new CreateEventRequestDto(
                "My Concert",
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(5).plusHours(3),
                "Stadium",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(4),
                EventStatusEnum.DRAFT,
                List.of(ticketType)
        );

        Set<ConstraintViolation<CreateEventRequestDto>> violations = validator.validate(dto);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldFailValidationWhenNameIsBlank() {
        CreateTicketTypeRequestDto ticketType = new CreateTicketTypeRequestDto("VIP", 100.0, "VIP Access", 50);
        CreateEventRequestDto dto = new CreateEventRequestDto(
                "",
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(5).plusHours(3),
                "Stadium",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(4),
                EventStatusEnum.DRAFT,
                List.of(ticketType)
        );

        Set<ConstraintViolation<CreateEventRequestDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Event name is required");
    }

    @Test
    void shouldFailValidationWhenVenueIsBlank() {
        CreateTicketTypeRequestDto ticketType = new CreateTicketTypeRequestDto("VIP", 100.0, "VIP Access", 50);
        CreateEventRequestDto dto = new CreateEventRequestDto(
                "My Concert",
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(5).plusHours(3),
                "",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(4),
                EventStatusEnum.DRAFT,
                List.of(ticketType)
        );

        Set<ConstraintViolation<CreateEventRequestDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Venue information is required");
    }

    @Test
    void shouldFailValidationWhenStatusIsNull() {
        CreateTicketTypeRequestDto ticketType = new CreateTicketTypeRequestDto("VIP", 100.0, "VIP Access", 50);
        CreateEventRequestDto dto = new CreateEventRequestDto(
                "My Concert",
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(5).plusHours(3),
                "Stadium",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(4),
                null,
                List.of(ticketType)
        );

        Set<ConstraintViolation<CreateEventRequestDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Event status must be provided");
    }

    @Test
    void shouldFailValidationWhenTicketTypesIsEmpty() {
        CreateEventRequestDto dto = new CreateEventRequestDto(
                "My Concert",
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(5).plusHours(3),
                "Stadium",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(4),
                EventStatusEnum.DRAFT,
                Collections.emptyList()
        );

        Set<ConstraintViolation<CreateEventRequestDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("At least one ticket type is required");
    }
}
