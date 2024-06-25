package fi.vm.kapa.rova.client.webapi.impl;

import com.fasterxml.jackson.databind.JavaType;
import fi.vm.kapa.rova.client.model.YpaOrganization;
import fi.vm.kapa.rova.client.webapi.WebApiClientConfig;
import fi.vm.kapa.rova.client.webapi.WebApiClientException;
import fi.vm.kapa.rova.client.webapi.YpaWebApiClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YpaWebApiRiClient extends AbstractYpaWebApiRiClient implements YpaWebApiClient {

    private static final long serialVersionUID = -5749802923118910532L;

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
        try {
            String response = getResultString(config.getBaseUrl(), pathWithParams, accessToken);
            JavaType resultType = mapper.getTypeFactory().constructParametricType(ArrayList.class, YpaOrganization.class);
            result = mapper.readValue(response, resultType);
        } catch (IOException e) {
            handleException(e);
        }
        return result;
    }

}
