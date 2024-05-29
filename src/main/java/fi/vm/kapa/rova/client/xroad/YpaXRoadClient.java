package fi.vm.kapa.rova.client.xroad;


import fi.vm.kapa.rova.client.model.YpaOrganization;

import java.util.List;

/**
 * Client interface for fetching information required when working on behalf of a company.
 */
public interface YpaXRoadClient {
    List<YpaOrganization> getRoles(String userId, String delegateId);
}
