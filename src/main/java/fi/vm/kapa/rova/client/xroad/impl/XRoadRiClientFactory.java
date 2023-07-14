/**
 * The MIT License
 * Copyright (c) 2022 Digital and Population Data Services Agency
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
