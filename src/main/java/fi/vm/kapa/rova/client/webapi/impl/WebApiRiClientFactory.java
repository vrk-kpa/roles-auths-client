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
