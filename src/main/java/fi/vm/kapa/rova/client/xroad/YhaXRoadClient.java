package fi.vm.kapa.rova.client.xroad;

import fi.vm.kapa.rova.client.model.PartyIssues;

import java.util.List;

public interface YhaXRoadClient {
    /**
     * Returns issues where delegate company has gotten a mandate from the principal person.
     *
     * @param userId        user identifier
     * @param delegateId    Id of the delegate (y-tunnus, VAT id or national company id)
     * @param principalIds  List of principal ids (hetu or UID)
     * @return              List of PartyIssues {@link fi.vm.kapa.rova.client.model.PartyIssues}
     */
    List<PartyIssues> getOrganizationalPersonMandates(String userId, String delegateId, List<String> principalIds);
}
