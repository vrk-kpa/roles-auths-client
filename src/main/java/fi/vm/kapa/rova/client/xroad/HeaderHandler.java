package fi.vm.kapa.rova.client.xroad;

import fi.vm.kapa.rova.client.common.RovaServiceDetails;
import fi.vm.kapa.xml.rova.api.delegate.ObjectFactory;
import fi.vm.kapa.xml.rova.api.delegate.XRoadClientIdentifierType;
import fi.vm.kapa.xml.rova.api.delegate.XRoadObjectType;
import fi.vm.kapa.xml.rova.api.delegate.XRoadServiceIdentifierType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPHeaderElement;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.handler.soap.SOAPHandler;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Collections;
import java.util.Set;

/**
 * Header handler class for JAX-WS reference implementation clients
 */
public class HeaderHandler implements SOAPHandler<SOAPMessageContext> {

    private ObjectFactory factory = new ObjectFactory();
    private static final Logger logger = LoggerFactory.getLogger(HeaderHandler.class);

    private String userId;
    private XRoadClientConfig config;
    private RovaServiceDetails serviceDetails;
    private RequestIdGenerator requestIdGenerator = new DefaultRequestIdGenerator();

    public HeaderHandler(XRoadClientConfig config, RovaServiceDetails serviceDetails) {
        this.config = config;
        this.serviceDetails = serviceDetails;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext messageContext) {

        HeaderDetails cd = config.getClientDetails();
        HeaderDetails sd = config.getServiceDetails();

        if (config.getRequestIdGenerator() != null) {
            requestIdGenerator = config.getRequestIdGenerator();
        }

        Boolean outboundProperty = (Boolean) messageContext
                .get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        SOAPMessage soapMsg = messageContext.getMessage();
        SOAPEnvelope soapEnv;

        if (outboundProperty.booleanValue()) {
            try {
                soapEnv = soapMsg.getSOAPPart().getEnvelope();
                SOAPHeader header = soapEnv.getHeader();
                if (header == null) {
                    header = soapEnv.addHeader();
                }

                String id = requestIdGenerator.generateId();

                JAXBElement<String> idElement = factory.createId(id);
                SOAPHeaderElement idHeaderElement = header
                        .addHeaderElement(idElement.getName());
                idHeaderElement.addTextNode(idElement.getValue());

                JAXBElement<String> protocolVersionElement = factory.createProtocolVersion("4.0");
                SOAPHeaderElement protocolVersionHeaderElement = header.addHeaderElement(protocolVersionElement.getName());
                protocolVersionHeaderElement.addTextNode(protocolVersionElement.getValue());

                JAXBElement<String> userIdElement = factory
                        .createUserId(userId);
                SOAPHeaderElement uidHeaderElement = header
                        .addHeaderElement(userIdElement.getName());
                uidHeaderElement.addTextNode(userIdElement.getValue());

                XRoadClientIdentifierType clientHeaderType = factory.createXRoadClientIdentifierType();
                JAXBElement<XRoadClientIdentifierType> clientElement = factory.createClient(clientHeaderType);

                clientHeaderType.setObjectType(XRoadObjectType.SUBSYSTEM);
                clientHeaderType.setXRoadInstance(cd.getXRoadInstance());
                clientHeaderType.setMemberClass(cd.getMemberClass());
                clientHeaderType.setMemberCode(cd.getMemberCode());
                clientHeaderType.setSubsystemCode(cd.getSubsystemCode());

                Marshaller marshaller;
                marshaller = JAXBContext.newInstance(
                        XRoadClientIdentifierType.class).createMarshaller();
                marshaller.marshal(clientElement, header);

                XRoadServiceIdentifierType serviceHeaderType = factory
                        .createXRoadServiceIdentifierType();
                JAXBElement<XRoadServiceIdentifierType> serviceElement = factory
                        .createService(serviceHeaderType);
                serviceHeaderType.setObjectType(XRoadObjectType.SERVICE);
                serviceHeaderType.setXRoadInstance(sd.getXRoadInstance());
                serviceHeaderType.setMemberClass(sd.getMemberClass());
                serviceHeaderType.setMemberCode(sd.getMemberCode());
                serviceHeaderType.setSubsystemCode(sd.getSubsystemCode());
                serviceHeaderType.setServiceVersion(sd.getVersion());
                serviceHeaderType.setServiceCode(serviceDetails.getXRoadServiceCode());

                marshaller = JAXBContext.newInstance(
                        XRoadServiceIdentifierType.class).createMarshaller();
                marshaller.marshal(serviceElement, header);

                soapMsg.saveChanges();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        return true;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public Set<QName> getHeaders() {
        return Collections.emptySet();
    }

    @Override
    public boolean handleFault(SOAPMessageContext messageContext) {
        return true;
    }

    @Override
    public void close(MessageContext messageContext) {
        // NOP
    }

}
