package fi.vm.kapa.rova.client.webapi.impl;

import com.fasterxml.jackson.databind.JavaType;
import fi.vm.kapa.rova.client.model.YpaOrganization;
import fi.vm.kapa.rova.client.webapi.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YpaWebApiJwtRiClient extends AbstractYpaWebApiRiClient implements YpaWebApiJwtClient {

    private static final long serialVersionUID = 8988149210867218601L;

    JwtUtil jwtUtil;

    public YpaWebApiJwtRiClient(WebApiJwtClientConfig config, String delegateId) {
        super(config, delegateId);
        this.jwtUtil = new JwtUtil(config);
    }

    @Override
    public List<YpaOrganization> getCompanies(String requestId) throws WebApiClientException {
        String tokenResponse = getRolesTokenResponse(getRequestPathWithParams(requestId, null, true));
        return jwtUtil.getCompanies(tokenResponse, delegateId);
    }

    @Override
    public List<YpaOrganization> getRoles(String requestId, String organizationId) throws WebApiClientException {
        String tokenResponse = getRolesTokenResponse(getRequestPathWithParams(requestId, organizationId, true));
        return jwtUtil.getCompanies(tokenResponse, delegateId);
    }

    public String getCompaniesToken(String requestId) throws WebApiClientException {
        return getRolesTokenResponse(getRequestPathWithParams(requestId, null, true));
    }

    public String getRolesToken(String requestId, String organizationId) throws WebApiClientException {
        return getRolesTokenResponse(getRequestPathWithParams(requestId, organizationId, true));
    }

    public String getCompaniesSessionToken(String requestId) throws WebApiClientException {
        return getRolesTokenResponse(getSessionTokenPathWithParams(requestId));
    }

    @Override
    public List<YpaOrganization> getSessionCompanies(String jwtString, String requestId) throws WebApiClientException {
        return getRolesResponse(getSessionCompaniesPathWithParams(jwtString, requestId));
    }

    public String getRolesTokenResponse(String pathWithParams) throws WebApiClientException {
        try {
            return getResultString(config.getBaseUrl(), pathWithParams, accessToken);
        } catch (IOException e) {
            handleException(e);
        }
        // should not get here
        return null;
    }

    private List<YpaOrganization> getRolesResponse(String pathWithParams) throws WebApiClientException {
        List<YpaOrganization> result = null;
        try {
            String responseString = getRolesTokenResponse(pathWithParams);
            JavaType resultType = mapper.getTypeFactory().constructParametricType(ArrayList.class, YpaOrganization.class);
            result = mapper.readValue(responseString, resultType);
        } catch (IOException e) {
            handleException(e);
        }
        return result;
    }

    protected String getSessionCompaniesPathWithParams(String jwtString, String requestId) {
        String base = "/service/ypa/jwt/api/organizationRoles/";
        return getPathWithParams(base + jwtString + "/", requestId);
    }

    protected String getSessionTokenPathWithParams(String requestId) {
        String base = "/service/ypa/api/organizationRoles/session/jwt/";
        return getPathWithParams(base + getOauthSessionId(), requestId);
    }
}
