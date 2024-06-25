package fi.vm.kapa.rova.client.xroad.impl;

import fi.vm.kapa.rova.client.common.EndPoint;
import fi.vm.kapa.rova.client.common.RovaServiceDetails;
import fi.vm.kapa.rova.client.xroad.HeaderHandler;
import fi.vm.kapa.rova.client.xroad.XRoadClientConfig;

import jakarta.xml.ws.handler.Handler;
import jakarta.xml.ws.handler.HandlerResolver;
import jakarta.xml.ws.handler.PortInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for JAX-WS Reference Implementation clients.
 */
public abstract class AbstractRiClient {

    protected List<EndPoint> endPoints = new ArrayList<>();

    protected HeaderHandler headerHandler;

    private int endPointIndex = 0;

    protected AbstractRiClient() {
    }

    protected HandlerResolver createHandlerResolver(XRoadClientConfig config, RovaServiceDetails details) {
        headerHandler = new HeaderHandler(config, details);
        return new HandlerResolver() {
            @Override
            public List<Handler> getHandlerChain(PortInfo portInfo) {
                List<Handler> handlers = new ArrayList<>();
                handlers.add(headerHandler);
                return handlers;
            }
        };
    }

    protected synchronized String getNextEndpoint() {
        if (++endPointIndex >= endPoints.size()) {
            endPointIndex = 0;
        }
        return endPoints.get(endPointIndex).toString();
    }

}
