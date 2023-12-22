package fi.vm.kapa.rova.client.xroad.impl;

import fi.vm.kapa.rova.client.common.*;
import fi.vm.kapa.rova.client.model.Authorization;
import fi.vm.kapa.rova.client.model.AuthorizationList;
import fi.vm.kapa.rova.client.model.DecisionReason;
import fi.vm.kapa.rova.client.xroad.HpaXRoadClient;
import fi.vm.kapa.rova.client.xroad.XRoadClientConfig;
import fi.vm.kapa.xml.rova.api.authorization.*;
import fi.vm.kapa.xml.rova.api.authorization.list.RovaAuthorizationListPortType;
import fi.vm.kapa.xml.rova.api.authorization.list.RovaAuthorizationListResponse;
import fi.vm.kapa.xml.rova.api.authorization.list.RovaAuthorizationListService_Service;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.Holder;
import jakarta.xml.ws.handler.HandlerResolver;
import java.util.ArrayList;
import java.util.Set;

/**
 * Client implementation for querying possibilities to operate on behalf of another person.
 */
public class HpaXRoadRiClient extends AbstractRiClient implements HpaXRoadClient {

    private RovaAuthorizationService_Service rovaAuthorizationService = new RovaAuthorizationService_Service();

    private RovaAuthorizationListService_Service rovaAuthorizationListService = new RovaAuthorizationListService_Service();

    private ObjectFactory factory = new ObjectFactory();

    private fi.vm.kapa.xml.rova.api.authorization.list.ObjectFactory authorizationListFactory = new fi.vm.kapa.xml.rova.api.authorization.list.ObjectFactory();

    private XRoadClientConfig clientConfig;

    public HpaXRoadRiClient(XRoadClientConfig config) {
        this.clientConfig = config;
    }

    public Authorization isAuthorized(String userId, String delegateId, String principalId, Set<String> issues) {
        if (userId == null || delegateId == null || principalId == null) {
            throw new IllegalArgumentException("null value in required argument.");
        }

        RovaServiceDetails details = RovaServices.getDetails(RovaServices.RovaService.AUTHORIZATION.name());
        for (Server server : clientConfig.getServers()) {
            endPoints.add(new EndPoint(server, details.getPath()));
        }
        details = RovaServices.getDetails(RovaServices.RovaService.AUTHORIZATION.name());
        HandlerResolver hs = createHandlerResolver(clientConfig, details);
        rovaAuthorizationService.setHandlerResolver(hs);

        RovaAuthorizationPortType port = rovaAuthorizationService.getRovaAuthorizationPort();
        BindingProvider bp = (BindingProvider) port;
        bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getNextEndpoint());

        Holder<Request> request = new Holder<>(factory.createRequest());
        Holder<RovaAuthorizationResponse> response = new Holder<>(factory.createRovaAuthorizationResponse());
        request.value.setDelegateIdentifier(delegateId);
        if (issues != null && !issues.isEmpty()) {
            request.value.getIssue().addAll(issues);
        }
        request.value.setPrincipalIdentifier(principalId);
        headerHandler.setUserId(userId);

        port.rovaAuthorizationService(request, response);

        AuthorizationType authResult = null;
        if (response.value != null) {
            authResult = response.value.getAuthorization();
        }

        Authorization auth;
        if (authResult == AuthorizationType.ALLOWED) {
            auth = new Authorization(Authorization.Result.ALLOWED);
        } else if (authResult == AuthorizationType.DISALLOWED) {
            auth = new Authorization(Authorization.Result.DISALLOWED);
            for (DecisionReasonType reason : response.value.getReason()) {
                auth.getReasons().add(new DecisionReason(reason.getRule(), reason.getValue(), null));
            }
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
        return auth;
    }

    @Override
    public AuthorizationList getAuthorizationList(String userId, String delegateId, String principalId) {
        if (userId == null || delegateId == null || principalId == null) {
            throw new IllegalArgumentException("null value in required argument.");
        }

        RovaServiceDetails details = RovaServices.getDetails(RovaServices.RovaService.AUTHORIZATION_LIST.name());
        for (Server server : clientConfig.getServers()) {
            endPoints.add(new EndPoint(server, details.getPath()));
        }
        details = RovaServices.getDetails(RovaServices.RovaService.AUTHORIZATION_LIST.name());
        HandlerResolver hs = createHandlerResolver(clientConfig, details);
        rovaAuthorizationListService.setHandlerResolver(hs);

        RovaAuthorizationListPortType port = rovaAuthorizationListService.getRovaAuthorizationListPort();

        BindingProvider bp = (BindingProvider) port;
        bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getNextEndpoint());

        Holder<fi.vm.kapa.xml.rova.api.authorization.list.Request> request = new Holder<>(authorizationListFactory.createRequest());
        Holder<RovaAuthorizationListResponse> response = new Holder(authorizationListFactory.createRovaAuthorizationListResponse());
        request.value.setDelegateIdentifier(delegateId);
        request.value.setPrincipalIdentifier(principalId);

        headerHandler.setUserId(userId);

        port.rovaAuthorizationListService(request, response);

        RovaAuthorizationListResponse authResult = null;
        if (response.value != null) {
            authResult = response.value;
        }

        AuthorizationList auth;
        if (authResult.getRoles().getRole() != null && authResult.getRoles().getRole().size() > 0) {
            auth = new AuthorizationList(authResult.getRoles().getRole());
        } else if (authResult.getRoles().getRole() != null && authResult.getRoles().getRole().size() == 0) {
            auth = new AuthorizationList(new ArrayList<>());
            for (fi.vm.kapa.xml.rova.api.authorization.list.DecisionReasonType reason : response.value.getReason()) {
                auth.getReasons().add(new DecisionReason(reason.getRule(), reason.getValue(), null));
            }
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
        return auth;
    }
}
