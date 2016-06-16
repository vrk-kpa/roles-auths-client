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
package fi.vm.kapa.rova.client.webapi;

import org.apache.commons.lang3.Validate;

import java.net.URL;

/**
 * Configuration class for REST Api clients.
 */
public class WebApiClientConfig {

    private final String clientId;
    private final URL baseUrl;
    private final String authorizeUrl;
    private final String tokenUrl;
    private final String apiKey;
    private final String oAuthSecret;
    private final String oAuthRedirect;

    @SuppressWarnings("unused")
    private WebApiClientConfig() {
        this(null, null, null, null, null, null, null);
    }

    /**
     * Constructor for config object.
     *
     * @see Builder
     * @param clientId service clientId
     * @param baseUrl base URL of service's web front
     * @param authorizeUrl URL for OAuth, (rova-web-api/oauth/authorize)
     * @param tokenUrl URL for fetching OAuth access token (rova-web-api/oauth/token)
     * @param apiKey service apiKey
     * @param oAuthSecret service oAuthSecret
     * @param oAuthRedirect URL to redirect to after principal selection
     */
    public WebApiClientConfig(String clientId, URL baseUrl, String authorizeUrl, String tokenUrl,
                              String apiKey, String oAuthSecret, String oAuthRedirect) {
        this.clientId = clientId;
        this.baseUrl = baseUrl;
        this.authorizeUrl = authorizeUrl;
        this.tokenUrl = tokenUrl;
        this.apiKey = apiKey;
        this.oAuthSecret = oAuthSecret;
        this.oAuthRedirect = oAuthRedirect;
    }

    public String getClientId() {
        return clientId;
    }

    public URL getBaseUrl() {
        return baseUrl;
    }

    public String getAuthorizeUrl() {
        return authorizeUrl;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getoAuthSecret() {
        return oAuthSecret;
    }

    public String getoAuthRedirect() {
        return oAuthRedirect;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String clientId;
        private URL baseUrl;
        private String authorizeUrl;
        private String tokenUrl;
        private String apiKey;
        private String oAuthSecret;
        private String oAuthRedirect;

        public WebApiClientConfig build() {
            Validate.notBlank(clientId, "clientId must be non-blank");
            Validate.notNull(baseUrl, "baseUrl must be non-null");
            Validate.notBlank(authorizeUrl, "authorizeUrl must be non-blank");
            Validate.notBlank(tokenUrl, "clientId must be non-blank");
            Validate.notBlank(apiKey, "apiKey must be non-blank");
            Validate.notBlank(oAuthSecret, "oAuthSecret must be non-blank");
            Validate.notBlank(oAuthRedirect, "oAuthRedirect must be non-blank");
            return new WebApiClientConfig(clientId, baseUrl, authorizeUrl, tokenUrl, apiKey, oAuthSecret, oAuthRedirect);
        }
        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }
        public Builder baseUrl(URL baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }
        public Builder authorizeUrl(String authorizeUrl) {
            this.authorizeUrl = authorizeUrl;
            return this;
        }
        public Builder tokenUrl(String tokenUrl) {
            this.tokenUrl = tokenUrl;
            return this;
        }
        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }
        public Builder oAuthSecret(String oAuthSecret) {
            this.oAuthSecret = oAuthSecret;
            return this;
        }
        public Builder oAuthRedirect(String oAuthRedirect) {
            this.oAuthRedirect = oAuthRedirect;
            return this;
        }
    }
}
