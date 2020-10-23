/**
 * The MIT License
 * Copyright (c) 2016 Population Register Centre
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fi.vm.kapa.rova.client.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Information for available Rova services.
 */
public class RovaServices {

    private static Map<String, RovaServiceDetails> services = null;

    static {
        services = new HashMap();
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

