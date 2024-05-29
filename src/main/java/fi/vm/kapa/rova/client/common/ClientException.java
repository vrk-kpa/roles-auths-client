package fi.vm.kapa.rova.client.common;

/**
 * Exception class for common issues.
 */
public class ClientException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ClientException(String reason, Throwable t) {
        super(reason, t);
    }

    public ClientException(String reason) {
        super(reason);
    }
}
