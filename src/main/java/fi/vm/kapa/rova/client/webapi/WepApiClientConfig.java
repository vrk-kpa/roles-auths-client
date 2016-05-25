package fi.vm.kapa.rova.client.webapi;

import fi.vm.kapa.rova.client.xroad.HeaderDetails;
import fi.vm.kapa.rova.client.xroad.RequestIdGenerator;
import fi.vm.kapa.rova.client.xroad.XRoadServer;

import java.util.Set;

/**
 * Created by mtom on 5/20/16.
 */
public class WepApiClientConfig {

    private HeaderDetails clientDetails;
    private HeaderDetails serviceDetails;
    private String clientId;
    private Set<XRoadServer> servers;
    private RequestIdGenerator requestIdGenerator;

    public WepApiClientConfig(HeaderDetails clientDetails, HeaderDetails serviceDetails, String clientId, Set<XRoadServer> servers) {
        this.clientDetails = clientDetails;
        this.serviceDetails = serviceDetails;
        this.clientId = clientId;
        this.servers = servers;
    }

    public HeaderDetails getClientDetails() {
        return clientDetails;
    }

    public void setClientDetails(HeaderDetails clientDetails) {
        this.clientDetails = clientDetails;
    }

    public HeaderDetails getServiceDetails() {
        return serviceDetails;
    }

    public void setServiceDetails(HeaderDetails serviceDetails) {
        this.serviceDetails = serviceDetails;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Set<XRoadServer> getServers() {
        return servers;
    }

    public void setServers(Set<XRoadServer> servers) {
        this.servers = servers;
    }

    public RequestIdGenerator getRequestIdGenerator() {
        return requestIdGenerator;
    }

    public void setRequestIdGenerator(RequestIdGenerator requestIdGenerator) {
        this.requestIdGenerator = requestIdGenerator;
    }
}
