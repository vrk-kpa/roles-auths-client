package fi.vm.kapa.rova.client.webapi.impl;

import com.fasterxml.jackson.databind.JavaType;
import fi.vm.kapa.rova.client.model.YpaOrganization;
import fi.vm.kapa.rova.client.webapi.WebApiClientConfig;
import fi.vm.kapa.rova.client.webapi.WebApiClientException;
import fi.vm.kapa.rova.client.webapi.YpaWebApiClient;
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

public class YpaWebApiRiClient extends AbstractYpaWebApiRiClient implements YpaWebApiClient {

    public YpaWebApiRiClient(WebApiClientConfig config, String delegateId) {
        super(config, delegateId);
    }

    @Override
    public List<YpaOrganization> getCompanies(String requestId) throws WebApiClientException {
        return getRolesResponse(getRequestPathWithParams(requestId, null, false));
    }

    @Override
    public List<YpaOrganization> getRoles(String requestId, String organizationId) throws WebApiClientException {
        return getRolesResponse(getRequestPathWithParams(requestId, organizationId, false));
    }


    public List<YpaOrganization> getRolesResponse(String pathWithParams) throws WebApiClientException {
        List<YpaOrganization> result = null;
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        try {
            OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(new URL(config.getBaseUrl(), pathWithParams).toString()).setAccessToken(accessToken).buildQueryMessage();
            bearerClientRequest.setHeader("X-AsiointivaltuudetAuthorization", getAuthorizationValue(bearerClientRequest.getLocationUri().substring(config.getBaseUrl().toString().length())));

            OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);

            JavaType resultType = mapper.getTypeFactory().constructParametricType(ArrayList.class, YpaOrganization.class);
            result = mapper.readValue(resourceResponse.getBody(), resultType);
        } catch (IOException | OAuthProblemException | OAuthSystemException e) {
            handleException(e);
        }
        return result;
    }


}
