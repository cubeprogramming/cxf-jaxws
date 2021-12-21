package com.codenotfound.endpoint;

import com.codenotfound.client.TicketAgentClient;
import lombok.extern.log4j.Log4j2;
import org.example.ticketagent.ObjectFactory;
import org.example.ticketagent.TFlightsResponse;
import org.example.ticketagent.TListFlights;
import org.example.ticketagent_wsdl11.TicketAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Objects;

@Service
@Log4j2
public class TicketAgentProxyImpl implements TicketAgent {

  @Autowired
  private TicketAgentClient ticketAgentClient;

  @Override
  public TFlightsResponse listFlights(TListFlights body) {
    log.info("Inside listFlights in the endpoint");

    if (ticketAgentClient == null)
      ticketAgentClient = new TicketAgentClient();

//    BigInteger returnValue = BigInteger.valueOf(101);
    BigInteger returnValue = Objects.requireNonNull(ticketAgentClient.listFlights().get(0));

    log.info("Endpoint return value: " + returnValue);

    ObjectFactory factory = new ObjectFactory();
    TFlightsResponse response = factory.createTFlightsResponse();
    response.getFlightNumber().add(returnValue);

    return response;
  }
}
