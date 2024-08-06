package fi.vm.kapa.rova.client.webapi.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.vm.kapa.rova.client.model.Principal;
import fi.vm.kapa.rova.client.webapi.HpaWebApiClient;
import fi.vm.kapa.rova.client.webapi.WebApiClientConfig;
import fi.vm.kapa.rova.client.webapi.WebApiClientException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractHpaWebApiRiClient extends AbstractWebApiRiClient implements HpaWebApiClient {

    private static final long serialVersionUID = -5327858827255003945L;

    protected ObjectMapper mapper = new ObjectMapper();

    protected AbstractHpaWebApiRiClient(WebApiClientConfig config, String delegateId) {
        super(config, delegateId);
    }

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
        return "/service/hpa/user/register/transfer/" + transferToken + "/" + config.getClientId() + "/" + delegateId;
    }

    @Override
    public List<Principal> getPrincipals(String requestId) throws WebApiClientException {
        try {
            String pathWithParams = getPathWithParams("/service/hpa/api/delegate/" + getOauthSessionId(), requestId);
            String result = getResultString(config.getBaseUrl(), pathWithParams, accessToken);
            JavaType resultType = mapper.getTypeFactory().constructParametricType(ArrayList.class, Principal.class);
            return mapper.readValue(result, resultType);
        } catch (IOException e) {
            handleException(e);
        }
        return null;
    }

}
