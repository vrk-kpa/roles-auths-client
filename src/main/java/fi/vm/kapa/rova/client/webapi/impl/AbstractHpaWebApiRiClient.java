package fi.vm.kapa.rova.client.webapi.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.vm.kapa.rova.client.model.Principal;
import fi.vm.kapa.rova.client.webapi.HpaWebApiClient;
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
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractHpaWebApiRiClient extends AbstractWebApiRiClient implements HpaWebApiClient {
    protected ObjectMapper mapper = new ObjectMapper();

    public AbstractHpaWebApiRiClient(WebApiClientConfig config, String delegateId) {
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
//    	try {
    		//String encodedTransferToken = URLEncoder.encode(transferToken, StandardCharsets.ISO_8859_1.toString());
    		return "/service/hpa/user/register/transfer/" + transferToken + "/" + config.getClientId() + "/" + delegateId;
//    	} catch(UnsupportedEncodingException e) {
//    	    throw new AssertionError(e);
//    	}
    }

    @Override
    public List<Principal> getPrincipals(String requestId) throws WebApiClientException {
        List<Principal> result = null;
        try {
            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
            String pathWithParams = getPathWithParams("/service/hpa/api/delegate/" + getOauthSessionId(), requestId);

            OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(new URL(config.getBaseUrl(), pathWithParams).toString()).setAccessToken(accessToken).buildQueryMessage();
            bearerClientRequest.setHeader("X-AsiointivaltuudetAuthorization", getAuthorizationValue(bearerClientRequest.getLocationUri().substring(config.getBaseUrl().toString().length())));

            OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);

            JavaType resultType = mapper.getTypeFactory().constructParametricType(ArrayList.class, Principal.class);
            return mapper.readValue(resourceResponse.getBody(), resultType);
        } catch (IOException | OAuthSystemException | OAuthProblemException e) {
            handleException(e);
        }
        return result;
    }

}
