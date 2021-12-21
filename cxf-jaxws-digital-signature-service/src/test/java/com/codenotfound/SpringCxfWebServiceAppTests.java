package com.codenotfound;

import com.codenotfound.client.TicketAgentClient;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

//@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@Log4j2
public class SpringCxfWebServiceAppTests {

  @Autowired
  private TicketAgentClient ticketAgentClient;

  @Test
  public void testListFlights() {
    assertThat(ticketAgentClient.listFlights().get(0))
        .isEqualTo(BigInteger.valueOf(101));
  }
}
