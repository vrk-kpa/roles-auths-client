package fi.vm.kapa.rova.client.xroad.impl;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.Holder;
import jakarta.xml.ws.handler.HandlerResolver;

import fi.vm.kapa.rova.client.common.*;
import fi.vm.kapa.rova.client.model.PartyIssues;
import fi.vm.kapa.rova.client.xroad.XRoadClientConfig;
import fi.vm.kapa.rova.client.xroad.YyaXRoadClient;
import fi.vm.kapa.xml.rova.api.orgmandates.*;
import fi.vm.kapa.xml.rova.api.orgpersonmandates.RovaOrgPersonMandatesService_Service;

public class YyaXRoadRiClient extends AbstractRiClient implements YyaXRoadClient {

    private RovaOrgMandatesService_Service rovaOrgMandatesService = new RovaOrgMandatesService_Service();
    private ObjectFactory factory = new ObjectFactory();
    private XRoadClientConfig clientConfig;
    
    public YyaXRoadRiClient(XRoadClientConfig config) {
        this.clientConfig = config;
    }

    public List<PartyIssues> getOrganizationalMandates(String userId, String delegateId, List<String> principalIds) {
        if (delegateId == null || principalIds == null) {
            throw new IllegalArgumentException("null value in required argument.");
        }
        
        RovaServiceDetails details = RovaServices.getDetails(RovaServices.RovaService.ORG_MANDATES.name());
        for (Server server : clientConfig.getServers()) {
            endPoints.add(new EndPoint(server, details.getPath()));
        }
        details = RovaServices.getDetails(RovaServices.RovaService.ORG_MANDATES.name());
        HandlerResolver hs = createHandlerResolver(clientConfig, details);
        rovaOrgMandatesService.setHandlerResolver(hs);

        RovaOrgMandatesPortType port = rovaOrgMandatesService.getRovaOrgMandatesServicePort();
        BindingProvider bp = (BindingProvider) port;
        bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getNextEndpoint());

        Holder<Request> request = new Holder<>(factory.createRequest());
        Holder<Response> response = new Holder<>(factory.createResponse());
        request.value.setDelegate(delegateId);
        request.value.getPrincipal().addAll(principalIds);
        headerHandler.setUserId(userId);

        port.rovaOrgMandatesService(request, response);

        List<OrgMandatesType> orgMandatesResult = null;
        if (response.value != null && response.value.getPrincipalList() != null) {
            orgMandatesResult = response.value.getPrincipalList().getPrincipal();
        }

        List<PartyIssues> partyIssuesList = new ArrayList<>();
        if (orgMandatesResult != null) {
            orgMandatesResult.forEach(result -> {
                partyIssuesList.add(new PartyIssues(result.getPrincipalId(), result.getIssue(), result.getIncomplete().getValue()));
            });
        } else {
            String message = null;
            if (response.value != null) {
                JAXBElement<String> messageElem = response.value.getExceptionMessage();
                if (messageElem != null) {
                    message = messageElem.getValue();
                }
            }
            throw new ClientException("Unexpected response from server: " + message);
        }
        return partyIssuesList;
    }
}
