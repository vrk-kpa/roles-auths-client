/**
 * The MIT License
 * Copyright (c) 2022 Digital and Population Data Services Agency
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

import fi.vm.kapa.rova.client.common.*;
import fi.vm.kapa.rova.client.model.PartyIssues;
import fi.vm.kapa.rova.client.xroad.XRoadClientConfig;
import fi.vm.kapa.rova.client.xroad.YhaXRoadClient;
import fi.vm.kapa.xml.rova.api.orgpersonmandates.*;
import fi.vm.kapa.xml.rova.api.orgpersonmandates.RovaOrgPersonMandatesPortType;
import fi.vm.kapa.xml.rova.api.orgpersonmandates.RovaOrgPersonMandatesService_Service;

import javax.xml.bind.JAXBElement;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;
import javax.xml.ws.handler.HandlerResolver;
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
