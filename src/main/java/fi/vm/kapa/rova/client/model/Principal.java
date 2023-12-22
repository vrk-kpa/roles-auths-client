package fi.vm.kapa.rova.client.model;

/**
 * Person principal.
 */
public class Principal {

    private final String personId;
    private final String name;

    @SuppressWarnings("unused")
    private Principal() {
        this(null, null);
    }

    public Principal(String personId, String name) {
        this.personId = personId;
        this.name = name;
    }

    /**
     * @return Person identity code of this principal.
     */
    public String getPersonId() {
        return personId;
    }

    /**
     * @return Name of this principal.
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Principal [personId=" + personId + ", name=" + name + "]";
    }

}

