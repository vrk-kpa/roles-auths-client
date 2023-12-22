package fi.vm.kapa.rova.client.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model object for Ypa organization role responses.
 */
public class YpaOrganization {

    private final String name;
    private final String identifier;
    private final List<String> roles;
    private final boolean complete;

    @SuppressWarnings("unused")
    private YpaOrganization() {
        this(null, null, true);
    }

    public YpaOrganization(String identifier, String name, boolean complete) {
        this.identifier = identifier;
        this.name = name;
        this.roles = new ArrayList<>();
        this.complete = complete;
    }

    /**
     * @return name of the organization.
     */
    public String getName() {
        return name;
    }

    /**
     * @return organization identifier (Y-tunnus).
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * @return Roles for the person in the organization, for example TJ, TIL, ELI, S, IS or NIMKO.
     */
    public List<String> getRoles() {
        return roles;
    }

    public boolean isComplete() {
        return complete;
    }

    @Override
    public String toString() {
        return "YpaOrganization [name=" + name + ", identifier=" + identifier + ", roles=" + roles + ", complete=" + complete
                + "]";
    }

}
