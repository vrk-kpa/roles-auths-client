/**
 * The MIT License
 * Copyright (c) 2016 Population Register Centre
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fi.vm.kapa.rova.client.xroad.impl;

import fi.vm.kapa.rova.client.common.ClientException;
import fi.vm.kapa.rova.client.common.EndPoint;
import fi.vm.kapa.rova.client.common.RovaServiceDetails;
import fi.vm.kapa.rova.client.common.RovaServices;
import fi.vm.kapa.rova.client.common.Server;
import fi.vm.kapa.rova.client.model.Authorization;
import fi.vm.kapa.rova.client.model.AuthorizationList;
import fi.vm.kapa.rova.client.model.DecisionReason;
import fi.vm.kapa.rova.client.xroad.HpaXRoadClient;
import fi.vm.kapa.rova.client.xroad.XRoadClientConfig;
import fi.vm.kapa.xml.rova.api.authorization.*;
import fi.vm.kapa.xml.rova.api.authorization.DecisionReasonType;
import fi.vm.kapa.xml.rova.api.authorization.ObjectFactory;
import fi.vm.kapa.xml.rova.api.authorization.Request;
import fi.vm.kapa.xml.rova.api.authorization.list.*;

import javax.xml.bind.JAXBElement;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;
import javax.xml.ws.handler.HandlerResolver;
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

    public HpaXRoadRiClient(XRoadClientConfig config) {
        RovaServiceDetails details = RovaServices.getDetails(RovaServices.RovaService.AUTHORIZATION.name());
        for (Server server : config.getServers()) {
            endPoints.add(new EndPoint(server, details.getPath()));
        }
        HandlerResolver hs = createHandlerResolver(config, details);
        rovaAuthorizationService.setHandlerResolver(hs);

        details = RovaServices.getDetails(RovaServices.RovaService.AUTHORIZATION_LIST.name());
        hs = createHandlerResolver(config, details);
        rovaAuthorizationListService.setHandlerResolver(hs);
    }

    public Authorization isAuthorized(String userId, String delegateId, String principalId, Set<String> issues) {
        if (userId == null || delegateId == null || principalId == null) {
            throw new IllegalArgumentException("null value in required argument.");
        }

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

        System.out.println(rovaAuthorizationListService.getHandlerResolver());

        RovaAuthorizationListPortType port = rovaAuthorizationListService.getRovaAuthorizationListPort();
        BindingProvider bp = (BindingProvider) port;
        bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getNextEndpoint());

        Holder<fi.vm.kapa.xml.rova.api.authorization.list.Request> request = new Holder<>(authorizationListFactory.createRequest());
        Holder<RovaAuthorizationListResponse> response = new Holder(factory.createRovaAuthorizationResponse());
        request.value.setDelegateIdentifier(delegateId);
        request.value.setPrincipalIdentifier(principalId);
        headerHandler.setUserId(userId);

        port.rovaAuthorizationListService(request, response);

        RovaAuthorizationListResponse authResult = null;
        if (response.value != null) {
            authResult = response.value;
        }

        AuthorizationList auth;
        if (authResult.getRoles().getRoles() != null && authResult.getRoles().getRoles().size() > 0) {
            auth = new AuthorizationList(authResult.getRoles().getRoles());
        } else if (authResult.getRoles().getRoles() != null && authResult.getRoles().getRoles().size() == 0) {
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
