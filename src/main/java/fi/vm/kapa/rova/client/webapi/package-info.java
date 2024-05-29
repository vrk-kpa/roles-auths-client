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