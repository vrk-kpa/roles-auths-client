package fi.vm.kapa.rova.client.xroad;

import java.util.UUID;

/**
 * Default implementation for RequestIdGenerator interface.
 */
public class DefaultRequestIdGenerator implements RequestIdGenerator {

    @Override
    public String generateId() {
        return UUID.randomUUID().toString();
    }

}
