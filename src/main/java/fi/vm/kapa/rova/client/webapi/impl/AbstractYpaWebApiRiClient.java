package fi.vm.kapa.rova.client.webapi.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.vm.kapa.rova.client.webapi.WebApiClientConfig;
import fi.vm.kapa.rova.client.webapi.YpaWebApiClient;

public abstract class AbstractYpaWebApiRiClient extends AbstractWebApiRiClient implements YpaWebApiClient {

    private static final long serialVersionUID = -7951396876583941944L;

    protected ObjectMapper mapper = new ObjectMapper();

    protected AbstractYpaWebApiRiClient(WebApiClientConfig config, String delegateId) {
        super(config, delegateId);
    }

    @Override
    protected String getRegisterUrl() {
        return "/service/ypa/user/register/" + config.getClientId() + "/" + delegateId;
    }

    @Override
    protected String getUnRegisterUrl(String sessionId) {
        return "/service/ypa/user/unregister/" + sessionId;
    }

    protected String getTransferUrl(String sessionId) {
        return "/service/ypa/user/transfer/token/" + sessionId;
    }

    protected String getRegisterTransferUrl(String transferToken) {
        return "/service/ypa/user/register/transfer/" + transferToken + "/" + config.getClientId() + "/" + delegateId;
    }

    protected String getRequestPathWithParams(String requestId, String organizationId, boolean jwt) {
        String base = "/service/ypa/api/organizationRoles/";
        if (jwt) {
            base = base + "jwt/";
        }
        if (organizationId != null) {
            return getPathWithParams(base + getOauthSessionId() + "/" + organizationId, requestId);
        } else {
            return getPathWithParams(base + getOauthSessionId(), requestId);
        }
    }

}
