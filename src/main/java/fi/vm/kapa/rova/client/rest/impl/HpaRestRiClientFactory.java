package fi.vm.kapa.rova.client.rest.impl;

import fi.vm.kapa.rova.client.rest.HpaRestClient;
import fi.vm.kapa.rova.client.rest.HpaRestClientFactory;
import fi.vm.kapa.rova.client.rest.RestClientConfig;

public class HpaRestRiClientFactory implements HpaRestClientFactory {

    private RestClientConfig restClientConfig;

    public HpaRestRiClientFactory(RestClientConfig restClientConfig) {
        this.restClientConfig = restClientConfig;
    }

    @Override
    public HpaRestClient hpaRestClient() {
        return new HpaRestRiClient(restClientConfig);
    }

}
