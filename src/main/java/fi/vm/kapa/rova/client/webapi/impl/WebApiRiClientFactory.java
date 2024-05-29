package fi.vm.kapa.rova.client.webapi.impl;

import fi.vm.kapa.rova.client.webapi.HpaWebApiClient;
import fi.vm.kapa.rova.client.webapi.WebApiClientConfig;
import fi.vm.kapa.rova.client.webapi.WebApiClientFactory;
import fi.vm.kapa.rova.client.webapi.YpaWebApiClient;

/**
 * Client factory for REST Api Reference Implementation clients.
 */
public class WebApiRiClientFactory implements WebApiClientFactory {

    private WebApiClientConfig config;

    public WebApiRiClientFactory(WebApiClientConfig config) {
        this.config = config;
    }

    @Override
    public HpaWebApiClient hpaWebApiClient(String delegateId) {
        return new HpaWebApiRiClient(config, delegateId);
    }

    @Override
    public YpaWebApiClient ypaWebApiClient(String delegateId) {
        return new YpaWebApiRiClient(config, delegateId);
    }
}
