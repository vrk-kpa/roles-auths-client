package fi.vm.kapa.rova.client.webapi;

/**
 * Created by Juha Korkalainen on 14.6.2016.
 */
public class WebApiClientException extends Exception {
    public WebApiClientException(String msg, Throwable cause) {
        super(msg, cause);
    }
    public WebApiClientException(String msg) {
        super(msg);
    }

}
