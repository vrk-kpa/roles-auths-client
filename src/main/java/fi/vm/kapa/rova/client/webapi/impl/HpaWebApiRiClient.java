package fi.vm.kapa.rova.client.webapi.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.vm.kapa.rova.client.model.Authorization;
import fi.vm.kapa.rova.client.model.AuthorizationList;
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

/**
 * Client implementation for querying possibilities to operate on behalf of another person.
 */
public class HpaWebApiRiClient extends AbstractHpaWebApiRiClient {

    public HpaWebApiRiClient(WebApiClientConfig config, String delegateId) {
        super(config, delegateId);
    }

    private ObjectMapper mapper = new ObjectMapper();

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

    @Override
    public AuthorizationList getAuthorizationList(String principalId, String requestId)
            throws WebApiClientException {
        AuthorizationList result = null;
        try {
            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
            String pathWithParams = getPathWithParams("/service/hpa/api/authorizationlist/" + getOauthSessionId() + "/" + principalId, requestId);

            OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(new URL(config.getBaseUrl(), pathWithParams).toString()).setAccessToken(accessToken).buildQueryMessage();
            bearerClientRequest.setHeader("X-AsiointivaltuudetAuthorization", getAuthorizationValue(bearerClientRequest.getLocationUri().substring(config.getBaseUrl().toString().length())));

            OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);

            result = mapper.readValue(resourceResponse.getBody(), AuthorizationList.class);
        } catch (IOException | OAuthProblemException | OAuthSystemException e) {
            handleException(e);
        }
        return result;
    }

}
