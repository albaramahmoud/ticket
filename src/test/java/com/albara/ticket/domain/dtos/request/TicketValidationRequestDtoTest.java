package com.albara.ticket.domain.dtos.request;

import com.albara.ticket.domain.entities.TicketValidationMethod;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TicketValidationRequestDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldValidateValidDto() {
        TicketValidationRequestDto dto = new TicketValidationRequestDto(UUID.randomUUID(), TicketValidationMethod.QR_SCAN);

        Set<ConstraintViolation<TicketValidationRequestDto>> violations = validator.validate(dto);

        assertThat(violations).isEmpty();
    }
}
