package com.albara.ticket.services;

import com.albara.ticket.domain.entities.QrCode;
import com.albara.ticket.domain.entities.Ticket;

import java.util.UUID;

public interface QrCodeService {

  QrCode generateQrCode(Ticket ticket);

  byte[] getQrCodeImageForUserAndTicket(UUID userId, UUID ticketId);
}
