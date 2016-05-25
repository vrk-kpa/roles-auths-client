package fi.vm.kapa.rova.client.common;

/**
 * Service endpoint containing service and path information.
 */
public class EndPoint {

    public static final String DEFAULT_PATH = "/";

    private Server server;
    private String path = DEFAULT_PATH;

    public EndPoint(Server server) {
        this.server = server;
    }

    public EndPoint(Server server, String path) {
        this.server = server;
        if (path != null && ! path.startsWith("/")) {
            this.path = "/" + path;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return String.format("%s://%s:%d%s", ((server.isSecure()) ? "https": "http"),
                server.getHost(), server.getPort(), ((path != null) ? path : "/"));
    }

}
