/**
 * The MIT License
 * Copyright (c) 2022 Digital and Population Data Services Agency
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
package fi.vm.kapa.rova.client.xroad;

import java.util.Properties;

/**
 * Service specific values for XRoad message header.
 */
public class ServiceHeaderDetails extends HeaderDetails {

    /**
     * {@value}
     */
    public static final String XROAD_INSTANCE_KEY = "roles-auths-client.service_xroad_instance";
    /**
     * {@value}
     */
    public static final String MEMBER_CLASS_KEY = "roles-auths-client.service_member_class";
    /**
     * {@value}
     */
    public static final String MEMBER_CODE_KEY = "roles-auths-client.service_member_code";
    /**
     * {@value}
     */
    public static final String SUBSYSTEM_CODE_KEY = "roles-auths-client.service_subsystem_code";
    /**
     * {@value}
     */
    public static final String VERSION_KEY = "roles-auths-client.service_version";

    public ServiceHeaderDetails(Properties properties) {
        this.xRoadInstance = (String) properties.get(XROAD_INSTANCE_KEY);
        this.subsystemCode = (String) properties.get(SUBSYSTEM_CODE_KEY);
        this.memberClass = (String) properties.get(MEMBER_CLASS_KEY);
        this.memberCode = (String) properties.get(MEMBER_CODE_KEY);
        this.version = (String) properties.get(VERSION_KEY);
    }

}
