package com.codenotfound.client;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

import lombok.extern.log4j.Log4j2;
import org.example.ticketagent.ObjectFactory;
import org.example.ticketagent.TFlightsResponse;
import org.example.ticketagent.TListFlights;
import org.example.ticketagent_wsdl11.TicketAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class TicketAgentClient {

  @Autowired
  private TicketAgent ticketAgentProxyBean;

  public List<BigInteger> listFlights() {
    log.info("Inside listFlights client");

    ObjectFactory factory = new ObjectFactory();
    TListFlights tListFlights = factory.createTListFlights();

    TFlightsResponse response = Objects.requireNonNull(
        ticketAgentProxyBean.listFlights(tListFlights));

    return response.getFlightNumber();
  }
}
