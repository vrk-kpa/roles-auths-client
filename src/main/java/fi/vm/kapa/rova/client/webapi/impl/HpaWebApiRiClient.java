package fi.vm.kapa.rova.client.webapi.impl;

import fi.vm.kapa.rova.client.model.Authorization;
import fi.vm.kapa.rova.client.model.AuthorizationList;
import fi.vm.kapa.rova.client.webapi.WebApiClientConfig;
import fi.vm.kapa.rova.client.webapi.WebApiClientException;

import java.io.IOException;

/**
 * Client implementation for querying possibilities to operate on behalf of another person.
 */
public class HpaWebApiRiClient extends AbstractHpaWebApiRiClient {

    private static final long serialVersionUID = 3673475914669326808L;

    public HpaWebApiRiClient(WebApiClientConfig config, String delegateId) {
        super(config, delegateId);
    }

    @Override
    public Authorization getAuthorization(String principalId, String requestId, String... issues)
            throws WebApiClientException {
        Authorization result = null;
        try {
            String pathWithParams = getPathWithParams("/service/hpa/api/authorization/" + getOauthSessionId()
                    + "/" + principalId, requestId, issues);
            String response = getResultString(config.getBaseUrl(), pathWithParams, accessToken);
            result = mapper.readValue(response, Authorization.class);
        } catch (IOException e) {
            handleException(e);
        }
        return result;
    }

    @Override
    public AuthorizationList getAuthorizationList(String principalId, String requestId)
            throws WebApiClientException {
        AuthorizationList result = null;
        try {
            String pathWithParams = getPathWithParams("/service/hpa/api/authorizationlist/" + getOauthSessionId()
                    + "/" + principalId, requestId);
            String response = getResultString(config.getBaseUrl(), pathWithParams, accessToken);
            result = mapper.readValue(response, AuthorizationList.class);
        } catch (IOException e) {
            handleException(e);
        }
        return result;
    }

}
