package fi.vm.kapa.rova.client.common;

/**
 * Server interface.
 */
public interface Server {
    String getHost();

    int getPort();

    boolean isSecure();
}
