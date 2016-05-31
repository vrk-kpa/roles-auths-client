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
package fi.vm.kapa.rova.client.xroad;

import fi.vm.kapa.rova.client.common.*;
import fi.vm.kapa.rova.client.model.YpaOrganization;
import fi.vm.kapa.xml.rova.api.orgroles.*;

import javax.xml.bind.JAXBElement;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;
import javax.xml.ws.handler.HandlerResolver;
import java.util.ArrayList;
import java.util.List;

/**
 * Client implementation for fetching information required when working on behalf of a company.
 */
public class YpaXRoadRiClient extends AbstractRiClient implements YpaXRoadClient {

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

        OrganizationListType organizationListType = getOrganizationalRoles(userId, delegateId);

        List<YpaOrganization> responseList = new ArrayList<>();
        if (organizationListType != null && organizationListType.getOrganization() != null) {
            for (OrganizationalRolesType ort : organizationListType.getOrganization()) {
                if (ort.getOrganizationIdentifier()!= null && ort.getName() != null) {
                    YpaOrganization respOrganization = new YpaOrganization();
                    respOrganization.setName(ort.getName());
                    respOrganization.setIdentifier(ort.getOrganizationIdentifier());
                    List<String> roleList = new ArrayList<>();
                    if (ort.getRoles() != null && ort.getRoles().getRole() != null) {
                        for (String role : ort.getRoles().getRole()) {
                            roleList.add(role);
                        }
                    }
                    respOrganization.setRoles(roleList);
                    responseList.add(respOrganization);
                }
            }
        }
        return responseList;
    }


    private OrganizationListType getOrganizationalRoles(String userId, String delegateId) {
        OrganizationListType result = null;

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
            if (olt != null && olt.getOrganization() != null) {
                result = response.value.getOrganizationList();
            } else {
                JAXBElement<String> message = response.value.getExceptionMessage();
                if (message != null) {
                    throw new ClientException("Unexpected response from server: " + message.getValue());
                }
            }
        }

        return result;
    }

}
