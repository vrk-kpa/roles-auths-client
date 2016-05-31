package fi.vm.kapa.rova.client.model;

import java.util.HashSet;
import java.util.Set;

public class Authorization {
    private final Boolean authorized;
    private final Set<DecisionReason> reasons;

    @SuppressWarnings("unused")
    private Authorization() {
        this.authorized = null;
        this.reasons = new HashSet<>();
    }

    public Authorization(boolean authorized) {
        this.authorized = authorized;
        this.reasons = new HashSet<>();
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public Set<DecisionReason> getReasons() {
        return reasons;
    }
}
