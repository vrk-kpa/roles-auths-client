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
