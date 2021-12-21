package com.codenotfound;

import java.io.BufferedReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

import io.restassured.RestAssured;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j2;
import org.apache.cxf.helpers.IOUtils;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.codenotfound.client.TicketAgentClient;

import javax.xml.datatype.DatatypeFactory;

//@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@Log4j2
public class SpringCxfClientAppTests {

  @Autowired
  private TicketAgentClient ticketAgentClient;

  @Value("${test.address}")
  String serviceAddress;

  @Value("${server.endpoint}")
  String serviceEndpoint;

  @Test
  public void testListFlights() {
    assertThat(Objects.requireNonNull(ticketAgentClient.listFlights().get(0)))
        .isEqualTo(BigInteger.valueOf(101));
  }

  @Test
  public void testRawSoap() throws Exception {

//    Path requestPath = Paths.get(getClass().getClassLoader().getResource("UpdateThirdPartyAccess.xml").toURI());
    Path requestPath = Paths.get(getClass().getClassLoader().getResource("testSoap.xml").toURI());

    BufferedReader fileReader = Files.newBufferedReader(requestPath, StandardCharsets.UTF_8);

//    RestAssured.baseURI="http://0.0.0.0:8082/DEVWebService";
    RestAssured.baseURI = "" + serviceAddress + serviceEndpoint;

//    String randomUuid = IOUtils.toString(fileReader).replaceAll(Pattern.quote("RANDOM_UUID"), UUID.randomUUID().toString());
//    String requestContent = randomUuid.replaceAll(Pattern.quote("DATE_SENT"), DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar() ).toString());
    String requestContent = IOUtils.toString(fileReader);

    log.debug(requestContent);

    Response response=RestAssured.given()
            .header("Content-Type", "text/xml")
            .and()
            .body(requestContent)
            .when()
            .post("")
            .then()
            .statusCode(200)
            .and()
            .log().all()
            .extract().response();

    XmlPath jsXpath= new XmlPath(response.asString());//Converting string into xml path to assert
    String flightNumber=jsXpath.getString("flightNumber");
    log.debug("Response: " +  flightNumber);
    assertThat(flightNumber).isEqualTo("101");
  }
}
