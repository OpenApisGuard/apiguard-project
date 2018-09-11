package org.apiguard.security.jwt.service;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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
public class JwtServiceTest {

    private static JwtService service;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @BeforeClass
    public static void setup() {
        service = new JwtService();
    }

    @Test
    public void testJwtVerify() throws Exception {
        String secret = "e71829c351aa4242c2719cbfbe671c09";
        String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhMzZjMzA0OWIzNjI0OWEzYzlmODg5MWNiMTI3MjQzYyIsImV4cCI6MTQ0MjQzMDA1NCwibmJmIjoxNDQyNDI2NDU0LCJpYXQiOjE0NDI0MjY0NTR9.AhumfY35GFLuEEjrOXiaADo7Ae6gt_8VLwX7qffhQN4";
        boolean verify = service.verify(jwt, secret);
        Assert.assertTrue(verify);
    }

//    @Test
//    public void testJwtVerify2() throws Exception {
//        String secret = "0ce5f35e-a9b9-4717-93bd-4d3daa6b60d3";
//        String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlzcyI6IjdjZWIxNWM3LTdlNjktNDA5NC05NWU2LTk3M2UxM2I0YWY1NiJ9.0E-LJ3DSo1wvNvxq9NkK7IouUn2SA8ck-Xnn_PPTj5g";
//        boolean verify = service.verify(jwt, secret);
//        Assert.assertTrue(verify);
//    }

    @Test
    public void testJwtVerifySecretNull() throws Exception {
        String secret = null;
        String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhMzZjMzA0OWIzNjI0OWEzYzlmODg5MWNiMTI3MjQzYyIsImV4cCI6MTQ0MjQzMDA1NCwibmJmIjoxNDQyNDI2NDU0LCJpYXQiOjE0NDI0MjY0NTR9.AhumfY35GFLuEEjrOXiaADo7Ae6gt_8VLwX7qffhQN4";
        boolean verify = service.verify(jwt, secret);
        Assert.assertFalse(verify);
    }

    @Test
    public void testJwtVerifyJwtNull() throws Exception {
        String secret = "e71829c351aa4242c2719cbfbe671c09";
        String jwt = null;
        boolean verify = service.verify(jwt, secret);
        Assert.assertFalse(verify);
    }

    @Test
    public void testJwtVerifyEmptySecret() throws Exception {
        String secret = "";
        String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhMzZjMzA0OWIzNjI0OWEzYzlmODg5MWNiMTI3MjQzYyIsImV4cCI6MTQ0MjQzMDA1NCwibmJmIjoxNDQyNDI2NDU0LCJpYXQiOjE0NDI0MjY0NTR9.AhumfY35GFLuEEjrOXiaADo7Ae6gt_8VLwX7qffhQN4";
        boolean verify = service.verify(jwt, secret);
        Assert.assertFalse(verify);
    }

    @Test
    public void testJwtVerifyEmptyJwt() throws Exception {
        String secret = "e71829c351aa4242c2719cbfbe671c09";
        String jwt = "";
        boolean verify = service.verify(jwt, secret);
        Assert.assertFalse(verify);
    }

    @Test
    public void testJwtVerifyInvalidSecret() throws Exception {
        String secret = "e71829c351aa4242c2719c";
        String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhMzZjMzA0OWIzNjI0OWEzYzlmODg5MWNiMTI3MjQzYyIsImV4cCI6MTQ0MjQzMDA1NCwibmJmIjoxNDQyNDI2NDU0LCJpYXQiOjE0NDI0MjY0NTR9.AhumfY35GFLuEEjrOXiaADo7Ae6gt_8VLwX7qffhQN4";
        boolean verify = service.verify(jwt, secret);
        Assert.assertFalse(verify);
    }

    @Test
    public void testJwtVerifyInvalidJwt() throws Exception {
        String secret = "e71829c351aa4242c2719cbfbe671c09";
        String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhMzZjMzA0OWIzNjI0OWEzYzlmODg5MWNiMTI3MjQzYyIsImV4cCI6MTQ0MjQzMDA1NCwibmJmIjoxNDQyNDI2NDU0LCJpYXQiOjE0NDI0MjY0NTR9.AhumfY35GFLuEEjrOXiaADo";
        boolean verify = service.verify(jwt, secret);
        Assert.assertFalse(verify);
    }

    @Test
    public void testJwtGetClaims() throws Exception {
        String secret = "e71829c351aa4242c2719cbfbe671c09";
        String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhMzZjMzA0OWIzNjI0OWEzYzlmODg5MWNiMTI3MjQzYyIsImV4cCI6MTQ0MjQzMDA1NCwibmJmIjoxNDQyNDI2NDU0LCJpYXQiOjE0NDI0MjY0NTR9.AhumfY35GFLuEEjrOXiaADo7Ae6gt_8VLwX7qffhQN4";
        String claimsAndVerify = service.getClaimsAndVerify(jwt, secret);
        Assert.assertNotNull(claimsAndVerify);
        Assert.assertEquals("{\"iss\":\"a36c3049b36249a3c9f8891cb127243c\",\"exp\":1442430054,\"nbf\":1442426454,\"iat\":1442426454}", claimsAndVerify);
    }

    @Test
    public void testJwtGetIssuer() throws Exception {
        String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhMzZjMzA0OWIzNjI0OWEzYzlmODg5MWNiMTI3MjQzYyIsImV4cCI6MTQ0MjQzMDA1NCwibmJmIjoxNDQyNDI2NDU0LCJpYXQiOjE0NDI0MjY0NTR9.AhumfY35GFLuEEjrOXiaADo7Ae6gt_8VLwX7qffhQN4";
        String issuer = service.getIssuer(jwt);
        Assert.assertEquals("a36c3049b36249a3c9f8891cb127243c", issuer);
    }

    @Test
    public void testInvalidJwtGetIssuer() throws Exception {
        String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhMzZjMzA0OWIzNjI0OWEzYzlmODg5MWNiMTI3MjQzYyIsImV4cCI6MTQ0MjQzMDA1NCwibmJmIjoxND";
        String issuer = service.getIssuer(jwt);
        Assert.assertNull(issuer);
    }

    @Test
    public void testNullJwtGetIssuer() throws Exception {
        String jwt = null;
        String issuer = service.getIssuer(jwt);
        Assert.assertNull(issuer);
    }

    @Test
    public void testEmptyJwtGetIssuer() throws Exception {
        String jwt = "";
        String issuer = service.getIssuer(jwt);
        Assert.assertNull(issuer);
    }
}
