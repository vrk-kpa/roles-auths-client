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
package fi.vm.kapa.rova.client.webapi.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.vm.kapa.rova.client.model.Authorization;
import fi.vm.kapa.rova.client.model.Principal;
import fi.vm.kapa.rova.client.webapi.HpaWebApiClient;
import fi.vm.kapa.rova.client.webapi.WebApiClientConfig;
import fi.vm.kapa.rova.client.webapi.WebApiClientException;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Client implementation for querying possibilities to operate on behalf of another person.
 */
public class HpaWebApiRiClient extends AbstractWebApiRiClient implements HpaWebApiClient {

    public HpaWebApiRiClient(WebApiClientConfig config, String delegateId) {
        super(config, delegateId);
    }

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected String getRegisterUrl() {
        return "/service/hpa/user/register/" + config.getClientId() + "/" + delegateId;
    }

    @Override
    protected String getUnRegisterUrl(String sessionId) {
        return "/service/hpa/user/unregister/" + sessionId;
    }

    protected String getTransferUrl(String sessionId) {
        return "/service/hpa/user/transfer/token/" + sessionId;
    }

    protected String getRegisterTransferUrl(String transferToken) {
        return "/service/hpa/user/register/transfer/"+transferToken+"/"+config.getClientId()+"/"+delegateId;
    }

    @Override
    public List<Principal> getPrincipals(String requestId) throws WebApiClientException {
        List<Principal> result = null;
        try {
            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
            String pathWithParams = getPathWithParams("/service/hpa/api/delegate/" + getOauthSessionId(), requestId);

            OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(new URL(config.getBaseUrl(), pathWithParams).toString()).setAccessToken(accessToken).buildQueryMessage();
            bearerClientRequest.setHeader("X-AsiointivaltuudetAuthorization", getAuthorizationValue(bearerClientRequest.getLocationUri().substring(config.getBaseUrl().toString().length())));

            OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);

            JavaType resultType = mapper.getTypeFactory().constructParametricType(ArrayList.class, Principal.class);
            return mapper.readValue(resourceResponse.getBody(), resultType);
        } catch (IOException | OAuthSystemException | OAuthProblemException e) {
            handleException(e);
        }
        return result;
    }

    @Override
    public Authorization getAuthorization(String principalId, String requestId, String... issues)
            throws WebApiClientException {
        Authorization result = null;
        try {
            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
            String pathWithParams = getPathWithParams("/service/hpa/api/authorization/" + getOauthSessionId() + "/" + principalId, requestId, issues);

            OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(new URL(config.getBaseUrl(), pathWithParams).toString()).setAccessToken(accessToken).buildQueryMessage();
            bearerClientRequest.setHeader("X-AsiointivaltuudetAuthorization", getAuthorizationValue(bearerClientRequest.getLocationUri().substring(config.getBaseUrl().toString().length())));

            OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);

            result = mapper.readValue(resourceResponse.getBody(), Authorization.class);
        } catch (IOException | OAuthProblemException | OAuthSystemException e) {
            handleException(e);
        }
        return result;
    }

}
