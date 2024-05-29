package fi.vm.kapa.rova.client.webapi;

public interface HpaWebApiJwtClient extends HpaWebApiClient {

    /**
     * Get authorization Json Web Token for selected user with optional issues.
     *
     * @param principalId Personal Identity Code identifying the principal
     * @param requestId
     * @param issue       URIs identifying issues
     * @return Authorization
     * @throws WebApiClientException
     */
    String getAuthorizationToken(String principalId, String requestId, String... issue)
            throws WebApiClientException;

    /**
     * Get authorizationList Json Web Token for selected user.
     *
     * @param principalId Personal Identity Code identifying the principal
     * @param requestId
     * @return AuthorizationList
     * @throws WebApiClientException
     */
    String getAuthorizationListToken(String principalId, String requestId)
            throws WebApiClientException;
}
