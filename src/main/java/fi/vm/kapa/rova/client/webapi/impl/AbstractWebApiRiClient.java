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
import fi.vm.kapa.rova.client.webapi.WebApiClientConfig;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
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
import java.util.Base64;

public abstract class AbstractWebApiRiClient {

    protected final static String HMAC_ALGORITHM = "HmacSHA256";
    protected final WebApiClientConfig config;
    protected String delegateId;

    private RegisterToken registerToken;

    protected String accessToken;

    private static class RegisterToken {
        String sessionId;
        String userId;
        void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }
        void setUserId(String userId) {
            this.userId = userId;
        }
    }

    public AbstractWebApiRiClient(WebApiClientConfig config, String delegateId) {
        this.config = config;
        this.delegateId = delegateId;
    }

    protected abstract String getRegisterUrl();

    protected abstract String getUnRegisterUrl(String sessionId);

    public void getToken(String code, String urlParams) throws OAuthProblemException, OAuthSystemException {

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

        this.accessToken = oAuthResponse.getAccessToken();
    }

    public String register(String requestId, String urlParams) throws IOException {
        String pathWithParams = getPathWithParams(getRegisterUrl(), requestId);
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
        this.registerToken = mapper.readValue(tokenStr, RegisterToken.class);

        if (urlParams == null) {
            urlParams = "";
        }

        return config.getAuthorizeUrl() + "?client_id=" + config.getClientId()
                + "&redirect_uri=" + config.getOauthRedirect() + urlParams
                + "&response_type=code"
                + "&requestId=" + requestId
                + "&user=" + this.registerToken.userId;
    }

    public Boolean unregister() throws IOException {
        String sessionId = this.registerToken.sessionId;
        if (sessionId != null) {
            String path = getUnRegisterUrl(sessionId);
            URL url = new URL(config.getBaseUrl(), path);
            HttpURLConnection yc = (HttpURLConnection) url.openConnection();
            yc.setRequestProperty("X-AsiointivaltuudetAuthorization", getAuthorizationValue(path));
            String resultString = null;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()))) {
                String s;
                while ((s = in.readLine()) != null) {
                    resultString = s;
                }
            }
            return (resultString != null && "true".equalsIgnoreCase(resultString));
        } else {
            return false;
        }
    }

    protected String getOauthSessionId() {
        return registerToken.sessionId;
    }

    protected String getAuthorizationValue(String path) throws IOException {
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

    protected String getPathWithParams(String path, String requestId, String... issues) {
        StringBuilder pathBuilder = new StringBuilder(path)
                .append("?requestId=")
                .append(requestId);

        for (String issue : issues) {
            if (issue != null) {
                pathBuilder.append("&issues=").append(issue);
            }
        }

        return pathBuilder.toString();
    }
}
