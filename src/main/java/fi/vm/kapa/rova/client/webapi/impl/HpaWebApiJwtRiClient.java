package fi.vm.kapa.rova.client.webapi.impl;

import fi.vm.kapa.rova.client.model.Authorization;
import fi.vm.kapa.rova.client.model.AuthorizationList;
import fi.vm.kapa.rova.client.webapi.HpaWebApiJwtClient;
import fi.vm.kapa.rova.client.webapi.JwtUtil;
import fi.vm.kapa.rova.client.webapi.WebApiClientException;
import fi.vm.kapa.rova.client.webapi.WebApiJwtClientConfig;

import java.io.IOException;

/**
 * Client implementation for querying possibilities to operate on behalf of another person.
 */
public class HpaWebApiJwtRiClient extends AbstractHpaWebApiRiClient implements HpaWebApiJwtClient {

    private static final long serialVersionUID = -5114190316817667445L;

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
            String pathWithParams = getPathWithParams("/service/hpa/api/authorization/jwt/" + getOauthSessionId()
                    + "/" + principalId, requestId, issues);
            return getResultString(config.getBaseUrl(), pathWithParams, accessToken);
        } catch (IOException e) {
            handleException(e);
        }
        handleException(new WebApiClientException("Could not get Authorization token"));
        // should not get here
        return null;
    }

    @Override
    public String getAuthorizationListToken(String principalId, String requestId) throws WebApiClientException {
        try {
            String pathWithParams = getPathWithParams("/service/hpa/api/authorizationlist/jwt/"
                    + getOauthSessionId() + "/" + principalId, requestId);
            return getResultString(config.getBaseUrl(), pathWithParams, accessToken);
        } catch (IOException e) {
            handleException(e);
        }
        handleException(new WebApiClientException("Could not get AuthorizationList token"));
        // should not get here
        return null;
    }

}
