package fi.vm.kapa.rova.client.xroad;

import fi.vm.kapa.rova.client.common.EndPoint;
import fi.vm.kapa.rova.client.common.RovaServiceDetails;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for JAX-WS Reference Implementation clients.
 */
public abstract class AbstractRiClient {

    protected ArrayList<EndPoint> endPoints = new ArrayList<>();

    private int endPointIndex = 0;

    protected AbstractRiClient() {
    }

    protected HandlerResolver createHandlerResolver(XRoadClientConfig config, RovaServiceDetails details) {
        final HeaderHandler headerHandler = new HeaderHandler(config, details);
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
