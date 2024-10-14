package fi.vm.kapa.rova.client.webapi.impl;

import com.nimbusds.jose.util.Base64;
import com.nimbusds.oauth2.sdk.auth.ClientAuthenticationMethod;
import com.nimbusds.oauth2.sdk.auth.PlainClientSecret;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.id.ClientID;

import java.util.Set;

class BasicClientSecret extends PlainClientSecret {

    public BasicClientSecret(final ClientID clientID, final Secret secret) {
        super(ClientAuthenticationMethod.CLIENT_SECRET_BASIC, clientID, secret);
    }

    @Override
    public Set<String> getFormParameterNames() {
        return Set.of();
    }

    @Override
    public void applyTo(HTTPRequest httpRequest) {
        httpRequest.setAuthorization("Basic " + Base64.encode(getClientID().getValue() + ":"
                + getClientSecret().getValue()));
    }
}
