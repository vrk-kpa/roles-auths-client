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
package fi.vm.kapa.rova.client.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model object for Ypa organization role responses.
 */
public class YpaOrganization {

    private final String name;
    private final String identifier;
    private final List<String> roles;
    private final boolean complete;

    @SuppressWarnings("unused")
    private YpaOrganization() {
        this(null, null, true);
    }

    public YpaOrganization(String identifier, String name, boolean complete) {
        this.identifier = identifier;
        this.name = name;
        this.roles = new ArrayList<>();
        this.complete = complete;
    }

    /**
     * @return name of the organization.
     */
    public String getName() {
        return name;
    }

    /**
     * @return organization identifier (Y-tunnus).
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * @return Roles for the person in the organization, for example TJ, TIL, ELI, S, IS or NIMKO.
     */
    public List<String> getRoles() {
        return roles;
    }

    public boolean isComplete() {
        return complete;
    }

    @Override
    public String toString() {
        return "YpaOrganization [name=" + name + ", identifier=" + identifier + ", roles=" + roles + ", complete=" + complete
                + "]";
    }

}
