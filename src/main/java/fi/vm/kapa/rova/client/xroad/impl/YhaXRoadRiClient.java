package fi.vm.kapa.rova.client.xroad.impl;

import fi.vm.kapa.rova.client.common.*;
import fi.vm.kapa.rova.client.model.PartyIssues;
import fi.vm.kapa.rova.client.xroad.XRoadClientConfig;
import fi.vm.kapa.rova.client.xroad.YhaXRoadClient;
import fi.vm.kapa.xml.rova.api.orgpersonmandates.*;
import fi.vm.kapa.xml.rova.api.orgpersonmandates.RovaOrgPersonMandatesPortType;
import fi.vm.kapa.xml.rova.api.orgpersonmandates.RovaOrgPersonMandatesService_Service;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.Holder;
import jakarta.xml.ws.handler.HandlerResolver;
import java.util.ArrayList;
import java.util.List;

public class YhaXRoadRiClient extends AbstractRiClient implements YhaXRoadClient {

    private RovaOrgPersonMandatesService_Service rovaOrgPersonMandatesService = new RovaOrgPersonMandatesService_Service();
    private ObjectFactory factory = new ObjectFactory();
    private XRoadClientConfig clientConfig;

    public YhaXRoadRiClient(XRoadClientConfig config) {
        this.clientConfig = config;
    }

    @Override
    public List<PartyIssues> getOrganizationalPersonMandates(String userId, String delegateId, List<String> principalIds) {
        if (delegateId == null || principalIds == null) {
            throw new IllegalArgumentException("null value in required argument.");
        }

        RovaServiceDetails details = RovaServices.getDetails(RovaServices.RovaService.ORG_PERSON_MANDATES.name());
        for (Server server : clientConfig.getServers()) {
            endPoints.add(new EndPoint(server, details.getPath()));
        }

        HandlerResolver hs = createHandlerResolver(clientConfig, details);
        rovaOrgPersonMandatesService.setHandlerResolver(hs);

        RovaOrgPersonMandatesPortType port = rovaOrgPersonMandatesService.getRovaOrgPersonMandatesServicePort();
        BindingProvider bp = (BindingProvider) port;
        bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getNextEndpoint());

        Holder<Request> request = new Holder<>(factory.createRequest());
        Holder<Response> response = new Holder<>(factory.createResponse());
        request.value.setDelegate(delegateId);
        request.value.getPrincipal().addAll(principalIds);
        headerHandler.setUserId(userId);

        port.rovaOrgPersonMandatesService(request, response);

        List<OrgPersonMandatesType> orgPersonMandatesResult = null;
        if (response.value != null && response.value.getPrincipalList() != null) {
            orgPersonMandatesResult = response.value.getPrincipalList().getPrincipal();
        }

        List<PartyIssues> partyIssuesList = new ArrayList<>();
        if (orgPersonMandatesResult != null) {
            orgPersonMandatesResult.forEach(result -> {
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
