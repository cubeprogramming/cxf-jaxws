package org.example.ticketagent_wsdl11;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 3.4.1
 * 2020-12-10T20:16:00.659+01:00
 * Generated source version: 3.4.1
 *
 */
@WebService(targetNamespace = "http://example.org/TicketAgent.wsdl11", name = "TicketAgent")
@XmlSeeAlso({org.example.ticketagent.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface TicketAgent {

    @WebMethod
    @WebResult(name = "listFlightsResponse", targetNamespace = "http://example.org/TicketAgent.xsd", partName = "body")
    public org.example.ticketagent.TFlightsResponse listFlights(

        @WebParam(partName = "body", name = "listFlightsRequest", targetNamespace = "http://example.org/TicketAgent.xsd")
        org.example.ticketagent.TListFlights body,
        @WebParam(partName = "header", name = "listFlightsRequestHeader", targetNamespace = "http://example.org/TicketAgent.xsd", header = true)
        org.example.ticketagent.TListFlightsHeader header
    );
}
