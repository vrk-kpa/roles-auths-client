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
/**

 <h2>Usage example for web api</h2>
 Start by getting creating client {@link fi.vm.kapa.rova.client.webapi.impl.WebApiRiClientFactory factory}
 with appropriate {@link fi.vm.kapa.rova.client.webapi.WebApiClientConfig config}.

 Next initiate session and redirect end user's browser to the returned URL.
 <pre>
 {@code
 WebApiClientFactory factory = clientFactoryStore.getHpaFactory(clientId);
 HpaWebApiClient client = factory.hpaWebApiClient(delegatePersonId);
 String authorizeUrl = client.register("requestId");
 }
 </pre>

 <p>End user chooses the principal in Rova WebApi and returns to service with the OAuth
 response code and OAuth state. Fetch the access token and selected principal.</p>
 <pre>
 {@code
 client.getToken(code, state);
 List<Principal> principals = client.getPrincipals("requestId")
 }
 </pre>
 Check the authorization, optionally providing issue.

 <pre>
 {@code
 Authorization auth = client.getAuthorization(principal, "requestId", issue);
 }
 </pre>


 Using Ypa api is similar to Hpa. After creating the client and registering, redirect
 user's browser to authorizeUrl and fetch the token. Then fetch selected organization.

 <PRE>
 List&lt;YpaOrganization&gt; orgResult = ypaWebApiClient.getCompanies("requestId");
 </PRE>
 */
package fi.vm.kapa.rova.client.webapi;