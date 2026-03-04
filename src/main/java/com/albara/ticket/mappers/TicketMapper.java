package com.albara.ticket.mappers;

import com.albara.ticket.domain.dtos.response.GetTicketResponseDto;
import com.albara.ticket.domain.dtos.response.ListTicketResponseDto;
import com.albara.ticket.domain.dtos.response.ListTicketTicketTypeResponseDto;
import com.albara.ticket.domain.entities.Ticket;
import com.albara.ticket.domain.entities.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {

  ListTicketTicketTypeResponseDto toListTicketTicketTypeResponseDto(TicketType ticketType);

  ListTicketResponseDto toListTicketResponseDto(Ticket ticket);

//  @Mapping(target = "price", source = "ticket.ticketType.price")
//  @Mapping(target = "description", source = "ticket.ticketType.description")
//  @Mapping(target = "eventName", source = "ticket.ticketType.event.name")
//  @Mapping(target = "eventVenue", source = "ticket.ticketType.event.venue")
//  @Mapping(target = "eventStart", source = "ticket.ticketType.event.start")
//  @Mapping(target = "eventEnd", source = "ticket.ticketType.event.end")
//  GetTicketResponseDto toGetTicketResponseDto(Ticket ticket);
  @Mapping(target = "price", source = "ticketType.price")
  @Mapping(target = "description", source = "ticketType.description")
  @Mapping(target = "eventName", source = "ticketType.event.name")
  @Mapping(target = "eventVenue", source = "ticketType.event.venue")
  @Mapping(target = "eventStart", source = "ticketType.event.start")
  @Mapping(target = "eventEnd", source = "ticketType.event.end")
  GetTicketResponseDto toGetTicketResponseDto(Ticket ticket);
}
