package fi.vm.kapa.rova.client.rest;

import fi.vm.kapa.rova.client.model.Authorization;

/**
 * Client for HPA-rest-api.
 */
public interface HpaRestClient {
    /**
     * Get authorization for selected parties with optional issues.
     *
     * @param endUserId End user identification information
     * @param delegateId Personal Identity Code identifying the delegate
     * @param principalId Personal Identity Code identifying the principal
     * @param requestId
     * @param issue       URIs identifying issues
     * @return Authorization
     * @throws RestClientException
     */
    Authorization getAuthorization(String endUserId, String delegateId, String principalId, String requestId, String... issue)
            throws RestClientException;
}
