package fi.vm.kapa.rova.client.webapi.impl;

import fi.vm.kapa.rova.client.webapi.*;

/**
 * Client factory for REST Api Reference Implementation clients.
 */
public class WebApiJwtRiClientFactory implements WebApiJwtClientFactory {

    private WebApiJwtClientConfig config;

    public WebApiJwtRiClientFactory(WebApiJwtClientConfig config) {
        this.config = config;
    }

    @Override
    public HpaWebApiJwtClient hpaWebApiJwtClient(String delegateId) {
        return new HpaWebApiJwtRiClient(config, delegateId);
    }

    @Override
    public YpaWebApiJwtClient ypaWebApiJwtClient(String delegateId) {
        return new YpaWebApiJwtRiClient(config, delegateId);
    }
}
