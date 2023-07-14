/**
 * The MIT License
 * Copyright (c) 2022 Digital and Population Data Services Agency
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
package fi.vm.kapa.rova.client.rest;

import org.apache.commons.lang3.Validate;

import java.net.URL;

/**
 * Configuration class for REST Api clients.
 */
public class RestClientConfig {

    private final String clientId;
    private final URL baseUrl;
    private final String apiKey;

    @SuppressWarnings("unused")
    private RestClientConfig() {
        clientId = null;
        baseUrl = null;
        apiKey = null;
    }

    /**
     * Constructor for config object.
     *
     * @param clientId      service clientId
     * @param baseUrl       base URL of service's web front
     * @param apiKey        service apiKey
     * @see Builder
     */
    public RestClientConfig(URL baseUrl, String clientId, String apiKey) {
        this.clientId = clientId;
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    public String getClientId() {
        return clientId;
    }
    public URL getBaseUrl() {
        return baseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String clientId;
        private URL baseUrl;
        private String apiKey;

        public RestClientConfig build() {
            Validate.notBlank(clientId, "clientId must be non-blank");
            Validate.notNull(baseUrl, "baseUrl must be non-null");
            Validate.notBlank(apiKey, "apiKey must be non-blank");
            return new RestClientConfig(baseUrl, clientId, apiKey);
        }

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder baseUrl(URL baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }
    }
}
