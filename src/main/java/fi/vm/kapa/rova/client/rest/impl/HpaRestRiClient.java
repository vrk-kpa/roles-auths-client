package fi.vm.kapa.rova.client.rest.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.vm.kapa.rova.client.model.Authorization;
import fi.vm.kapa.rova.client.rest.HpaRestClient;
import fi.vm.kapa.rova.client.rest.RestClientConfig;
import fi.vm.kapa.rova.client.rest.RestClientException;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class HpaRestRiClient implements HpaRestClient {

    private static final String HMAC_ALGORITHM = "HmacSHA256";
    public static final String HASH_HEADER_NAME = "X-AsiointivaltuudetAuthorization";
    public static final String END_USER_HEADER_NAME = "X-userId";

    private RestClientConfig restClientConfig;

    private ObjectMapper mapper = new ObjectMapper();

    public HpaRestRiClient(RestClientConfig restClientConfig) {
        this.restClientConfig = restClientConfig;
    }

    @Override
    public Authorization getAuthorization(String endUser, String delegateId, String principalId, String requestId, String... issue)
            throws RestClientException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        String url = restClientConfig.getBaseUrl() + "/service/rest/hpa/authorization/"+restClientConfig.getClientId()
                + "/" + delegateId + "/" + principalId;
        StringBuilder sb = new StringBuilder(url);
        sb.append("?");

        if (issue != null) {
            for (String s : issue) {
                sb.append("issue=");
                sb.append(s);
                sb.append("&");
            }
        }
        sb.append("requestId=").append(requestId);
        url = sb.toString();

        HttpGet httpGet = new HttpGet(url);

        HttpClientResponseHandler<Authorization> handler = response -> {
            HttpEntity entity = response.getEntity();
            return entity != null ? mapper.readValue(entity.getContent(), Authorization.class) : null;
        };

        /* SEND AND RETRIEVE RESPONSE */
        try {
            appendValidationHeaders(httpGet, endUser);
            return httpClient.execute(httpGet, null, handler);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return null;
        }

    }

    private void appendValidationHeaders(HttpGet httpGet, String endUserId) throws IOException, URISyntaxException {
        httpGet.addHeader(HASH_HEADER_NAME, getAuthorizationValue(httpGet.getUri().getPath() + "?" + httpGet.getUri().getRawQuery()));
        httpGet.addHeader(END_USER_HEADER_NAME, endUserId);
    }

    protected String getAuthorizationValue(String path) throws IOException {
        String timestamp = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME);
        return restClientConfig.getClientId() + " " + timestamp + " " + hash(path + " " + timestamp, restClientConfig.getApiKey());
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

}
