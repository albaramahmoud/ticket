package com.albara.ticket.domain.dtos.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CreateTicketTypeRequestDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldValidateValidDto() {
        CreateTicketTypeRequestDto dto = new CreateTicketTypeRequestDto("VIP", 100.0, "VIP Access", 50);

        Set<ConstraintViolation<CreateTicketTypeRequestDto>> violations = validator.validate(dto);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldFailValidationWhenNameIsBlank() {
        CreateTicketTypeRequestDto dto = new CreateTicketTypeRequestDto("", 100.0, "VIP Access", 50);

        Set<ConstraintViolation<CreateTicketTypeRequestDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Ticket type name is required");
    }

    @Test
    void shouldFailValidationWhenPriceIsNull() {
        CreateTicketTypeRequestDto dto = new CreateTicketTypeRequestDto("VIP", null, "VIP Access", 50);

        Set<ConstraintViolation<CreateTicketTypeRequestDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Price is required");
    }

    @Test
    void shouldFailValidationWhenPriceIsNegative() {
        CreateTicketTypeRequestDto dto = new CreateTicketTypeRequestDto("VIP", -10.0, "VIP Access", 50);

        Set<ConstraintViolation<CreateTicketTypeRequestDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Price must be zero or greater");
    }
}
