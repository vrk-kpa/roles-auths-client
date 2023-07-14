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
package fi.vm.kapa.rova.client.webapi.impl;

import fi.vm.kapa.rova.client.model.Authorization;
import fi.vm.kapa.rova.client.model.AuthorizationList;
import fi.vm.kapa.rova.client.webapi.HpaWebApiJwtClient;
import fi.vm.kapa.rova.client.webapi.JwtUtil;
import fi.vm.kapa.rova.client.webapi.WebApiClientException;
import fi.vm.kapa.rova.client.webapi.WebApiJwtClientConfig;
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

/**
 * Client implementation for querying possibilities to operate on behalf of another person.
 */
public class HpaWebApiJwtRiClient extends AbstractHpaWebApiRiClient implements HpaWebApiJwtClient {

    private WebApiJwtClientConfig jwtConfig;
    private JwtUtil jwtUtil;

    public HpaWebApiJwtRiClient(WebApiJwtClientConfig config, String delegateId) {
        super(config, delegateId);
        this.jwtConfig = config;
        this.jwtUtil = new JwtUtil(jwtConfig);
    }

    @Override
    public Authorization getAuthorization(String principalId, String requestId, String... issues) throws WebApiClientException {
        String jwtString = getAuthorizationToken(principalId, requestId, issues);
        return jwtUtil.getAuthorization(jwtString, principalId);
    }

    @Override
    public AuthorizationList getAuthorizationList(String principalId, String requestId)
            throws WebApiClientException {
        String jwtString = getAuthorizationListToken(principalId, requestId);
        return jwtUtil.getAuthorizationList(jwtString, principalId);
    }

    @Override
    public String getAuthorizationToken(String principalId, String requestId, String... issues) throws WebApiClientException {
        try {
            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
            String pathWithParams = getPathWithParams("/service/hpa/api/authorization/jwt/" + getOauthSessionId() + "/" + principalId, requestId, issues);

            OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(new URL(config.getBaseUrl(), pathWithParams).toString()).setAccessToken(accessToken).buildQueryMessage();
            bearerClientRequest.setHeader("X-AsiointivaltuudetAuthorization", getAuthorizationValue(bearerClientRequest.getLocationUri().substring(config.getBaseUrl().toString().length())));

            OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);
            return resourceResponse.getBody();
        } catch (IOException | OAuthProblemException | OAuthSystemException e) {
            handleException(e);
        }
        handleException(new WebApiClientException("Could not get Authorization token"));
        // should not get here
        return null;
    }

    @Override
    public String getAuthorizationListToken(String principalId, String requestId) throws WebApiClientException {
        try {
            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
            String pathWithParams = getPathWithParams("/service/hpa/api/authorizationlist/jwt/" + getOauthSessionId() + "/" + principalId, requestId);

            OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(new URL(config.getBaseUrl(), pathWithParams).toString()).setAccessToken(accessToken).buildQueryMessage();
            bearerClientRequest.setHeader("X-AsiointivaltuudetAuthorization", getAuthorizationValue(bearerClientRequest.getLocationUri().substring(config.getBaseUrl().toString().length())));

            OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);

            return resourceResponse.getBody();
        } catch (IOException | OAuthProblemException | OAuthSystemException e) {
            handleException(e);
        }
        handleException(new WebApiClientException("Could not get AuthorizationList token"));
        // should not get here
        return null;
    }


}
