package fi.vm.kapa.rova.client.webapi;

import fi.vm.kapa.rova.client.model.YpaOrganization;

import java.util.List;

/**
 * Client for YPA-api
 */
public interface YpaWebApiJwtClient extends YpaWebApiClient {

    /**
     * Get companies chosen by end user.
     *
     * @param requestId
     * @return List of organizations and user's roles in them.
     * @throws WebApiClientException
     */
    String getCompaniesToken(String requestId) throws WebApiClientException;

    /**
     * Get user's roles in given organization.
     *
     * @param requestId
     * @param organizationId
     * @return Organization and user's roles in it
     * @throws WebApiClientException
     */
    String getRolesToken(String requestId, String organizationId) throws WebApiClientException;

    /**
     * Get JWT token representing users session
     *
     * @param requestId
     * @return A token representing users ypa session.
     * @throws WebApiClientException
     */
    String getCompaniesSessionToken(String requestId) throws WebApiClientException;

    /**
     * Get companies chosen by end user.
     *
     * @param sessionJwtToken
     * @param requestId
     * @return List of organizations and user's roles in them.
     * @throws WebApiClientException
     */
    List<YpaOrganization> getSessionCompanies(String sessionJwtToken, String requestId) throws WebApiClientException;

}
