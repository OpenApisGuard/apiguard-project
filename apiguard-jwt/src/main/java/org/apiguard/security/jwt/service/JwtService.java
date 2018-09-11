package org.apiguard.security.jwt.service;


import org.apiguard.security.jwt.exceptions.ApiGuardJwtException;
import org.json.JSONObject;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.InvalidSignatureException;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@Service
public class JwtService {

    private static final String ISSUER = "iss";
    private static final String NOT_BEFORE = "nbf";
    private static final String EXPIRES = "exp";

    public boolean verify(String jwtStr, String secret) throws ApiGuardJwtException {

        if (StringUtils.isEmpty(jwtStr) || StringUtils.isEmpty(secret)) {
            return false;
        }

        try {
            // Exception when validation failed: org.springframework.security.jwt.crypto.sign.InvalidSignatureException
            MacSigner verifer = new MacSigner(secret);
            JwtHelper.decodeAndVerify(jwtStr, verifer);
        }
        catch(InvalidSignatureException e) {
            //log validation failed
            return false;
        }
        catch (Exception e) {
            // log exception
            return false;
        }

        return true;
    }

    public String getClaims(String token) {
        return JwtHelper.decode(token).getClaims();
    }

    public String getClaimsAndVerify(String jwtStr, String secret) throws ApiGuardJwtException {
        if (StringUtils.isEmpty(jwtStr) || StringUtils.isEmpty(secret)) {
            return null;
        }

        try {
            // Exception when validation failed: org.springframework.security.jwt.crypto.sign.InvalidSignatureException
            MacSigner verifer = new MacSigner(secret);

            Jwt jwt = JwtHelper.decodeAndVerify(jwtStr, verifer);
            return jwt.getClaims();
        }
        catch(InvalidSignatureException e) {
            //log validation failed
            return null;
        }
        catch (Exception e) {
            // log exception
            return null;
        }
    }

    public String getIssuer(String jwtStr) throws ApiGuardJwtException {
        if (StringUtils.isEmpty(jwtStr)) {
            return null;
        }

        try {
            Jwt jwt = JwtHelper.decode(jwtStr);
            if (jwt == null) {
                return null;
            }

            String claims = jwt.getClaims();
            JSONObject jo = new JSONObject(claims);
            return (String) jo.get(ISSUER);
        }
        catch(InvalidSignatureException e) {
            //log validation failed
            return null;
        }
        catch (Exception e) {
            // log exception
            return null;
        }
    }

    public boolean isBefore(String claims) {
        if (StringUtils.isEmpty(claims)) {
            return false;
        }

        try {
            JSONObject jo = new JSONObject(claims);
            Long notBefore = (Long) jo.get(NOT_BEFORE);
            return new Date().before(new Date(notBefore));
        }
        catch (Exception e) {
            // log exception
            return false;
        }
    }

    public boolean isExpired(String claims) {
        if (StringUtils.isEmpty(claims)) {
            return false;
        }

        try {
            JSONObject jo = new JSONObject(claims);
            Long exp = (Long) jo.get(EXPIRES);
            return new Date().after(new Date(exp));
        }
        catch (Exception e) {
            // log exception
            return false;
        }
    }
}
