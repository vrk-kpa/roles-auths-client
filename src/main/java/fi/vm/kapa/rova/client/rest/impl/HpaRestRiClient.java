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
package fi.vm.kapa.rova.client.rest.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.vm.kapa.rova.client.model.Authorization;
import fi.vm.kapa.rova.client.rest.HpaRestClient;
import fi.vm.kapa.rova.client.rest.RestClientConfig;
import fi.vm.kapa.rova.client.rest.RestClientException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.io.IOException;
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
    public static final String END_USER_ID = "RiClientUser";

    private RestClientConfig restClientConfig;

    private ObjectMapper mapper = new ObjectMapper();

    public HpaRestRiClient(RestClientConfig restClientConfig) {
        this.restClientConfig = restClientConfig;
    }

    @Override
    public Authorization getAuthorization(String delegateId, String principalId, String requestId, String... issue)
            throws RestClientException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        String url = restClientConfig.getBaseUrl() + "service/rest/hpa/authorization/"+restClientConfig.getClientId()+"/" + delegateId + "/" + principalId;
        StringBuilder sb = new StringBuilder(url);
        sb.append("?");

        if (issue != null && issue.length > 0) {
            for (int i = 0; i < issue.length; i++) {
                sb.append("issues=");
                sb.append(issue[i]);
                sb.append("&");

            }
        }
        sb.append("requestId="+requestId);
        url = sb.toString();

        HttpGet httpGet = new HttpGet(url);

        ResponseHandler<Authorization> handler = new ResponseHandler<Authorization>() {
            @Override
            public Authorization handleResponse(final HttpResponse response)
                    throws ClientProtocolException, IOException {
                int status = response.getStatusLine().getStatusCode();
                System.out.println("status: "+ status);
                HttpEntity entity = response.getEntity();
                return entity != null ? mapper.readValue(entity.getContent(), Authorization.class) : null;
            }
        };

        /* SEND AND RETRIEVE RESPONSE */
        try {
            appendValidationHeaders(httpGet);
            for (Header h : httpGet.getAllHeaders()) {
                System.out.println("header "+ h.getName() +" has value "+ h.getValue());
            }
            return httpClient.execute(httpGet, handler);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    private void appendValidationHeaders(HttpGet httpGet) throws IOException {
        httpGet.addHeader(HASH_HEADER_NAME, getAuthorizationValue(httpGet.getURI().getPath() + "?" + httpGet.getURI().getRawQuery()));
        httpGet.addHeader(END_USER_HEADER_NAME, END_USER_ID);
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
            String result = new String(Base64.getEncoder().encode(rawHmac));
            return result;
        } catch (NoSuchAlgorithmException | InvalidKeyException | IllegalStateException e) {
            throw new IOException("Cannot create hash", e);
        }
    }

}
