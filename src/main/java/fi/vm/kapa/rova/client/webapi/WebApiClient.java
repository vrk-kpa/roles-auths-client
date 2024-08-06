package fi.vm.kapa.rova.client.webapi;

import java.io.Serializable;

/**
 * Shared parent methods for HpaWebApiClient and YpaWebClient.
 */
public interface WebApiClient extends Serializable {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String ASIOINTIVALTUUDET_AUTHORIZATION_HEADER = "X-AsiointivaltuudetAuthorization";

    //Initiates a session with webApi. Stores OAuth userid and sessionid for later requests.

    /**
     * Initiates a session with webApi. Stores OAuth userid and sessionid for later requests.
     * Return a URL for Rova WebApi selection screen. End user should be directed there for
     * principal selection.
     *
     * @param requestId
     * @param userInterfaceLanguage Language code (ISO 639) for the WebApi selection screen, or null to use the default language.
     * @return URL for Rova WebApi selection screen.
     * @throws WebApiClientException
     */
    String register(String requestId, String userInterfaceLanguage) throws WebApiClientException;

    /**
     * Terminates a session with webApi.
     * Returns true for successful termination. E
     *
     * @throws WebApiClientException
     */
    Boolean unregister() throws WebApiClientException;

    /**
     * Initiates a session with webApi using transfer token from existing web api session.
     * Stores OAuth userid and sessionid for later requests.
     * Return a URL for Rova WebApi selection screen. End user should be directed there for
     * principal selection.
     *
     * @param requestId
     * @param userInterfaceLanguage Language code (ISO 639) for the WebApi selection screen, or null to use the default language.
     * @return URL for Rova WebApi selection screen.
     * @throws WebApiClientException
     */
    String registerTransfer(String transferToken, String requestId, String userInterfaceLanguage) throws WebApiClientException;

    /**
     * Requests transfer token for current web api session.
     * Returns transfer token as String.
     *
     * @return URL for Rova WebApi selection screen.
     * @throws WebApiClientException
     */
    String getTransferToken() throws WebApiClientException;

    /**
     * Get OAuth access token and store it in this client.
     *
     * @param codeParam  OAuth response code received by end user after selection as query parameter
     * @param stateParam OAuth response state received by end user after selection as query parameter
     * @throws WebApiClientException
     */
    void getToken(String codeParam, String stateParam) throws WebApiClientException;

    /**
     * Get delegate id that was provided in the client initialization.
     *
     * @return String;
     */
    String getDelegateId();
}
