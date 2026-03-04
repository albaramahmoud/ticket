package com.albara.ticket.controllers;

import com.albara.ticket.domain.dtos.ErrorDto;
import com.albara.ticket.exceptions.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldHandleTicketNotFoundException() {
        TicketNotFoundException exception = new TicketNotFoundException("Ticket not found");

        ResponseEntity<ErrorDto> response = handler.handleTicketNotFoundException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Ticket not found");
    }

    @Test
    void shouldHandleTicketsSoldOutException() {
        TicketsSoldOutException exception = new TicketsSoldOutException("Sold out");

        ResponseEntity<ErrorDto> response = handler.handleTicketsSoldOutException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Tickets are sold out for this ticket type");
    }

    @Test
    void shouldHandleQrCodeNotFoundException() {
        QrCodeNotFoundException exception = new QrCodeNotFoundException("QR not found");

        ResponseEntity<ErrorDto> response = handler.handleQrCodeNotFoundException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("QR code not found");
    }

    @Test
    void shouldHandleQrCodeGenerationException() {
        QrCodeGenerationException exception = new QrCodeGenerationException("Gen failed");

        ResponseEntity<ErrorDto> response = handler.handleQrCodeGenerationException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Unable to generate QR Code");
    }

    @Test
    void shouldHandleEventUpdateException() {
        EventUpdateException exception = new EventUpdateException("Update failed");

        ResponseEntity<ErrorDto> response = handler.handleEventUpdateException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Unable to update event");
    }

    @Test
    void shouldHandleTicketTypeNotFoundException() {
        TicketTypeNotFoundException exception = new TicketTypeNotFoundException("Type not found");

        ResponseEntity<ErrorDto> response = handler.handleTicketTypeNotFoundException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Ticket type not found");
    }

    @Test
    void shouldHandleEventNotFoundException() {
        EventNotFoundException exception = new EventNotFoundException("Event not found");

        ResponseEntity<ErrorDto> response = handler.handleEventNotFoundException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Event not found");
    }

    @Test
    void shouldHandleUserNotFoundException() {
        UserNotFoundException exception = new UserNotFoundException("User not found");

        ResponseEntity<ErrorDto> response = handler.handleUserNotFoundException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("User not found");
    }

    @Test
    void shouldHandleException() {
        Exception exception = new Exception("General error");

        ResponseEntity<ErrorDto> response = handler.handleException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("An unknown error occurred");
    }
}
