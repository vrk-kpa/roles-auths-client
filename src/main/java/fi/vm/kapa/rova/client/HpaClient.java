package fi.vm.kapa.rova.client;

import fi.vm.kapa.xml.rova.api.delegate.PrincipalType;

import java.util.List;

/**
 * Client interface for fetching information required when when working on behalf of an other person.
 */
public interface HpaClient {
    /**
     * @param delegateId personal identification number of delegate
     * @param principalId personal identification number of principal
     * @param issue possible issue that is being checked
     * @return boolean value if delegate is authorized or not
     */
    boolean getAuthorization(String delegateId, String principalId, String issue);
}
