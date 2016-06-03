/**
 * The MIT License
 * Copyright (c) 2016 Population Register Centre
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fi.vm.kapa.rova.client.webapi.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.vm.kapa.rova.client.common.Token;
import fi.vm.kapa.rova.client.model.Authorization;
import fi.vm.kapa.rova.client.model.Principal;
import fi.vm.kapa.rova.client.webapi.HpaWebApiClient;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class HpaWebApiRiClient implements HpaWebApiClient {

    private final static String HMAC_ALGORITHM = "HmacSHA256";
    private final WebApiClientConfig config;
    private String delegateId;

    private Token token;
    private String accessToken;

    public HpaWebApiRiClient(WebApiClientConfig config, String delegateId) {
        this.config = config;
        this.delegateId = delegateId;
    }

    @Override
    public void register(String requestId) throws IOException {
        String pathWithParams = getPathWithParams("/service/hpa/user/register/" + config.getClientId() + "/" + delegateId);
        URL url = new URL(config.getBaseUrl(), pathWithParams);
        HttpURLConnection yc = (HttpURLConnection)url.openConnection();
        yc.setRequestProperty("X-AsiointivaltuudetAuthorization", getAuthorizationValue(pathWithParams));

        String tokenStr = null;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()))) {
            String s;
            while ((s = in.readLine()) != null) {
                tokenStr = s;
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        this.token = mapper.readValue(tokenStr, Token.class);
    }

    @Override
    public String getOauthSessionId() {
        return token.getSessionId();
    }

    @Override
    public String getOauthUserId() {
        return token.getUserId();
    }

    @Override
    public String getToken(String code, String urlParams) throws OAuthProblemException, OAuthSystemException {

        OAuthClientRequest.TokenRequestBuilder requestBuilder = OAuthClientRequest.tokenLocation(config.getTokenUrl())
                .setGrantType(GrantType.AUTHORIZATION_CODE)
                .setClientId(config.getClientId())
                .setClientSecret(config.getoAuthSecret())
                .setCode(code);

        if (urlParams == null) {
            urlParams = "";
        }

        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        OAuthJSONAccessTokenResponse oAuthResponse = oAuthClient.accessToken(
                requestBuilder.setRedirectURI(config.getOauthRedirect() + urlParams).buildBodyMessage(),
                OAuthJSONAccessTokenResponse.class);

        String token = oAuthResponse.getAccessToken();
        this.accessToken = token;
        return token;
    }

    @Override
    public List<Principal> getPrincipals() throws OAuthProblemException, OAuthSystemException, IOException {
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        String pathWithParams = getPathWithParams("/service/hpa/api/delegate/" + getOauthSessionId());
        OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(new URL(config.getBaseUrl(), pathWithParams).toString())
                .setAccessToken(accessToken).buildQueryMessage();
        bearerClientRequest.setHeader("X-AsiointivaltuudetAuthorization",
                getAuthorizationValue(bearerClientRequest.getLocationUri().substring(config.getBaseUrl().toString().length())));

        OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);

        ObjectMapper mapper = new ObjectMapper();
        com.fasterxml.jackson.databind.JavaType resultType = mapper.getTypeFactory().constructParametricType(ArrayList.class, Principal.class);
        return mapper.readValue(resourceResponse.getBody(), resultType);
    }

    @Override
    public Authorization getAuthorization(String principalId, String requestId, String... issues) throws IOException, OAuthProblemException, OAuthSystemException {
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        String pathWithParams = getPathWithParams("/service/hpa/api/authorization/" + getOauthSessionId() + "/" + principalId,
                requestId, issues);

        OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(new URL(config.getBaseUrl(), pathWithParams).toString())
                .setAccessToken(accessToken).buildQueryMessage();
        bearerClientRequest.setHeader("X-AsiointivaltuudetAuthorization",
                getAuthorizationValue(bearerClientRequest.getLocationUri().substring(config.getBaseUrl().toString().length())));

        OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);

        ObjectMapper mapper = new ObjectMapper();

        Authorization auth = mapper.readValue(resourceResponse.getBody(), Authorization.class);
        return auth;
    }

    private String getAuthorizationValue(String path) throws IOException {
        String timestamp = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME);
        return config.getClientId() + " " + timestamp + " " + hash(path + " " + timestamp, config.getApiKey());
    }

    private String hash(String data, String key) throws IOException {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_ALGORITHM);
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            String result = new String(Base64.getEncoder().encode(rawHmac));
            return result;
        } catch (NoSuchAlgorithmException | InvalidKeyException | IllegalStateException e) {
            throw new IOException("Cannot create hash", e);
        }
    }

    private String getPathWithParams(String path) {
        return getPathWithParams(path, null);
    }

    private String getPathWithParams(String path, String requestId, String... issues) {
        StringBuilder pathBuilder = new StringBuilder(path)
                .append("?requestId=")
                .append(requestId);

        for (String issue : issues) {
            pathBuilder.append("&issues=")
                    .append(issue);
        }

        return pathBuilder.toString();
    }
}
