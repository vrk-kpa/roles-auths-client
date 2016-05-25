package fi.vm.kapa.rova.client;


import fi.vm.kapa.rova.client.model.YpaOrganization;

import java.util.List;

/**
 * Client interface for fetching information required when when working on behalf of a company.
 */
public interface YpaClient {
    List<YpaOrganization> getRoles(String delegateId);
}
