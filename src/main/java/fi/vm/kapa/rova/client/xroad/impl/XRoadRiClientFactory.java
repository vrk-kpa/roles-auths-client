package fi.vm.kapa.rova.client.xroad.impl;

import fi.vm.kapa.rova.client.xroad.*;

/**
 * Client factory for JAX-WS Reference Implementation clients.
 * If several XRoad servers are configured into use they will
 * be used as endpoints with round-robin strategy without fail-over.
 */
public class XRoadRiClientFactory implements XRoadClientFactory {

    private XRoadClientConfig config;

    public XRoadRiClientFactory(XRoadClientConfig config) {
        this.config = config;
    }

    @Override
    public HpaXRoadClient hpaClient() {
        return new HpaXRoadRiClient(config);
    }

    @Override
    public YpaXRoadClient ypaClient() {
        return new YpaXRoadRiClient(config);
    }

    @Override
    public YyaXRoadClient yyaClient() {
        return new YyaXRoadRiClient(config);
    }

    @Override
    public YhaXRoadClient yhaClient() {
        return new YhaXRoadRiClient(config);
    }

}
