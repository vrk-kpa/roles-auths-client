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
package fi.vm.kapa.rova.client.webapi.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.vm.kapa.rova.client.webapi.WebApiClientConfig;
import fi.vm.kapa.rova.client.webapi.YpaWebApiClient;

public abstract class AbstractYpaWebApiRiClient extends AbstractWebApiRiClient implements YpaWebApiClient {
    protected ObjectMapper mapper = new ObjectMapper();

    public AbstractYpaWebApiRiClient(WebApiClientConfig config, String delegateId) {
        super(config, delegateId);
    }

    @Override
    protected String getRegisterUrl() {
        return "/service/ypa/user/register/" + config.getClientId() + "/" + delegateId;
    }

    @Override
    protected String getUnRegisterUrl(String sessionId) {
        return "/service/ypa/user/unregister/" + sessionId;
    }

    protected String getTransferUrl(String sessionId) {
        return "/service/ypa/user/transfer/token/" + sessionId;
    }

    protected String getRegisterTransferUrl(String transferToken) {
    	try {
    		String encodedTransferToken = URLEncoder.encode(transferToken, StandardCharsets.UTF_8.toString());
    		return "/service/ypa/user/register/transfer/" + encodedTransferToken + "/" + config.getClientId() + "/" + delegateId;
    	} catch(UnsupportedEncodingException e) {
    	    throw new AssertionError(e);
    	}
    }

    protected String getRequestPathWithParams(String requestId, String organizationId, boolean jwt) {
        String base = "/service/ypa/api/organizationRoles/";
        if (jwt) {
            base = base + "jwt/";
        }
        if (organizationId != null) {
            return getPathWithParams(base + getOauthSessionId() + "/" + organizationId, requestId);
        } else {
            return getPathWithParams(base + getOauthSessionId(), requestId);
        }
    }

}
