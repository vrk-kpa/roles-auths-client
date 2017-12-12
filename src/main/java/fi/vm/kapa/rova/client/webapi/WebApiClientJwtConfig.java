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

import java.net.URL;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class WebApiClientJwtConfig extends WebApiClientConfig {

    private String publicKey;

    private Key webApiPublicKey;

    private PublicKey webApiRSAPublicKey;

    private String serviceUUID;

    public WebApiClientJwtConfig(String clientId, URL baseUrl, String authorizeUrl, String tokenUrl, String apiKey,
                                 String oAuthSecret, String oAuthRedirect, String publicKey)
            throws InvalidKeySpecException, NoSuchAlgorithmException {

        super(clientId, baseUrl, authorizeUrl, tokenUrl, apiKey, oAuthSecret, oAuthRedirect);

        byte[] publicBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        webApiRSAPublicKey = keyFactory.generatePublic(keySpec);
    }

    public RSAPublicKey getRSAPublicKey() {
        return (RSAPublicKey) webApiRSAPublicKey;
    }

    public String getServiceUUID() {
        return serviceUUID;
    }
}
