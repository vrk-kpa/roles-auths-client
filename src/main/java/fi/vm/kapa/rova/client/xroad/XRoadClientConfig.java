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
package fi.vm.kapa.rova.client.xroad;

import java.util.Set;

/**
 * Configuration class for XRoad connections.
 */
public class XRoadClientConfig {

    private HeaderDetails clientDetails;
    private HeaderDetails serviceDetails;
    private String clientId;
    private Set<XRoadServer> servers;
    private RequestIdGenerator requestIdGenerator;

    /**
     *
     * @param clientDetails client header details
     * @param serviceDetails service header details
     * @param clientId api user indentifier
     * @param servers XRoad servers
     * @see ClientHeaderDetails
     * @see ServiceHeaderDetails
     */
    public XRoadClientConfig(HeaderDetails clientDetails, HeaderDetails serviceDetails, String clientId, Set<XRoadServer> servers) {
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
