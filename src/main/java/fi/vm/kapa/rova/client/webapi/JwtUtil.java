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
package fi.vm.kapa.rova.client.webapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import fi.vm.kapa.rova.client.model.Authorization;
import fi.vm.kapa.rova.client.model.AuthorizationList;
import fi.vm.kapa.rova.client.model.DecisionReason;
import fi.vm.kapa.rova.client.model.YpaOrganization;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class JwtUtil {

    public static final String ISSUER = "Suomi.fi-Valtuudet";
    public static final String SUBJECT_ORG_ROLES = "OrganizationalRoles";
    public static final String SUBJECT_AUTHORIZATION = "Authorization";
    public static final String SUBJECT_AUTHORIZATION_LIST = "AuthorizationList";
    public static final String END_USER = "end_user";
    public static final String PRINCIPAL = "principal";
    public static final String ISSUE = "issue";
    public static final String RESPONSE = "response";
    public static final String REASONS = "reasons";

    private WebApiJwtClientConfig jwtConfig;

    private ObjectMapper objectMapper = new ObjectMapper();

    public JwtUtil(WebApiJwtClientConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }


    public SignedJWT parseAndVerifyToken(String jwtString) throws WebApiClientException {
        try {
            SignedJWT signedJWT = SignedJWT.parse(jwtString);

            JWSVerifier verifier = new RSASSAVerifier(jwtConfig.getRSAPublicKey());
            if (signedJWT.verify(verifier)) {
                JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
                if (claimsSet.getAudience().contains(jwtConfig.getServiceUUID()) &&
                        claimsSet.getIssuer().equalsIgnoreCase(JwtUtil.ISSUER)) {
                    return signedJWT;
                }
            }
        } catch (ParseException | JOSEException e) {
            throw new WebApiClientException(e.getMessage());
        }
        throw new WebApiClientException("Authorization token cannot be verified");
    }

    public Authorization getAuthorization(String jwtString, String principalId) throws WebApiClientException {
        try {
            SignedJWT signedJWT = parseAndVerifyToken(jwtString);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            if (claimsSet.getStringClaim(JwtUtil.PRINCIPAL).equalsIgnoreCase(principalId) &&
                    claimsSet.getSubject().equalsIgnoreCase(SUBJECT_AUTHORIZATION)) {

                String responseString = claimsSet.getStringClaim(RESPONSE);
                Authorization auth = new Authorization(Authorization.Result.valueOf(responseString));

                String reasonsJson = claimsSet.getStringClaim(REASONS);
                if (StringUtils.isNotBlank(reasonsJson)) {
                    Set<DecisionReason> reasons = objectMapper.readValue(reasonsJson, TypeFactory.defaultInstance().constructCollectionType(Set.class, DecisionReason.class));
                    if (reasons != null && !reasons.isEmpty()) {
                        auth.getReasons().addAll(reasons);
                    }
                }

                return auth;
            }
            throw new WebApiClientException("Authorization token cannot be verified");
        } catch (ParseException | JsonProcessingException e) {
            throw new WebApiClientException(e.getMessage());
        }
    }

    public AuthorizationList getAuthorizationList(String jwtString, String principalId) throws WebApiClientException {
        try {
            SignedJWT signedJWT = parseAndVerifyToken(jwtString);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            if (claimsSet.getStringClaim(JwtUtil.PRINCIPAL).equalsIgnoreCase(principalId) &&
                    claimsSet.getSubject().equalsIgnoreCase(SUBJECT_AUTHORIZATION_LIST)) {
                String responseString = claimsSet.getStringClaim(RESPONSE);
                List<String> roles = objectMapper.readValue(responseString, List.class);
                AuthorizationList authoriationList = new AuthorizationList(roles);

                String reasonsJson = claimsSet.getStringClaim(REASONS);
                if (StringUtils.isNotBlank(reasonsJson)) {
                    Set<DecisionReason> reasons = objectMapper.readValue(reasonsJson, TypeFactory.defaultInstance().constructCollectionType(Set.class, DecisionReason.class));
                    if (reasons != null && !reasons.isEmpty()) {
                        authoriationList.getReasons().addAll(reasons);
                    }
                }

                return authoriationList;
            }
            throw new WebApiClientException("Authorization token cannot be verified");
        } catch (ParseException | IOException e) {
            throw new WebApiClientException(e.getMessage());
        }
    }

    public List<YpaOrganization> getCompanies(String jwtString, String delegateId) throws WebApiClientException {
        try {
            SignedJWT signedJWT = parseAndVerifyToken(jwtString);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            if (claimsSet.getStringClaim(JwtUtil.END_USER).equalsIgnoreCase(delegateId) &&
                    claimsSet.getSubject().equalsIgnoreCase(SUBJECT_ORG_ROLES)) {
                String responseString = claimsSet.getStringClaim(RESPONSE);
                List<YpaOrganization> orgRoles = objectMapper.readValue(responseString, new TypeReference<List<YpaOrganization>>(){});
                return orgRoles;
            }
            throw new WebApiClientException("OrganizationList token cannot be verified");
        } catch (ParseException | IOException e) {
            throw new WebApiClientException(e.getMessage());
        }
    }
}
