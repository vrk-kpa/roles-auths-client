package fi.vm.kapa.rova.client.xroad;

import fi.vm.kapa.rova.client.ClientFactory;
import fi.vm.kapa.rova.client.HpaClient;
import fi.vm.kapa.rova.client.YpaClient;

/**
 * Client factory for JAX-WS Reference Implementation clients.
 * If several XRoad servers are configured into use they will
 * be used as endpoints with round-robin strategy without fail-over.
 */
public class RiClientFactory implements ClientFactory {

    private XRoadClientConfig config;

    public RiClientFactory(XRoadClientConfig config) {
        this.config = config;
    }

    @Override
    public HpaClient hpaClient() {
        return new HpaRiClient(config);
    }

    @Override
    public YpaClient ypaClient() {
        return new YpaRiClient(config);
    }

}
