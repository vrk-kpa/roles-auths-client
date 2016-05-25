package fi.vm.kapa.rova.client.xroad;

/**
 * Request identifier generator interface.
 */
public interface RequestIdGenerator {
    /**
     * Generates a string that identifies a request.
     * @return String that identifies the request.
     */
    String generateId();
}
