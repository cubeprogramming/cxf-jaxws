package com.codenotfound.endpoint;

import com.codenotfound.client.TicketAgentClient;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.common.gzip.GZIPFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

@Configuration
public class EndpointConfig {

  @Value("${server.endpoint}")
  private String serviceEndpoint;

  @Autowired
  private Bus bus;

  @Autowired
  private TicketAgentProxyImpl ticketAgentProxy;

  @Bean
  public Endpoint endpoint() {
    EndpointImpl endpoint =
//        new EndpointImpl(bus, new TicketAgentProxyImpl());
          new EndpointImpl(bus, ticketAgentProxy);
    endpoint.publish(serviceEndpoint);

    GZIPFeature feature = new GZIPFeature();
    feature.setThreshold(0);
    feature.setForce(true);
    feature.initialize(endpoint, bus);

    return endpoint;
  }
}
