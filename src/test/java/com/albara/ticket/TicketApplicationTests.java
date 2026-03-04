package com.albara.ticket;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import com.albara.ticket.mappers.EventMapper;
import com.albara.ticket.mappers.TicketMapper;
import com.albara.ticket.mappers.TicketValidationMapper;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:testdb;NON_KEYWORDS=VALUE",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
class TicketApplicationTests {

	@MockBean private JwtDecoder jwtDecoder;
	@MockBean private EventMapper eventMapper;
	@MockBean private TicketMapper ticketMapper;
	@MockBean private TicketValidationMapper ticketValidationMapper;

	@Test
	void contextLoads() {}
}