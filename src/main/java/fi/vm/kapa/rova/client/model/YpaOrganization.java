package fi.vm.kapa.rova.client.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model object for Ypa responses.
 */
public class YpaOrganization {

        private String name;
        private String identifier;
        private List<String> roles = new ArrayList<String>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }
}
