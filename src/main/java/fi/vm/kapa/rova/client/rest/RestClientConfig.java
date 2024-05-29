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
