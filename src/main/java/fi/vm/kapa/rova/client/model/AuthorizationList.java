package fi.vm.kapa.rova.client.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * AuthorizationList contains roles which user have on behalf of another.
 * Reasons for not providing any roles are given, if service configuration permits disclosing them.
 */
public class AuthorizationList {

    private final List<String> roles;
    private final Set<DecisionReason> reasons;

    @SuppressWarnings("unused")
    private AuthorizationList() {
        this.roles = null;
        this.reasons = new HashSet<>();
    }

    public AuthorizationList(List<String> roles) {
        this.roles = roles;
        this.reasons = new HashSet<>();
    }

    public List<String> getRoles() {
        return roles;
    }

    public Set<DecisionReason> getReasons() {
        return reasons;
    }

    @Override
    public String toString() {
        return "AuthorizationList [roles=" + roles + ", reasons=" + reasons + "]";
    }

}
