package fi.vm.kapa.rova.client.xroad;

import java.util.Properties;

/**
 * XRoad message header information.
 */
public class HeaderDetails {

    protected String sdsbInstance;
    protected String memberClass;
    protected String memberCode;
    protected String subsystemCode;
    protected String version;

    public HeaderDetails() {
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

    public String getSdsbInstance() {
        return sdsbInstance;
    }

    public void setSdsbInstance(String sdsbInstance) {
        this.sdsbInstance = sdsbInstance;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
