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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * AuthorizationList contains roles which user have on behalf of another.
 * Reasons for not providing any roles are given, if service configuration permits disclosing them.
 */
public class AuthorizationList {

    private final List<String> roles;
    private final Set<DecisionReason> reasons;

    @SuppressWarnings("unused")
    private AuthorizationList() {
        this.roles = null;
        this.reasons = new HashSet<>();
    }

    public AuthorizationList(List<String> roles) {
        this.roles = roles;
        this.reasons = new HashSet<>();
    }

    public List<String> getRoles() {
        return roles;
    }

    public Set<DecisionReason> getReasons() {
        return reasons;
    }

    @Override
    public String toString() {
        return "AuthorizationList [roles=" + roles + ", reasons=" + reasons + "]";
    }

}
