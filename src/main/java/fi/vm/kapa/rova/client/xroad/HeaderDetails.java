package fi.vm.kapa.rova.client.xroad;

/**
 * XRoad message header information.
 */
public class HeaderDetails {

    protected String xRoadInstance;
    protected String memberClass;
    protected String memberCode;
    protected String subsystemCode;
    protected String version;

    public HeaderDetails() {
        // NOP
    }

    public HeaderDetails(String xRoadInstance, String memberClass, String memberCode, String subsystemCode, String version) {
        this.xRoadInstance = xRoadInstance;
        this.memberClass = memberClass;
        this.memberCode = memberCode;
        this.subsystemCode = subsystemCode;
        this.version = version;
    }

    public String getSubsystemCode() {
        return subsystemCode;
    }

    public void setSubsystemCode(String subsystemCode) {
        this.subsystemCode = subsystemCode;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getMemberClass() {
        return memberClass;
    }

    public void setMemberClass(String memberClass) {
        this.memberClass = memberClass;
    }

    public String getXRoadInstance() {
        return xRoadInstance;
    }

    public void setXRoadInstance(String xRoadInstance) {
        this.xRoadInstance = xRoadInstance;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
