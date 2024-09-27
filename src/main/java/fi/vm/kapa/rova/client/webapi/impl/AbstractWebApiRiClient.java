package fi.vm.kapa.rova.client.webapi.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.*;
import com.nimbusds.oauth2.sdk.auth.ClientAuthentication;
import com.nimbusds.oauth2.sdk.auth.ClientSecretBasic;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import fi.vm.kapa.rova.client.webapi.WebApiClientConfig;
import fi.vm.kapa.rova.client.webapi.WebApiClientException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.UUID;

import static fi.vm.kapa.rova.client.webapi.WebApiClient.ASIOINTIVALTUUDET_AUTHORIZATION_HEADER;
import static fi.vm.kapa.rova.client.webapi.WebApiClient.AUTHORIZATION_HEADER;

public abstract class AbstractWebApiRiClient {

    protected static final String HMAC_ALGORITHM = "HmacSHA256";
    protected final WebApiClientConfig config;
    protected final String delegateId;

    private RegisterToken registerToken;

    protected AccessToken accessToken;

    private final String stateParameter = UUID.randomUUID().toString();

    private static class RegisterToken {
        String sessionId;
        String userId;

        @SuppressWarnings("unused")
        void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        @SuppressWarnings("unused")
        void setUserId(String userId) {
            this.userId = userId;
        }
    }

    protected AbstractWebApiRiClient(WebApiClientConfig config, String delegateId) {
        this.config = config;
        this.delegateId = delegateId;
    }

    protected abstract String getRegisterUrl();

    protected abstract String getUnRegisterUrl(String sessionId);

    protected abstract String getTransferUrl(String sessionId);

    protected abstract String getRegisterTransferUrl(String transferToken);

    public void getToken(String codeParam, String stateParam) throws WebApiClientException {
        if (!stateParameter.equals(stateParam)) {
            throw new WebApiClientException("Mismatching OAuth state parameter. Expected state=" + stateParameter);
        }

        try {
            AuthorizationCode code = new AuthorizationCode(codeParam);
            URI callback = new URI(config.getoAuthRedirect());
            AuthorizationGrant codeGrant = new AuthorizationCodeGrant(code, callback);
            ClientID clientID = new ClientID(config.getClientId());
            Secret clientSecret = new Secret(config.getoAuthSecret());
            ClientAuthentication clientAuth = new ClientSecretBasic(clientID, clientSecret);
            URI tokenEndpoint = new URI(config.getTokenUrl());
            TokenRequest request = new TokenRequest(tokenEndpoint, clientAuth, codeGrant, null);
            TokenResponse response = TokenResponse.parse(request.toHTTPRequest().send());
            if (!response.indicatesSuccess()) {
                TokenErrorResponse errorResponse = response.toErrorResponse();
                throw new IOException(errorResponse.getErrorObject().toString());
            }
            AccessTokenResponse successResponse = response.toSuccessResponse();
            accessToken = successResponse.getTokens().getAccessToken();
        } catch (URISyntaxException | ParseException | IOException e) {
            handleException("Unable to get token", e);
        }
    }

    public String getTransferToken() throws WebApiClientException {
        try {
            String sessionId = this.registerToken.sessionId;
            if (sessionId == null) {
                throw new WebApiClientException("No active session found for transfer.");
            }
            return getResultString(getTransferUrl(sessionId));
        } catch (IOException e) {
            handleException("Transfer Token fetching failed", e);
        }
        return null;
    }

    public String registerTransfer(String transferToken, String requestId, String userInterfaceLanguage) throws WebApiClientException {
        return actuallyRegister(getRegisterTransferUrl(transferToken), requestId, userInterfaceLanguage);
    }

    public String register(String requestId, String userInterfaceLanguage) throws WebApiClientException {
        return actuallyRegister(getRegisterUrl(), requestId, userInterfaceLanguage);
    }

    private String actuallyRegister(String path, String requestId, String userInterfaceLanguage) throws WebApiClientException {
        try {
            String tokenStr = getResultString(getPathWithParams(path, requestId));
            ObjectMapper mapper = new ObjectMapper();
            this.registerToken = mapper.readValue(tokenStr, RegisterToken.class);

            return config.getAuthorizeUrl() //
                    + "?client_id=" + config.getClientId() //
                    + "&response_type=code"//
                    + "&requestId=" + requestId//
                    + "&user=" + this.registerToken.userId //
                    + "&scope=read"
                    + "&state=" + stateParameter //
                    + "&redirect_uri=" + config.getoAuthRedirect() //
                    + (userInterfaceLanguage != null ? "&lang=" + userInterfaceLanguage : "");
        } catch (IOException e) {
            handleException(e);
        }
        // should not get here
        return null;
    }

    public Boolean unregister() throws WebApiClientException {
        try {
            String sessionId = this.registerToken.sessionId;
            if (sessionId == null) {
                return false;
            }
            String resultString = getResultString(getUnRegisterUrl(sessionId));
            return ("true".equalsIgnoreCase(resultString));
        } catch (IOException e) {
            handleException("Unregistration failed", e);
        }
        return false;
    }

    protected String getOauthSessionId() {
        return registerToken.sessionId;
    }

    protected String getAuthorizationValue(String path) throws IOException {
        String timestamp = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME);
        return config.getClientId() + " " + timestamp + " " + hash(path + " " + timestamp, config.getApiKey());
    }

    @SuppressWarnings("Duplicates")
    private String hash(String data, String key) throws IOException {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_ALGORITHM);
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            return new String(Base64.getEncoder().encode(rawHmac));
        } catch (NoSuchAlgorithmException | InvalidKeyException | IllegalStateException e) {
            throw new IOException("Cannot create hash", e);
        }
    }

    protected String getPathWithParams(String path, String requestId, String... issues) {
        StringBuilder pathBuilder = new StringBuilder(path).append("?requestId=").append(requestId);
        for (String issue : issues) {
            if (issue != null) {
                pathBuilder.append("&issues=").append(issue);
            }
        }
        return pathBuilder.toString();
    }

    protected void handleException(Throwable t) throws WebApiClientException {
        throw new WebApiClientException("Got exception client now in inactive state", t);
    }

    protected void handleException(String msg, Throwable t) throws WebApiClientException {
        throw new WebApiClientException(msg, t);
    }

    protected String readResultString(InputStream inputStream) throws IOException {
        StringBuilder resultString = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = in.readLine()) != null) {
                resultString.append(line);
            }
        }
        return resultString.toString();
    }

    protected String getResultString(URL baseUrl, String path, AccessToken accessToken) throws IOException {
        URLConnection conn = new URL(baseUrl + path).openConnection();
        conn.setRequestProperty(AUTHORIZATION_HEADER, accessToken.toAuthorizationHeader());
        conn.setRequestProperty(ASIOINTIVALTUUDET_AUTHORIZATION_HEADER, getAuthorizationValue(path));
        try (InputStream is = conn.getInputStream()) {
            return readResultString(is);
        }
    }

    private String getResultString(String path) throws IOException {
        URL url = new URL(config.getBaseUrl(), path);
        HttpURLConnection yc = (HttpURLConnection) url.openConnection();
        yc.setRequestProperty(ASIOINTIVALTUUDET_AUTHORIZATION_HEADER, getAuthorizationValue(path));
        try (InputStream is = yc.getInputStream()) {
            return readResultString(is);
        }
    }

    public String getDelegateId() {
        return this.delegateId;
    }
}
