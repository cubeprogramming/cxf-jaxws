// This is client configuration with both digital signature and HTTPs encryption

package com.codenotfound.client;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.configuration.security.FiltersType;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.ext.logging.LoggingInInterceptor;
import org.apache.cxf.ext.logging.LoggingOutInterceptor;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.common.gzip.GZIPInInterceptor;
import org.apache.cxf.transport.common.gzip.GZIPOutInterceptor;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.example.ticketagent_wsdl11.TicketAgent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

@Configuration
public class ClientConfig {

  @Value("${client.ticketagent.address}")
  private String address;

  @Value("${client.ticketagent.endpoint}")
  private String endpoint;

  @Value("${client.ticketagent.keystore-alias}")
  private String keystoreAlias;

  @Value("${client.ticketagent.trust-store}")
  private Resource trustStoreResource;

  @Value("${client.ticketagent.trust-store-password}")
  private String trustStorePassword;

  @Bean(name = "ticketAgentProxyBean")
  public TicketAgent ticketAgent() {
    JaxWsProxyFactoryBean jaxWsProxyFactoryBean =
        new JaxWsProxyFactoryBean();
    jaxWsProxyFactoryBean.setServiceClass(TicketAgent.class);
    jaxWsProxyFactoryBean.setAddress(address + endpoint);

    // add the WSS4J OUT interceptor to sign the request message
    jaxWsProxyFactoryBean.getOutInterceptors().add(clientWssOut());
    // add the WSS4J IN interceptor to verify the signature on the response message
    jaxWsProxyFactoryBean.getInInterceptors().add(clientWssIn());

    // add compression
    GZIPOutInterceptor compressor = new GZIPOutInterceptor();
    compressor.setThreshold(0);
    compressor.setForce(true); //For some reason no compression happens unless forced
    jaxWsProxyFactoryBean.getInInterceptors().add(new GZIPInInterceptor());
    jaxWsProxyFactoryBean.getOutInterceptors().add(compressor);

    // log the request and response messages
    jaxWsProxyFactoryBean.getInInterceptors()
        .add(loggingInInterceptor());
    jaxWsProxyFactoryBean.getOutInterceptors()
        .add(loggingOutInterceptor());

    return (TicketAgent) jaxWsProxyFactoryBean.create();
  }

  @Bean
  public HTTPConduit ticketAgentConduit()
          throws NoSuchAlgorithmException, KeyStoreException,
          CertificateException, IOException {
    Client client = ClientProxy.getClient(ticketAgent());

    HTTPConduit httpConduit = (HTTPConduit) client.getConduit();
    httpConduit.setTlsClientParameters(tlsClientParameters());

    return httpConduit;
  }


  @Bean
  public TLSClientParameters tlsClientParameters()
          throws NoSuchAlgorithmException, KeyStoreException,
          CertificateException, IOException {
    TLSClientParameters tlsClientParameters =
            new TLSClientParameters();
    tlsClientParameters.setSecureSocketProtocol("TLS");
    // should NOT be used in production
    tlsClientParameters.setDisableCNCheck(true);
    tlsClientParameters.setTrustManagers(trustManagers());
    tlsClientParameters.setCipherSuitesFilter(cipherSuitesFilter());

    return tlsClientParameters;
  }


  @Bean
  public TrustManager[] trustManagers()
          throws NoSuchAlgorithmException, KeyStoreException,
          CertificateException, IOException {
    TrustManagerFactory trustManagerFactory = TrustManagerFactory
            .getInstance(TrustManagerFactory.getDefaultAlgorithm());
    trustManagerFactory.init(trustStore());

    return trustManagerFactory.getTrustManagers();
  }


  @Bean
  public KeyStore trustStore() throws KeyStoreException,
          NoSuchAlgorithmException, CertificateException, IOException {
    KeyStore trustStore = KeyStore.getInstance("JKS");
    trustStore.load(trustStoreResource.getInputStream(),
            trustStorePassword.toCharArray());

    return trustStore;
  }


  @Bean
  public FiltersType cipherSuitesFilter() {
    FiltersType filter = new FiltersType();
    filter.getInclude().add("TLS_ECDHE_RSA_.*");
    filter.getInclude().add("TLS_DHE_RSA_.*");

    return filter;
  }


  @Bean
  public Map<String, Object> clientOutProps() {
    Map<String, Object> clientOutProps = new HashMap<>();
    clientOutProps.put(WSHandlerConstants.ACTION,
        WSHandlerConstants.TIMESTAMP + " "
            + WSHandlerConstants.SIGNATURE);
    clientOutProps.put(WSHandlerConstants.USER, keystoreAlias);
    clientOutProps.put(WSHandlerConstants.PW_CALLBACK_CLASS,
        ClientCallbackHandler.class.getName());
    clientOutProps.put(WSHandlerConstants.SIG_PROP_FILE,
        "client_key.properties");

    return clientOutProps;
  }

  @Bean
  public WSS4JOutInterceptor clientWssOut() {
    WSS4JOutInterceptor clientWssOut = new WSS4JOutInterceptor();
    clientWssOut.setProperties(clientOutProps());

    return clientWssOut;
  }

  @Bean
  public Map<String, Object> clientInProps() {
    Map<String, Object> clientInProps = new HashMap<>();
    clientInProps.put(WSHandlerConstants.ACTION,
        WSHandlerConstants.TIMESTAMP + " "
            + WSHandlerConstants.SIGNATURE);
    clientInProps.put(WSHandlerConstants.SIG_PROP_FILE,
        "client_trust.properties");

    return clientInProps;
  }

  @Bean
  public WSS4JInInterceptor clientWssIn() {
    WSS4JInInterceptor clientWssIn = new WSS4JInInterceptor();
    clientWssIn.setProperties(clientInProps());

    return clientWssIn;
  }



  @Bean
  public LoggingInInterceptor loggingInInterceptor() {
    return new LoggingInInterceptor();
  }

  @Bean
  public LoggingOutInterceptor loggingOutInterceptor() {
    return new LoggingOutInterceptor();
  }
}
