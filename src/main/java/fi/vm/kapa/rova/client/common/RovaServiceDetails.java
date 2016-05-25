package fi.vm.kapa.rova.client.common;

/**
 * Rova Service Details.
 */
public class RovaServiceDetails {

    private String xRoadServiceCode;
    private String path;

    public RovaServiceDetails(String xRoadServiceCode, String path) {
        this.xRoadServiceCode = xRoadServiceCode;
        this.path = path;
    }

    public String getXRoadServiceCode() {
        return xRoadServiceCode;
    }

    public void setXRoadServiceCode(String xRoadServiceCode) {
        this.xRoadServiceCode = xRoadServiceCode;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
