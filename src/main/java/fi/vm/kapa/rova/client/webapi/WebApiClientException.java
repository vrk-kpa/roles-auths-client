package fi.vm.kapa.rova.client.webapi;

/**
 * Generic exception class for WebApiClient
 * <p>
 * This exception can be thrown if client is in error state.
 * This exception is also thrown if any underlying library throws checked exception.
 */
public class WebApiClientException extends Exception {

    private static final long serialVersionUID = 1L;

    public WebApiClientException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public WebApiClientException(String msg) {
        super(msg);
    }

}
