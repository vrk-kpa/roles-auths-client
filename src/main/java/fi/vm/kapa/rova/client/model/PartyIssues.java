package fi.vm.kapa.rova.client.model;

/**
 * Class used in YYA and YHA responses that contains information on what issues
 * the principal has given a mandate to the delegate in the request.
 */
import java.util.Collection;
import java.util.HashSet;

public class PartyIssues {

    public PartyIssues(String id, Collection<String> issues, boolean incomplete) {
        this.id = id;
        this.issues = issues;
        this.incomplete = incomplete;
    }

    /**
     * Id of the principal
     */
    private String id;
    
    /**
     * List of issues
     */
    private Collection<String> issues;
    
    /**
     * Indicates if the evaluation for this principal was fully executed.
     * The value of this is true if some error prevents full evaluation,
     * otherwise false.
     */
    private boolean incomplete;

    public String getId() {
        return id;
    }

    public Collection<String> getIssues() {
        if (issues == null) {
            issues = new HashSet<>();
        }
        return issues;
    }

    public boolean isIncomplete() {
        return incomplete;
    }

    public void markIncomplete() {
        incomplete = true;
    }

    @Override
    public String toString() {
        return "PartyIssues[" +
                "id='" + id + '\'' +
                ", issues=" + issues +
                ", incomplete=" + incomplete +
                ']';
    }
}
