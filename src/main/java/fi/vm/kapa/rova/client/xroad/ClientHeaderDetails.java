package fi.vm.kapa.rova.client.xroad;

import java.util.Properties;

/**
 * Client specific values for XRoad message header.
 */
public class ClientHeaderDetails extends HeaderDetails {

    /**
     * {@value}
     */
    public static final String XROAD_INSTANCE_KEY = "roles-auths-client.client_xroad_instance";
    /**
     * {@value}
     */
    public static final String MEMBER_CLASS_KEY = "roles-auths-client.client_member_class";
    /**
     * {@value}
     */
    public static final String MEMBER_CODE_KEY = "roles-auths-client.client_member_code";
    /**
     * {@value}
     */
    public static final String SUBSYSTEM_CODE_KEY = "roles-auths-client.client_subsystem_code";
    /**
     * {@value}
     */
    public static final String VERSION_KEY = "roles-auths-client.client_version";

    public ClientHeaderDetails(Properties properties) {
        this.xRoadInstance = (String) properties.get(XROAD_INSTANCE_KEY);
        this.subsystemCode = (String) properties.get(SUBSYSTEM_CODE_KEY);
        this.memberClass = (String) properties.get(MEMBER_CLASS_KEY);
        this.memberCode = (String) properties.get(MEMBER_CODE_KEY);
        this.version = (String) properties.get(VERSION_KEY);
    }

    public ClientHeaderDetails(String xRoadInstance, String memberClass, String memberCode, String subsystemCode, String version) {
        this.xRoadInstance = xRoadInstance;
        this.memberClass = memberClass;
        this.memberCode = memberCode;
        this.subsystemCode = subsystemCode;
        this.version = version;
    }
}
