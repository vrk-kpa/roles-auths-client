package fi.vm.kapa.rova.client.webapi;

import org.apache.commons.lang3.Validate;

import java.net.URL;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class WebApiJwtClientConfig extends WebApiClientConfig {

    private PublicKey webApiRSAPublicKey;

    private String serviceUUID;

    /**
     * Non oauth configuration for jwt validation with JWTUtil
     * @param serviceUUID
     * @param publicKey
     */
    public WebApiJwtClientConfig(String serviceUUID, String publicKey) {
        this(null, null, null, null, null,
                null, null, serviceUUID, publicKey);
    }


    public WebApiJwtClientConfig(String clientId, URL baseUrl, String authorizeUrl, String tokenUrl, String apiKey,
                                 String oAuthSecret, String oAuthRedirect, String serviceUUID, String publicKey) {

        super(clientId, baseUrl, authorizeUrl, tokenUrl, apiKey, oAuthSecret, oAuthRedirect);

        this.serviceUUID = serviceUUID;
        byte[] publicBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            webApiRSAPublicKey = keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public RSAPublicKey getRSAPublicKey() {
        return (RSAPublicKey) webApiRSAPublicKey;
    }

    public String getServiceUUID() {
        return serviceUUID;
    }

    public static WebApiJwtClientConfig.Builder jwtBuilder() {
        return new WebApiJwtClientConfig.Builder();
    }

    public static class Builder {
        private String clientId;
        private URL baseUrl;
        private String authorizeUrl;
        private String tokenUrl;
        private String apiKey;
        private String oAuthSecret;
        private String oAuthRedirect;
        private String serviceUUID;
        private String publicKey;

        public WebApiJwtClientConfig build() {
            Validate.notBlank(clientId, "clientId must be non-blank");
            Validate.notNull(baseUrl, "baseUrl must be non-null");
            Validate.notBlank(authorizeUrl, "authorizeUrl must be non-blank");
            Validate.notBlank(tokenUrl, "clientId must be non-blank");
            Validate.notBlank(apiKey, "apiKey must be non-blank");
            Validate.notBlank(oAuthSecret, "oAuthSecret must be non-blank");
            Validate.notBlank(oAuthRedirect, "oAuthRedirect must be non-blank");
            Validate.notBlank(serviceUUID, "serviceUUID must be non-blank");
            Validate.notBlank(publicKey, "publicKey must be non-blank");
            return new WebApiJwtClientConfig(clientId, baseUrl, authorizeUrl, tokenUrl, apiKey, oAuthSecret,
                    oAuthRedirect, serviceUUID, publicKey);
        }

        public WebApiJwtClientConfig.Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public WebApiJwtClientConfig.Builder baseUrl(URL baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public WebApiJwtClientConfig.Builder authorizeUrl(String authorizeUrl) {
            this.authorizeUrl = authorizeUrl;
            return this;
        }

        public WebApiJwtClientConfig.Builder tokenUrl(String tokenUrl) {
            this.tokenUrl = tokenUrl;
            return this;
        }

        public WebApiJwtClientConfig.Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public WebApiJwtClientConfig.Builder oAuthSecret(String oAuthSecret) {
            this.oAuthSecret = oAuthSecret;
            return this;
        }

        public WebApiJwtClientConfig.Builder oAuthRedirect(String oAuthRedirect) {
            this.oAuthRedirect = oAuthRedirect;
            return this;
        }

        public WebApiJwtClientConfig.Builder serviceUUID(String serviceUUID) {
            this.serviceUUID = serviceUUID;
            return this;
        }

        public WebApiJwtClientConfig.Builder publicKey(String publicKey) {
            this.publicKey = publicKey;
            return this;
        }
    }
}
