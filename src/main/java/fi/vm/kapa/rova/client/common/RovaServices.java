package fi.vm.kapa.rova.client.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Information for available Rova services.
 */
public class RovaServices {

    private static Map<String, RovaServiceDetails> services = null;

    static {
        services = new HashMap<>();
        services.put(RovaService.AUTHORIZATION.name(),
                new RovaServiceDetails("rovaAuthorizationService", "/rova/authorization"));
        services.put(RovaService.AUTHORIZATION_LIST.name(),
                new RovaServiceDetails("rovaAuthorizationListService", "/rova/authorizationList"));
        services.put(RovaService.ROLES.name(),
                new RovaServiceDetails("rovaOrganizationalRolesService", "/rova/OrganizationalRoles"));
        services.put(RovaService.ORG_MANDATES.name(),
                new RovaServiceDetails("rovaOrgMandatesService", "/rova/OrganizationalMandates"));
        services.put(RovaService.ORG_PERSON_MANDATES.name(),
                new RovaServiceDetails("rovaOrgPersonMandatesService", "/rova/OrganizationalPersonMandates"));
    }

    private RovaServices() {
    }

    public static RovaServiceDetails getDetails(String service) {
        return services.get(service);
    }

    public static void putDetails(String service, RovaServiceDetails details) {
        services.put(service, details);
    }

    public enum RovaService {
        AUTHORIZATION,
        AUTHORIZATION_LIST,
        ROLES,
        ORG_MANDATES,
        ORG_PERSON_MANDATES
    }

}

