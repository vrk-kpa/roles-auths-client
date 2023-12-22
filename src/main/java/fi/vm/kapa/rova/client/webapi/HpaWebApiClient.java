package fi.vm.kapa.rova.client.webapi;

import fi.vm.kapa.rova.client.model.Authorization;
import fi.vm.kapa.rova.client.model.AuthorizationList;
import fi.vm.kapa.rova.client.model.Principal;

import java.util.List;

/**
 * Client for HPA-api.
 */
public interface HpaWebApiClient extends WebApiClient {

    /**
     * Get principals chosen by end user.
     *
     * @param requestId
     * @return List of principals
     * @throws WebApiClientException
     * @see Principal
     */
    List<Principal> getPrincipals(String requestId) throws WebApiClientException;

    /**
     * Get authorization for selected user with optional issues.
     *
     * @param principalId Personal Identity Code identifying the principal
     * @param requestId
     * @param issue       URIs identifying issues
     * @return Authorization
     * @throws WebApiClientException
     */
    Authorization getAuthorization(String principalId, String requestId, String... issue)
            throws WebApiClientException;

    /**
     * Get authorizationList for selected user.
     *
     * @param principalId Personal Identity Code identifying the principal
     * @param requestId
     * @return AuthorizationList
     * @throws WebApiClientException
     */
    AuthorizationList getAuthorizationList(String principalId, String requestId)
            throws WebApiClientException;
}
