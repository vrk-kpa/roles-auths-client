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
package fi.vm.kapa.rova.client.xroad.impl;

import fi.vm.kapa.rova.client.common.EndPoint;
import fi.vm.kapa.rova.client.common.RovaServiceDetails;
import fi.vm.kapa.rova.client.xroad.HeaderHandler;
import fi.vm.kapa.rova.client.xroad.XRoadClientConfig;

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
