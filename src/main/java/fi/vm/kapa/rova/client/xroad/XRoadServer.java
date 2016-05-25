package fi.vm.kapa.rova.client.xroad;

import fi.vm.kapa.rova.client.common.Server;

/**
 * XRoad server details.
 */
public class XRoadServer implements Server {

    public static final int DEFAULT_PORT = 443;
    public static final boolean DEFAULT_SECURE = true;

    private String host;
    private int port = DEFAULT_PORT;
    private boolean secure = DEFAULT_SECURE;

    public XRoadServer(String host) {
        this.host = host;
    }
    public XRoadServer(String host, int port, boolean secure) {
        this.host = host;
        this.port = port;
        this.secure = secure;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }
}
