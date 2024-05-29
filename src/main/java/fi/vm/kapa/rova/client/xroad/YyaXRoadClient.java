package fi.vm.kapa.rova.client.xroad;

import fi.vm.kapa.rova.client.model.PartyIssues;

import java.util.List;

public interface YyaXRoadClient {
   
    /**
     * Returns issues where delegate company has gotten a mandate from the principal company.
     *
     * @param userId        user identifier
     * @param delegateId    Id of the delegate (y-tunnus, VAT id or national company id)
     * @param principalIds  List of principal ids (y-tunnus, VAT id or national company id)
     * @return              List of PartyIssues {@link fi.vm.kapa.rova.client.model.PartyIssues}
     */
    List<PartyIssues> getOrganizationalMandates(String userId, String delegateId, List<String> principalIds);

}