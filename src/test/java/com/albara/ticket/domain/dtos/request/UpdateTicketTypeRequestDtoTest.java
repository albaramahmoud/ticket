package com.albara.ticket.domain.dtos.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateTicketTypeRequestDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldValidateValidDto() {
        UpdateTicketTypeRequestDto dto = new UpdateTicketTypeRequestDto(UUID.randomUUID(), "VIP", 100.0, "VIP Access", 50);

        Set<ConstraintViolation<UpdateTicketTypeRequestDto>> violations = validator.validate(dto);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldFailValidationWhenNameIsBlank() {
        UpdateTicketTypeRequestDto dto = new UpdateTicketTypeRequestDto(UUID.randomUUID(), "", 100.0, "VIP Access", 50);

        Set<ConstraintViolation<UpdateTicketTypeRequestDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Ticket type name is required");
    }

    @Test
    void shouldFailValidationWhenPriceIsNull() {
        UpdateTicketTypeRequestDto dto = new UpdateTicketTypeRequestDto(UUID.randomUUID(), "VIP", null, "VIP Access", 50);

        Set<ConstraintViolation<UpdateTicketTypeRequestDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Price is required");
    }

    @Test
    void shouldFailValidationWhenPriceIsNegative() {
        UpdateTicketTypeRequestDto dto = new UpdateTicketTypeRequestDto(UUID.randomUUID(), "VIP", -10.0, "VIP Access", 50);

        Set<ConstraintViolation<UpdateTicketTypeRequestDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Price must be zero or greater");
    }
}
