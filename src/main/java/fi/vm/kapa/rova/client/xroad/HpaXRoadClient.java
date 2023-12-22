package fi.vm.kapa.rova.client.xroad;

import fi.vm.kapa.rova.client.model.Authorization;
import fi.vm.kapa.rova.client.model.AuthorizationList;

import java.util.Set;

/**
 * Client interface for fetching information required when working on behalf of an other person.
 */
public interface HpaXRoadClient {
    /**
     * @param userId      user identifier
     * @param delegateId  personal identification number of delegate
     * @param principalId personal identification number of principal
     * @param issue       possible issues that should be checked
     * @return boolean value if delegate is authorized or not
     */
    Authorization isAuthorized(String userId, String delegateId, String principalId, Set<String> issue);

    /**
     * @param userId      user identifier
     * @param delegateId  personal identification number of delegate
     * @param principalId personal identification number of principal
     * @return AuthorizationList containing authorization roles for delegate on behalf of principal
     */
    AuthorizationList getAuthorizationList(String userId, String delegateId, String principalId);
}
