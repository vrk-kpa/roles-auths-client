package fi.vm.kapa.rova.client.webapi;

import fi.vm.kapa.rova.client.model.YpaOrganization;

import java.util.List;

/**
 * Client for YPA-api
 */
public interface YpaWebApiClient extends WebApiClient {

    /**
     * Get companies chosen by end user.
     *
     * @param requestId
     * @return List of organizations and user's roles in them.
     * @throws WebApiClientException
     */
    List<YpaOrganization> getCompanies(String requestId) throws WebApiClientException;

    /**
     * Get user's roles in given organization.
     *
     * @param requestId
     * @param organizationId
     * @return Organization and user's roles in it
     * @throws WebApiClientException
     */
    List<YpaOrganization> getRoles(String requestId, String organizationId) throws WebApiClientException;
}
