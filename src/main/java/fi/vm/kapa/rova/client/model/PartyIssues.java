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

/**
 * Class used in YYA and YHA responses that contains information on what issues
 * the principal has given a mandate to the delegate in the request.
 */
import java.util.Collection;
import java.util.HashSet;

public class PartyIssues {

    public PartyIssues(String id, Collection<String> issues, boolean incomplete) {
        this.id = id;
        this.issues = issues;
        this.incomplete = incomplete;
    }

    /**
     * Id of the principal
     */
    private String id;
    
    /**
     * List of issues
     */
    private Collection<String> issues;
    
    /**
     * Indicates if the evaluation for this principal was fully executed.
     * The value of this is true if some error prevents full evaluation,
     * otherwise false.
     */
    private boolean incomplete;

    public String getId() {
        return id;
    }

    public Collection<String> getIssues() {
        if (issues == null) {
            issues = new HashSet<>();
        }
        return issues;
    }

    public boolean isIncomplete() {
        return incomplete;
    }

    public void markIncomplete() {
        incomplete = true;
    }

    @Override
    public String toString() {
        return "PartyIssues[" +
                "id='" + id + '\'' +
                ", issues=" + issues +
                ", incomplete=" + incomplete +
                ']';
    }
}
