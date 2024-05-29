package fi.vm.kapa.rova.client.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Authorization tells whether queried operation is allowed or disallowed. Reasons for
 * disallowing are given, if service configuration permits disclosing them.
 */
public class Authorization {

    public enum Result {
        ALLOWED, DISALLOWED
    }

    private final Result result;
    private final Set<DecisionReason> reasons;

    @SuppressWarnings("unused")
    private Authorization() {
        this.result = null;
        this.reasons = new HashSet<>();
    }

    public Authorization(Result result) {
        this.result = result;
        this.reasons = new HashSet<>();
    }

    public Result getResult() {
        return result;
    }

    public Set<DecisionReason> getReasons() {
        return reasons;
    }

    @Override
    public String toString() {
        return "Authorization [result=" + result + ", reasons=" + reasons + "]";
    }

}
