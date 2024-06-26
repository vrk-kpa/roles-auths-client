package fi.vm.kapa.rova.client.xroad.impl;

import fi.vm.kapa.rova.client.common.*;
import fi.vm.kapa.rova.client.model.YpaOrganization;
import fi.vm.kapa.rova.client.xroad.XRoadClientConfig;
import fi.vm.kapa.rova.client.xroad.YpaXRoadClient;
import fi.vm.kapa.xml.rova.api.orgroles.*;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.Holder;
import jakarta.xml.ws.handler.HandlerResolver;
import java.util.ArrayList;
import java.util.List;

/**
 * Client implementation for fetching information required when working on behalf of a company.
 */
public class YpaXRoadRiClient extends AbstractRiClient implements YpaXRoadClient {

    private static final String INCOMPLETE = "incomplete";

    private RovaOrganizationalRolesService_Service rovaRolesService = new RovaOrganizationalRolesService_Service();

    private ObjectFactory factory = new ObjectFactory();

    public YpaXRoadRiClient(XRoadClientConfig config) {
        RovaServiceDetails details = RovaServices.getDetails(RovaServices.RovaService.ROLES.name());
        for (Server server : config.getServers()) {
            endPoints.add(new EndPoint(server, details.getPath()));
        }
        HandlerResolver hs = createHandlerResolver(config, details);
        rovaRolesService.setHandlerResolver(hs);
    }

    public List<YpaOrganization> getRoles(String userId, String delegateId) {
        if (userId == null || delegateId == null) {
            throw new IllegalArgumentException("null value in required argument.");
        }
        OrganizationListType result = null;
        boolean complete = true;

        RovaOrganizationalRolesPortType port = rovaRolesService.getRovaOrganizationalRolesPort();
        BindingProvider bp = (BindingProvider) port;
        bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getNextEndpoint());

        Holder<Request> request = new Holder<>(factory.createRequest());
        Holder<Response> response = new Holder<>(factory.createResponse());
        request.value.setDelegateIdentifier(delegateId);
        headerHandler.setUserId(userId);

        port.rovaOrganizationalRolesService(request, response);
        if (response.value != null) {
            OrganizationListType olt = response.value.getOrganizationList();

            JAXBElement<String> expMsg = response.value.getExceptionMessage();
            if (expMsg != null && expMsg.getValue().equals(INCOMPLETE)) {
                complete = false;
            }

            if (olt != null && olt.getOrganization() != null) {
                result = response.value.getOrganizationList();
            } else {
                JAXBElement<String> message = response.value.getExceptionMessage();
                if (message != null && complete) {
                    throw new ClientException("Unexpected response from server: " + message.getValue());
                }
            }
        }

        return createResponseList(result, complete);
    }

    public List<YpaOrganization> createResponseList(OrganizationListType organizationListType, boolean complete) {

        List<YpaOrganization> responseList = new ArrayList<>();
        if (organizationListType != null && organizationListType.getOrganization() != null) {
            for (OrganizationalRolesType ort : organizationListType.getOrganization()) {
                if (ort.getOrganizationIdentifier() != null && ort.getName() != null) {
                    YpaOrganization respOrganization = new YpaOrganization(ort.getOrganizationIdentifier(), ort.getName(), complete);
                    List<String> roleList = respOrganization.getRoles();
                    if (ort.getRoles() != null && ort.getRoles().getRole() != null) {
                        for (String role : ort.getRoles().getRole()) {
                            roleList.add(role);
                        }
                    }
                    responseList.add(respOrganization);
                }
            }
        }
        return responseList;
    }

}
