package fi.vm.kapa.rova.client.rest;

/**
 * Generic exception class for WebApiClient
 * <p>
 * This exception can be thrown if client is in error state.
 * This exception is also thrown if any underlying library throws checked exception.
 */
public class RestClientException extends Exception {

    private static final long serialVersionUID = 1L;

    public RestClientException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RestClientException(String msg) {
        super(msg);
    }

}
